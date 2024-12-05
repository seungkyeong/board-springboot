package dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import dto.BoardDTO;

@Mapper
public interface BoardDAO {
    List<BoardDTO> getAllBoardList() throws Exception; //게시판 목록 조회
    int createBoard(BoardDTO requestParam) throws Exception; //게시물 생성
    int updateBoard(BoardDTO requestParam) throws Exception; //게시물 수정
    List<BoardDTO> getBoardDetail(String sysNo) throws Exception; //게시물 상세 조회
}




