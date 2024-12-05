package controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import constant.ExceptionConstant;
import dto.BoardDTO;
import dto.ResponseDTO;
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
    @GetMapping("/list")
    public ResponseDTO<Object> getBoardList() throws Exception {
    	List<BoardDTO> data = boardService.getAllBoardList();
    	System.out.println("boardList");
    	System.out.println(data.toString());
    	return new ResponseDTO<>(data);
    }
    
    /* 게시물 생성 & 수정 
     * sysNo 없을 경우 신규 생성,
     * sysNo 있을 경우 수정*/
    @PostMapping ({"/post", "/post/{sysNo}"}) //post로 해서 create -> post로 바꾸고 :id있으면 수정으로 할까..?
    public ResponseDTO<Object> postBoard(
    		@PathVariable(value = "sysNo", required = false) String sysNo,
    		@RequestParam("title") String title,
    		@RequestParam("content") String content,
    		@RequestParam("userId") String userId,
    		@RequestParam("userName") String userName,
    		@RequestParam("imgPath") String imgPath
    		) throws Exception {
    	
    	sysNo = sysNo != null ? sysNo: "";
    	System.out.printf("sysNo: %s", sysNo);
    	
    	BoardDTO requestParam = new BoardDTO(sysNo, title, content, userId, userName, null, null, 0, imgPath);
    	int data = boardService.postBoard(requestParam); //성공:1, 실패:0
    	System.out.printf("createBoard: %d", data);

    	if(data == 0) { //게시물 생성 & 수정 실패했을 경우
    		System.out.println("Operation Error");
    		throw new GeneralException(ExceptionConstant.OPERATION.getCode(), ExceptionConstant.OPERATION.getMessage());
    	}else { //게시물 생성 & 수정 성공했을 경우 
    		
    	}
    	return new ResponseDTO<>(data);
    }
    
    /* 게시물 상세 조회 */
    @GetMapping("/detail") 
    public ResponseDTO<Object> getBoardDetail(@RequestParam("sysNo") String sysNo) throws Exception {
    	List<BoardDTO> data = boardService.getBoardDetail(sysNo);
    	System.out.println("board");
    	System.out.println(data.toString());
    	
    	//데이터 없을 때 에러 표시 해야 함
//  	if(boardList == null) {
//    		
//  	}
    	return new ResponseDTO<>(data);
    }
    
 // 게시물 삭제
//  @GetMapping("/detail")
//  public List<BoardDTO> getBoardDetail() throws Exception {
//      return boardService.getAllBoardList();
//  }
    
    // 회원가입
//  @GetMapping("/detail")
//  public List<BoardDTO> getBoardDetail() throws Exception {
//      return boardService.getAllBoardList();
//  }
    
 // 로그인
//  @GetMapping("/detail")
//  public List<BoardDTO> getBoardDetail() throws Exception {
//      return boardService.getAllBoardList();
//  }
    
 // 회원 정보 수정
//  @GetMapping("/detail")
//  public List<BoardDTO> getBoardDetail() throws Exception {
//      return boardService.getAllBoardList();
//  }
    
    // 회원 정보 조회
//  @GetMapping("/detail")
//  public List<BoardDTO> getBoardDetail() throws Exception {
//      return boardService.getAllBoardList();
//  }
    
 // 비밀번호 변경
//  @GetMapping("/detail")
//  public List<BoardDTO> getBoardDetail() throws Exception {
//      return boardService.getAllBoardList();
//  }
}


