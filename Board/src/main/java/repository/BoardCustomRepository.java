package repository;

import org.springframework.data.domain.Page;
import dto.BoardDTO;
import dto.SearchDTO;
import entity.Board;

public interface BoardCustomRepository {
	/* 게시물 조회 */
	/* allList: 게시물 조회 
	 * viewList: 조회수 Top 게시물 조회
	 * likeList: 좋아요 Top 게시물 조회 */
	Page<Board> getBoard(SearchDTO searchDto);
	
	/* 게시물 상세 조회 */
	BoardDTO getBoardDetail(SearchDTO searchDto); 
}
