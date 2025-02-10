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

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import aws.S3Service;
import constant.ExceptionConstant;
import dao.BoardDAO;
import dto.BoardDTO;
import dto.CommentDTO;
import dto.NotificationDTO;
import dto.ResponseDTO;
import dto.SearchDTO;
import exceptionHandle.GeneralException;
import webSocketHandle.NotificationWebSocketHandler;

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
	@Autowired
	private final NotificationWebSocketHandler notificationWebSocketHandler;
	@Autowired                     
	private final NotificationService NotificationService;
	@Autowired
    private SqlSession sqlSession;
	

    public BoardService(BoardDAO Boarddao, S3Service S3service,@Lazy NotificationWebSocketHandler notificationWebSocketHandler, NotificationService NotificationService) { 
        this.Boarddao = Boarddao;
        this.S3service = S3service;
        this.notificationWebSocketHandler = notificationWebSocketHandler;
        this.NotificationService = NotificationService;
    }

    /* 게시판 목록 조회 */ 
    public List<Object> getAllBoardList(SearchDTO search) throws Exception{
    	//Count 조회
    	search.setCountFlag(1);
    	int BoardListCount = Boarddao.getAllCountBoardList(search);
    	
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
    
    /* 업로드 파일 삭제 */ 
    public ResponseDTO<Object> deleteFiles(List<String> keys) throws Exception{
    	return S3service.deleteFiles(keys);
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
    
    /* 게시물 조회수/좋아요 수 증가 */ 
    public int updateCount(Map<String, Object> requestData) throws Exception{  
    	//redis에 1 증가
    	String redisKey = "count:" + requestData.get("type") + ":" + requestData.get("sysNo");
    	if(requestData.get("action").equals("Increase")) {
    		redisTemplate.opsForValue().increment(redisKey, 1);
    	}else {
    		redisTemplate.opsForValue().decrement(redisKey, 1);
    	}
    	
    	//view 값 가져오기
    	ValueOperations<String, Long> ops = redisTemplate.opsForValue();
    	Long Count = ops.get(redisKey);
    	
    	//expire redis 세팅 & ttl 세팅
    	String redisExpiredKey = "expired:" + requestData.get("type") + ":" + requestData.get("sysNo");
    	ops.set(redisExpiredKey, Count, 1, TimeUnit.MINUTES);
    	
    	//좋아요 로그 
    	if(requestData.get("type").equals("like")) {
    		Boarddao.createLikeLog(requestData);
    	}
    	
        return 1; 
    }
    
    /* 게시물 조회수 1분마다 DB에 반영 */
    public void syncCount(Map<String, Object> requestData) throws Exception{
    	System.out.printf("requestData.get(\"type\") 결과: %s", requestData.get("type")=="like");
    	if("like".equals(requestData.get("type"))) {
    		requestData.put("type", "like_count");
    	}
    	
    	// DB에 조회수 반영
        int data = Boarddao.syncCount(requestData);
    }
    
    /* 댓글 생성 & 수정 */ 
    public int postComment(CommentDTO requestParam) throws Exception{
    	//게시물 작성자 SysNo
    	String boardCreaterSysNo = requestParam.getBoardCreaterSysNo();
    	//댓글 작성자 SysNo
    	String commentCreaterSysNo = requestParam.getUserSysNo();
    	
    	//댓글 생성
    	int data = Boarddao.createComment(requestParam);
    	
    	if(data == 0) { //실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	//게시글 작성자 조회 (DB에서 가져오기)
        if (!boardCreaterSysNo.equals(commentCreaterSysNo)) { // 본인 댓글이면 알림 X
        	System.out.println("------------본인 댓글 아님");
        	
        	// 알림 저장
        	String message = "게시글에 새로운 댓글이 달렸습니다!";
        	data = NotificationService.saveNotification(requestParam.getBoardCreater(), boardCreaterSysNo, requestParam.getBoardSysNo(), message, requestParam.getTitle());
           
        	//알림 보내기
        	notificationWebSocketHandler.sendNotification(boardCreaterSysNo, "게시글에 새로운 댓글이 달렸습니다!");
        }
        return data;
    }
    
    /* 게시물 다중 삭제 */ 
    public int deleteBoardList(Map<String, Object> requestParam) throws Exception{
    	// 트랜잭션 시작 (예제에서는 MyBatis 사용)
//        sqlSession.getConnection().setAutoCommit(false);
        
    	//게시물 삭제
        int data = Boarddao.deleteBoardList(requestParam);
        
        if(data == 0) {
        	throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
        }
        
        //게시물 Comment 삭제
        data = Boarddao.deleteCommentList(requestParam);
        
        if(data == 0) {
        	throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
        }
        
        //게시물 좋아요 List 삭제
        data = Boarddao.deleteLikeList(requestParam);
        //Redis 삭제
        List<String> deleteList = (List<String>)requestParam.get("deleteList");
        if (deleteList != null) {
            for (String sysNo : deleteList) {
                String redisLikeKey = "count:like:" + sysNo; // Redis 키 생성
                String redisViewKey = "count:view:" + sysNo; // Redis 키 생성
                redisTemplate.delete(redisLikeKey); // Redis에서 삭제
                redisTemplate.delete(redisViewKey); // Redis에서 삭제
            }
        }
        	
        // 모든 삭제가 정상적으로 수행되면 커밋
//        sqlSession.getConnection().commit();
            
    	return data;
    }
    
    /* 좋아요 다중 삭제 */ 
    public int deleteLikeList(Map<String, Object> requestParam) throws Exception{
    	//게시물 좋아요 List 삭제
    	int data = Boarddao.deleteLikeList(requestParam);
    	//좋아요 Redis 1 감소
    	 List<String> deleteList = (List<String>)requestParam.get("deleteList");
         if (deleteList != null) {
             for (String sysNo : deleteList) {
            	 requestParam.put("sysNo", sysNo);
            	 data = updateCount(requestParam);
             }
         }
    	return data;
    }
    
    /* 알림 조회 */ 
    public List<NotificationDTO> getNotiList(NotificationDTO requestParam) throws Exception{
    	return Boarddao.getNotiList(requestParam); 
    }
    
    /* 알림 Flag 업데이트 */ 
    public int updateNotiReadFlag(NotificationDTO requestParam) throws Exception{
    	return Boarddao.updateNotiReadFlag(requestParam); 
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

