package controller;

import java.net.URL;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import constant.ExceptionConstant;
import dto.BoardDTO;
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
    	List<BoardDTO> data = boardService.getAllBoardList(search);
    	System.out.println(search);
    	System.out.printf("boardList: ");
    	System.out.println(data.toString());
    	return new ResponseDTO<>(data);
    }
    
    /* 파일 업로드 */
    @PostMapping ("/post/file") 
    public List<URL> uploadFile(@RequestParam("files") MultipartFile[] files) throws Exception {
    	//파일 업로드
    	List<URL> fileUrls = boardService.uploadFile(files); //성공:1, 실패:0

        // 업로드된 파일들의 URL 반환
        return fileUrls;
    }
    
    /* 게시물 생성 & 수정 
     * sysNo 없을 경우 신규 생성,
     * sysNo 있을 경우 수정*/
    @PostMapping ("/post") //post로 해서 create -> post로 바꾸고 :id있으면 수정으로 할까..?
    public ResponseDTO<Object> postBoard(@RequestBody BoardDTO board) throws Exception {
    	System.out.printf("sysNo: %s\n", board.getSysNo());
    	
    	//게시물 생성 / 수정
    	int data = boardService.postBoard(board); //성공:1, 실패:0

    	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}else { //게시물 생성 & 수정 성공했을 경우 
    		
    	}
    	return new ResponseDTO<>(data);
    }
    
    /* 게시물 상세 조회 */
    @PostMapping("/detail") 
    public ResponseDTO<Object> getBoardDetail(@RequestBody SearchDTO search) throws Exception {
    	BoardDTO data = boardService.getBoardDetail(search);
    	System.out.println("board");
    	System.out.println(data.toString());
    	
    	//데이터 없을 때 에러 표시 해야 함
    	if(data == null) {
    		System.out.println("Data Not Exist");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}
    	return new ResponseDTO<>(data);
    }
    
 // 게시물 삭제
//  @GetMapping("/detail")
//  public List<BoardDTO> getBoardDetail() throws Exception {
//      return boardService.getAllBoardList();
//  }
}


