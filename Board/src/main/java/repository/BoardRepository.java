package repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.Board;

public interface BoardRepository extends JpaRepository<Board, String> {
    /* 게시물 조회 */
	Optional<Board> findById(String sysNo);
	
	/* 게시물 삭제(다중) */
	@Modifying
    @Query("DELETE FROM Board t WHERE t.sysNo IN :sysNoList")
    void deleteBoard(@Param("sysNoList") List<String> sysNoList);
}