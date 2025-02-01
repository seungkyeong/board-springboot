package service;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import aws.S3Service;
import dao.BoardDAO;
import dto.BoardDTO;
import dto.CommentDTO;
import dto.SearchDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Service
public class BoardService {
	@Autowired                     
	private final BoardDAO Boarddao;
	@Autowired                     
	private final S3Service S3service;
	@Autowired
    private RedisTemplate<String, Long> redisTemplate;
	

    public BoardService(BoardDAO Boarddao, S3Service S3service) { 
        this.Boarddao = Boarddao;
        this.S3service = S3service;
    }

    /* 게시판 목록 조회 */ 
    public List<Object> getAllBoardList(SearchDTO search) throws Exception{
    	System.out.printf("************boardList: %s", search.getType());
    	
    	//Count 조회
    	search.setCountFlag(1);
    	int BoardListCount = Boarddao.getAllCountBoardList(search);
    	System.out.println("boardList select finish");
    	
    	//10개씩 조회
    	search.setCountFlag(0);
    	List<BoardDTO> BoardList = Boarddao.getAllBoardList(search);
    	
    	
    	// Redis 세팅
        BoardList.forEach(board -> {
        	 String redisKey = "count:view:" + board.getSysNo();
        	 ValueOperations<String, Long> ops = redisTemplate.opsForValue();

        	 // Redis에 해당 키가 없는 경우만 설정 
        	 ops.setIfAbsent(redisKey, board.getView());
        	 
        	//Redis에서 조회해서 view만 세팅
        	 Long viewCount = ops.get(redisKey);
        	 board.setView(viewCount);
        });
        
        if(search.getType().equals("likeList")) {
        	BoardList.forEach(board -> {
           	 String redisKey = "count:like:" + board.getSysNo();
           	 ValueOperations<String, Long> ops = redisTemplate.opsForValue();

           	 // Redis에 해당 키가 없는 경우만 설정 
           	 ops.setIfAbsent(redisKey, board.getLikeCount());
           	 
           	//Redis에서 조회해서 view만 세팅
           	 Long likeCount = ops.get(redisKey);
           	 board.setLikeCount(likeCount);
           });
        }
        
        List<Object> data = new ArrayList<>();
        data.add(BoardListCount);
        data.add(BoardList);
        
        return data;
    }
    
    /* 파일 업로드 */ 
    public List<URL> uploadFile(MultipartFile[] files) throws Exception{
    	List<URL> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            // 파일 이름 생성(uuid_file)
        	String decodeFileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
            String fileName = UUID.randomUUID().toString().replace("-", "") + decodeFileName;

            // S3 임시 업로드 url 발급
            URL url = S3service.generatePresignedUrl(fileName);
            fileUrls.add(url); // 파일 URL 리스트에 추가
        }
        return fileUrls;
    }
    
    /* 게시물 생성 & 수정 */ 
    public int postBoard(BoardDTO requestParam) throws Exception{
    	//strImgPath 넣기
    	requestParam.setStrImgPath(String.join(",", requestParam.getImgPath()));
    	
        if(requestParam.getSysNo() == "") { //신규 생성인 경우 
        	return Boarddao.createBoard(requestParam);
        }else { //수정인 경우 
        	System.out.println(requestParam.toString());
        	return Boarddao.updateBoard(requestParam);
        }
        
    }
    
    /* 게시물 상세 조회 */ 
    public List<Object> getBoardDetail(SearchDTO search) throws Exception{  
    	BoardDTO boardDto = Boarddao.getBoardDetail(search);
    	List<CommentDTO> commentDto = Boarddao.getComment(search);
    	System.out.println(commentDto.toString());
    	
        Map<String, CommentDTO> rootMap = new HashMap<>();
        List<CommentDTO> rootComments = new ArrayList<>();
        List<CommentDTO> childComments = new ArrayList<>();

        for (CommentDTO comment : commentDto) {

            if (comment.getParSysNo().equals("")) { //루트 댓글인 경우(부모) 
                rootComments.add(comment);
                rootMap.put(comment.getSysNo(), comment); 
            } else { //루트 댓글이 아닌 경우(자식)
                childComments.add(comment);
            }
        }

        // 자식을 돌면서 부모의 Replies에 자식 세팅
        for (CommentDTO comment : childComments) {
            CommentDTO parent = rootMap.get(comment.getParSysNo()); // Map에서 부모 찾기
            if (parent != null) {
                parent.getReplies().add(comment);
            }
        }
    	
    	System.out.printf("getStrImgPath: %s", boardDto.getStrImgPath());
    	List<String> imgPath = new ArrayList<>(Arrays.asList(boardDto.getStrImgPath().split(",")));
    	boardDto.setImgPath(imgPath);
    	
    	List<Object> data = new ArrayList<>();
        data.add(boardDto);
        data.add(rootComments);
    	
        return data;
    }
    
    /* 게시물 조회수 증가 */ 
    public int addViewCount(Map<String, String> requestData) throws Exception{  
    	//redis에 1 증가
    	String redisKey = "count:" + requestData.get("type") + ":" + requestData.get("sysNo");
    	redisTemplate.opsForValue().increment(redisKey, 1);
    	
    	//view 값 가져오기
    	ValueOperations<String, Long> ops = redisTemplate.opsForValue();
    	Long viewCount = ops.get(redisKey);
    	
    	//expire redis 세팅 & ttl 세팅
    	String redisExpiredKey = "expired:" + requestData.get("type") + ":" + requestData.get("sysNo");
    	ops.set(redisExpiredKey, viewCount, 1, TimeUnit.MINUTES);
    	
        return 1; 
    }
    
    /* 게시물 조회수 1분마다 DB에 반영 */
    public void syncViewCount(Map<String, Object> requestData) throws Exception{
    	System.out.printf("requestData.get(\"type\") 결과: %s", requestData.get("type")=="like");
    	if("like".equals(requestData.get("type"))) {
    		requestData.put("type", "like_count");
    	}
    	
    	// DB에 조회수 반영
        int data = Boarddao.syncViewCount(requestData);
    }
    
    /* 댓글 생성 & 수정 */ 
    public int postComment(CommentDTO requestParam) throws Exception{
        return Boarddao.createComment(requestParam);
    }
    
//    public String testRedisConnection() {
//        // Redis에 간단한 키-값 쌍을 설정하고 확인하는 예제
//        String key = "testKey";
//        String value = "testValue";
//
//        // Redis에 값을 설정
//        redisTemplate.opsForValue().set(key, value);
//
//        // Redis에서 값을 가져와서 확인
//        String retrievedValue = redisTemplate.opsForValue().get(key);
//
//        return "Stored value: " + retrievedValue;
//    }
}

