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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import aws.S3Service;
import constant.AppConstant;
import constant.ExceptionConstant;
import dto.BoardDTO;
import dto.CommentDTO;
import dto.LikeDTO;
import dto.ResponseDTO;
import dto.SearchDTO;
import entity.Board;
import entity.Comment;
import exceptionHandle.GeneralException;
import lombok.RequiredArgsConstructor;
import repository.BoardCustomRepositoryImpl;
import repository.BoardRepository;
import repository.CommentRepository;
import repository.LikeRepository;
import webSocketHandle.NotificationWebSocketHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@RequiredArgsConstructor
@Service
public class BoardService {       
	private final S3Service S3service;
    private final RedisTemplate<String, Long> redisTemplate;
	private final NotificationWebSocketHandler notificationWebSocketHandler;
	private final NotificationService NotificationService;
	private final BoardRepository boardRepository;
	private final LikeRepository likeRepository;
	private final CommentRepository commentRepository;
	private final BoardCustomRepositoryImpl boardCustomRepository;

    /* 게시판 목록 조회 */ 
    public List<Object> getAllBoardList(SearchDTO search) throws Exception{
    	List<BoardDTO> BoardList = new ArrayList<>();
    	    	
    	//게시판 목록 조회
    	Page<Board> boardPage = boardCustomRepository.getBoard(search);
    	List<Board> boards = boardPage.getContent();
    	long BoardListCount = boardPage.getTotalElements();
    	
    	// Redis 세팅
    	boards.forEach(board -> {
    		BoardDTO boardDto = BoardDTO.fromEntity(board);
        	
        	// 해당 키가 없는 경우만 설정 
        	String redisKey = AppConstant.RedisKey.COUNT + ":" + AppConstant.RedisKey.VIEW + boardDto.getSysNo();
        	redisTemplate.opsForValue().setIfAbsent(redisKey, board.getView());
        	 
        	//Redis에서 조회해서 view만 세팅
        	Long viewCount = redisTemplate.opsForValue().get(redisKey);
        	boardDto.setView(viewCount);
        	
        	BoardList.add(boardDto);
        });
        
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
        	String decodeFileName = URLDecoder.decode(file.getOriginalFilename(), AppConstant.UTF );
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
    @Transactional
    public void postBoard(BoardDTO boardDto) throws Exception{
    	//이미지 경로 String으로 바꾸기 
    	boardDto.setStrImgPath(String.join(",", boardDto.getImgPath()));
    	
        if(boardDto.getSysNo() == null) { //신규 생성인 경우 
        	boardDto.setSysNo(UUID.randomUUID().toString().replace("-", "")); //게시물 system_no 생성
            boardRepository.save(boardDto.toEntity());
        }else { //수정인 경우 
        	//게시물 조회
        	Board board = boardRepository.findById(boardDto.getSysNo())
        			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_BOARD.getCode(), ExceptionConstant.NOT_FOUND_BOARD.getMessage()));

        	//게시물 수정
        	board.updateBoard(boardDto.getTitle(), boardDto.getContent(), boardDto.getStrImgPath());
        }
    }
    
    /* 게시물 상세 조회 */ 
    @Transactional
    public List<Object> getBoardDetail(SearchDTO search) throws Exception{  
    	//boardSysNo
    	String boardSysNo = search.getSearchList().get(AppConstant.Property.SYSNO);
    	//Comment 계층 구조를 위한 변수
    	List<CommentDTO> commentList = new ArrayList<>();
        Map<String, CommentDTO> parentComments = new HashMap<>();
        List<CommentDTO> childComments = new ArrayList<>();
    	
    	//게시물 상세 조회
    	BoardDTO boardDto = boardCustomRepository.getBoardDetail(search);
    	if(boardDto == null) { //상세 조회 페이지가 없으면 에러 
    		throw new GeneralException(ExceptionConstant.NOT_FOUND_BOARD.getCode(), ExceptionConstant.NOT_FOUND_BOARD.getMessage());
    	}
    	
    	//댓글 조회
    	List<Comment> comments = commentRepository.findByBoardSysNoOrderByCreateDateDesc(boardSysNo);
    	
        //댓글 순회하면서 부모(루트), 자식 나누기 
        for (Comment comment : comments) {
        	CommentDTO commentdto = CommentDTO.fromEntity(comment);
        	
            if (comment.getParSysNo().equals("")) { //루트 댓글인 경우(부모)
            	commentList.add(commentdto);
                parentComments.put(comment.getSysNo(), commentdto); 
            } else { //루트 댓글이 아닌 경우(자식)
                childComments.add(commentdto);
            }
        }

        //자식 댓글 순회하면서 부모의 대댓글에 자식 세팅
        for (CommentDTO comment : childComments) {
        	//부모 있는지 확인
        	if(comment.getParSysNo() != null && !comment.getParSysNo().equals("")) {
        		CommentDTO parent = parentComments.get(comment.getParSysNo()); //부모 찾기
        		
                if (parent != null) { //부모가 삭제되지 않은 경우 
                    parent.getReplies().add(comment);
                } else { //부모가 삭제된 경우
                	//삭제된 부모 생성
                	CommentDTO commentdto = new CommentDTO(comment.getParSysNo(), comment.getBoardSysNo(), "삭제된 댓글입니다.");
                	commentList.add(commentdto);
                	
                	//parentComments에 삭제된 부모 세팅
                	parentComments.put(comment.getParSysNo(), commentdto);
                	parent = parentComments.get(comment.getParSysNo());
                	
                	//삭제된 부모 대댓글에 자식 댓글 세팅
                	parent.getReplies().add(comment);
                }
        	}
        }
    	
        //img String -> Array로 변경
    	boardDto.setImgPath(new ArrayList<>(Arrays.asList(boardDto.getStrImgPath().split(","))));
    	
    	List<Object> data = new ArrayList<>(Arrays.asList(boardDto, commentList));
        return data;
    }
    
    /* 게시물 조회수 증가 */ 
    @Transactional
    public void updateCount(Map<String, Object> request) throws Exception{  
    	ValueOperations<String, Long> ops = redisTemplate.opsForValue();
    	
    	//redis에 조회수 1 증가
    	String redisKey = AppConstant.RedisKey.COUNT + ":" + request.get(AppConstant.Property.TYPE) + ":" + request.get(AppConstant.Property.SYSNO);
    	ops.increment(redisKey, 1);
    	
    	//조회수 값 가져오기
    	Long Count = ops.get(redisKey);
    	
    	//expire redis 세팅 & ttl 세팅
    	String redisExpiredKey = AppConstant.RedisKey.EXPIRED + ":" + request.get(AppConstant.Property.TYPE) + ":" + request.get(AppConstant.Property.SYSNO);
    	ops.set(redisExpiredKey, Count, 1, TimeUnit.MINUTES);
    }
    
    /* 게시물 좋아요 증가,감소 */ 
    @Transactional
    public void updateLike(LikeDTO like) throws Exception{  
    	if(like.getAction().equals(AppConstant.RedisKey.LIKE)) { //좋아요 누른 경우 
    		like.setSysNo(UUID.randomUUID().toString().replace("-", "")); //회원 system_no 생성
    		likeRepository.save(like.toEntity());
    	} else { //좋아요 취소한 경우 
    		likeRepository.deleteByBoardSysNoAndUserSysNo(like.getBoardSysNo(), like.getUserSysNo());
    	}
    }
    
    /* 게시물 조회수 1분마다 DB에 반영(TTL 만료시) */
    @Transactional
    public void syncCount(Map<String, Object> requestData) throws Exception{
    	String sysNo = (String) requestData.get(AppConstant.Property.SYSNO);
    	long view = (long) requestData.get(AppConstant.RedisKey.VIEW);
    	
    	//조회수 조회
        Board board = boardRepository.findById(sysNo)
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_BOARD.getCode(), ExceptionConstant.NOT_FOUND_BOARD.getMessage()));

        //조회수 수정
        board.updateBoard(view);
    }
    
    /* 댓글 생성, 수정 */ 
    @Transactional
    public void postComment(CommentDTO commentDto) throws Exception{
    	String boardCreater = commentDto.getBoardCreaterSysNo(); //게시물 작성자 SysNo
    	String commentCreater = commentDto.getUserSysNo(); //댓글 작성자 SysNo
    	
    	if(commentDto.getSysNo().equals("")) { //신규 생성인 경우 
    		//댓글 생성
    		commentDto.setSysNo(UUID.randomUUID().toString().replace("-", "")); //댓글 system_no 생성
            commentRepository.save(commentDto.toEntity());
        }else { //수정인 경우
        	//댓글 조회
        	Comment comment = commentRepository.findById(commentDto.getSysNo())
        			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUND_COMMENT.getCode(), ExceptionConstant.NOT_FOUND_COMMENT.getMessage()));

        	//댓글 수정
        	comment.updateComment(commentDto.getComment());
        }
    	
    	//게시글 작성자에게 알림 전송(게시글 작성자 != 댓글 작성자인 경우만 해당)
        if (!boardCreater.equals(commentCreater)) {
        	//Noti 저장
        	NotificationService.saveNotification(commentDto);
           
        	//Noti 알림 보내기
        	notificationWebSocketHandler.sendNotification(commentDto.getBoardCreaterSysNo(), AppConstant.SOCKET_MESSAGE);
        }
    }
    
    /* 게시물 삭제(다중) */ 
    @Transactional
    public void deleteBoardList(Map<String, Object> request) throws Exception{
    	List<String> deleteList = (List<String>) request.get(AppConstant.Property.DELETE_LIST);
    	
    	//게시물 삭제
    	boardRepository.deleteBoard(deleteList);
        
        //게시물 Comment 삭제
        commentRepository.deleteComment(deleteList);
        
        //게시물 좋아요 List 삭제
        likeRepository.deleteLikelog(deleteList);
        
        //Redis 삭제
        if (deleteList != null) {
            for (String sysNo : deleteList) {
                String redisViewKey = AppConstant.RedisKey.COUNT + ":" + AppConstant.RedisKey.VIEW + ":" + sysNo; 
                redisTemplate.delete(redisViewKey); 
            }
        } 
    }
    
    /* 좋아요 삭제(다중) */ 
    @Transactional
    public void deleteLikeList(Map<String, Object> request) throws Exception{
    	List<String> deleteList = (List<String>) request.get(AppConstant.Property.DELETE_LIST);
        String userSysNo = (String) request.get(AppConstant.Property.USER_SYSNO);
        
    	//게시물 좋아요 List 삭제
    	likeRepository.deleteMyLikelog(userSysNo, deleteList);
    }
    
    /* 댓글 삭제 */ 
    @Transactional
    public void deleteComment(Map<String, Object> request) throws Exception{
    	String sysNo = (String) request.get("sysNo");
    	//댓글 삭제
    	commentRepository.deleteById(sysNo);
    }
}

