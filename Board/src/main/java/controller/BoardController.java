package controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import constant.ApiPathConstant;
import dto.BoardDTO;
import dto.CommentDTO;
import dto.LikeDTO;
import dto.ResponseDTO;
import dto.SearchDTO;
import lombok.RequiredArgsConstructor;
import service.BoardService;

@RestController
@RequestMapping(ApiPathConstant.API_ROOT) 
@RequiredArgsConstructor
public class BoardController { 
	private final BoardService boardService;

    /* 게시판 목록 조회 */
    @PostMapping(ApiPathConstant.BOARD.GET_LIST)
    public ResponseEntity<ResponseDTO<Object>> getBoardList(@RequestBody SearchDTO search) throws Exception {
    	List<Object> data = boardService.getAllBoardList(search);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 파일 업로드 */
    @PostMapping (ApiPathConstant.BOARD.UPLOAD_FILE) 
    public List<URL> uploadFile(@RequestParam("files") MultipartFile[] files) throws Exception {
    	List<URL> fileUrls = boardService.uploadFile(files); 

        return fileUrls; // 업로드된 파일들의 URL 반환
    }
    
    /* 업로드 파일 삭제 */
    @PostMapping (ApiPathConstant.BOARD.DELETE_FILE) 
    public ResponseDTO<Object> deleteFile(@RequestBody Map<String, List<String>> requestData) throws Exception {
    	List<String> keys = requestData.get("keys");
    	
    	return boardService.deleteFiles(keys); 
    }
    
    /* 게시물 생성, 수정 */ 
    @PostMapping (ApiPathConstant.BOARD.POST_BOARD) 
    public ResponseEntity<ResponseDTO<Object>> postBoard(@RequestBody BoardDTO board) throws Exception {
    	boardService.postBoard(board); 

    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 게시물 상세 조회 */
    @PostMapping(ApiPathConstant.BOARD.GET_DETAIL) 
    public ResponseEntity<ResponseDTO<Object>> getBoardDetail(@RequestBody SearchDTO search) throws Exception {
    	List<Object> data = boardService.getBoardDetail(search);
		
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(data));
    }
    
    /* 게시물 조회수 증가 */
    @PostMapping(ApiPathConstant.BOARD.UPDATE_VIEW) 
    public ResponseEntity<ResponseDTO<Object>> updateCount(@RequestBody Map<String, Object> request) throws Exception {
    	boardService.updateCount(request);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 게시물 좋아요 증가, 감소 */
    @PostMapping(ApiPathConstant.BOARD.UPLOAD_LIKE) 
    public ResponseEntity<ResponseDTO<Object>> updateLike(@RequestBody LikeDTO like) throws Exception {
    	boardService.updateLike(like);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 댓글 생성, 수정 */
    @PostMapping (ApiPathConstant.BOARD.POST_COMMENT) 
    public ResponseEntity<ResponseDTO<Object>> postComment(@RequestBody CommentDTO comment) throws Exception {
    	boardService.createComment(comment);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
     
    /* 게시물 삭제(다중) */
    @PostMapping(ApiPathConstant.BOARD.DELETE_BOARD)
    public ResponseEntity<ResponseDTO<Object>> deleteBoardList(@RequestBody Map<String, Object> request) throws Exception {
    	boardService.deleteBoardList(request);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
    
    /* 좋아요 삭제(다중) */
    @PostMapping(ApiPathConstant.BOARD.DELETE_LIKE)
    public ResponseEntity<ResponseDTO<Object>> deleteLikeList(@RequestBody Map<String, Object> request) throws Exception {
    	boardService.deleteLikeList(request);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>());
    }
}


