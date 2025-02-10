package controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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
import exceptionHandle.GeneralException;
import service.BoardService;

@RestController //RESTful API를 작성하기 위한 어노테이션 
@RequestMapping("/api/board") 
public class BoardController {
	@Autowired
    private final BoardService boardService; //필드 선언, private: 해당 클래스에서만 접근 가능, final: 초기화 후 변경 불가능
	
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /* 게시판 목록 조회 */
    @PostMapping("/list")
    public ResponseDTO<Object> getBoardList(@RequestBody SearchDTO search) throws Exception {
    	List<Object> data = boardService.getAllBoardList(search);
    	if(data.size() <= 0) {
    		return new ResponseDTO<>();
    	} else {
    		return new ResponseDTO<>(data);
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
    
    /* 게시물 생성 & 수정 
     * sysNo 없을 경우 신규 생성,
     * sysNo 있을 경우 수정*/
    @PostMapping ("/post") 
    public ResponseDTO<Object> postBoard(@RequestBody BoardDTO board) throws Exception {
    	System.out.printf("sysNo: %s\n", board.getSysNo());
    	
    	//게시물 생성 / 수정
    	int data = boardService.postBoard(board); //성공:1, 실패:0

    	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>();
    }
    
    /* 게시물 상세 조회 */
    @PostMapping("/detail") 
    public ResponseDTO<Object> getBoardDetail(@RequestBody SearchDTO search) throws Exception {
    	List<Object> data = boardService.getBoardDetail(search);
    	System.out.println("board");
    	System.out.println(data.toString());
    	
    	//데이터 없을 때 에러 표시 해야 함
    	if(data.size() <= 0) {
    		System.out.println("Data Not Exist");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>(data);
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
//    	List<String> deleteList = (List<String>)requestData.get("deleteList");
    	
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


