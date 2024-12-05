package service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dao.BoardDAO;
import dto.BoardDTO;

@Service
public class BoardService {
	@Autowired                     
	private final BoardDAO Boarddao;
	

    public BoardService(BoardDAO Boarddao) { 
        this.Boarddao = Boarddao;
    }

    /* 게시판 목록 조회 */ 
    public List<BoardDTO> getAllBoardList() throws Exception{
        return Boarddao.getAllBoardList();
    }
    
    /* 게시물 생성 & 수정 */ 
    public int postBoard(BoardDTO requestParam) throws Exception{
        if(requestParam.getSysNo() == "") { //신규 생성인 경우 
        	return Boarddao.createBoard(requestParam);
        }else { //수정인 경우 
        	return Boarddao.updateBoard(requestParam);
        }
        
    }
    
    /* 게시물 상세 조회 */ 
    public List<BoardDTO> getBoardDetail(String sysNo) throws Exception{
        return Boarddao.getBoardDetail(sysNo);
    }

}

