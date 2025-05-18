package controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import constant.ExceptionConstant;
import dto.BoardDTO;
import dto.CommentDTO;
import dto.NotificationDTO;
import dto.ResponseDTO;
import dto.SearchDTO;
import entity.Board;
import exceptionHandle.GeneralException;
import repository.BoardRepository;
import repository.UserRepository;
import service.BoardService;

@RestController
@RequestMapping("/api/board") 
public class BoardController {
	@Autowired
    private final BoardService boardService; 
	
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /* 게시판 목록 조회 */
    @PostMapping("/list")
    public ResponseEntity<ResponseDTO<Object>> getBoardList(@RequestBody SearchDTO search) throws Exception {
    	List<Object> data = boardService.getAllBoardList(search);
    	if(data.size() <= 0) {
    		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    	} else {
    		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    	}
    }
    
    /* 파일 업로드 */
    @PostMapping ("/post/fileUpload") 
    public List<URL> uploadFile(@RequestParam("files") MultipartFile[] files) throws Exception {
    	//파일 업로드
    	List<URL> fileUrls = boardService.uploadFile(files); //성공:1, 실패:0

        // 업로드된 파일들의 URL 반환
        return fileUrls;
    }
    
    /* 업로드 파일 삭제 */
    @PostMapping ("/post/fileDelete") 
    public ResponseDTO<Object> deleteFile(@RequestBody Map<String, List<String>> requestData) throws Exception {
    	List<String> keys = requestData.get("keys");
    	
    	//파일 업로드
    	return boardService.deleteFiles(keys); //성공:1, 실패:0
    }
    
    /* 게시물 생성, 수정 */ 
    /* sysNo 없을 경우 신규 생성, sysNo 있을 경우 수정 */
    @PostMapping ("/post") 
    public ResponseEntity<ResponseDTO<Object>> postBoard(@RequestBody BoardDTO board) throws Exception {
    	boardService.postBoard(board); 

    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 게시물 상세 조회 */
    @PostMapping("/detail") 
    public ResponseEntity<ResponseDTO<Object>> getBoardDetail(@RequestBody SearchDTO search) throws Exception {
    	List<Object> data = boardService.getBoardDetail(search);
    	Board board = boardRepository.findByUserId(request.get("id"))
    			.orElseThrow(() -> new GeneralException(ExceptionConstant.NOT_FOUNT_BOARD.getCode(), ExceptionConstant.NOT_FOUNT_BOARD.getMessage()));
		result = user.getPassword();
		
		
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 게시물 조회수, 좋아요 증가 */
    @PostMapping("/updateCount") 
    public ResponseDTO<Object> updateCount(@RequestBody Map<String, Object> requestData) throws Exception {
    	System.out.printf("sysNo: ", requestData.get("sysNo"));
    	int data = boardService.updateCount(requestData);
    	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>();
    }
    
    /* 댓글 생성 & 수정 
     * sysNo 없을 경우 신규 생성,
     * sysNo 있을 경우 수정*/
    @PostMapping ("/comment") 
    public ResponseDTO<Object> postComment(@RequestBody CommentDTO comment) throws Exception {
    	System.out.printf("sysNo: %s\n", comment.getSysNo());
    	
    	//게시물 생성 / 수정
    	int data = boardService.postComment(comment); //성공:1, 실패:0

    	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>();
    }
     
    // 게시물 삭제
    @PostMapping("boardDelete")
    public ResponseDTO<Object> deleteBoardList(@RequestBody Map<String, Object> requestData) throws Exception {
    	int data = boardService.deleteBoardList(requestData);
    	
    	if(data == 0) { //게시물 삭제 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>(); 
    }
    
    // 좋아요 삭제
    @PostMapping("likeDelete")
    public ResponseDTO<Object> deleteLikeList(@RequestBody Map<String, Object> requestData) throws Exception {
    	int data = boardService.deleteLikeList(requestData);
    	
    	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>(); 
    }
    
    // 알림 조회
    @PostMapping("notiList")
    public ResponseDTO<Object> getNotiList(@RequestBody NotificationDTO requestData) throws Exception {
    	List<NotificationDTO> data = boardService.getNotiList(requestData);
    	
    	if(data.size() < 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>(data); 
    }
    
    // 알림 Flag 업데이트
    @PostMapping("update/notiList")
    public ResponseDTO<Object> updateNotiReadFlag(@RequestBody NotificationDTO requestData) throws Exception {
    	int data = boardService.updateNotiReadFlag(requestData);
    	
    	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>(data); 
    }
}


