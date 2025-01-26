package dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import dto.BoardDTO;
import dto.SearchDTO;
import dto.UserDTO;

@Mapper 
public interface BoardDAO {
	/* Board */
    List<BoardDTO> getAllBoardList(SearchDTO requestParam) throws Exception; //게시판 목록 조회
    int getAllCountBoardList(SearchDTO requestParam) throws Exception; //게시판 목록 개수 조회
    int createBoard(BoardDTO requestParam) throws Exception; //게시물 생성
    int updateBoard(BoardDTO requestParam) throws Exception; //게시물 수정
    BoardDTO getBoardDetail(SearchDTO requestParam) throws Exception; //게시물 상세 조회
    int syncViewCount(String sysNo, Long viewCount) throws Exception; //게시물 조회수 업데이트
    
    /* User */
    int createUser(UserDTO requestParam) throws Exception; //회원가입
    int updateUser(UserDTO requestParam) throws Exception; //게시물 수정
    List<UserDTO> checkUser(Map<String, String> requestParam) throws Exception; //id 기반 존재 여부 확인
    int updateUserDetail(UserDTO requestParam) throws Exception; //사용자 상세 수정
    int updateUserPw(Map<String, String> requestParam) throws Exception; //비밀번호 수정
}


