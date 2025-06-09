package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import entity.Likelog;

public interface LikeRepository extends JpaRepository<Likelog, String> {
    /* 좋아요 로그 삭제 */
	void deleteByBoardSysNoAndUserSysNo(String boardSysNo, String UserSysNo);
	
	/* 좋아요 삭제(다중) */
	@Modifying
    @Query("DELETE FROM Likelog t WHERE t.boardSysNo IN :sysNoList")
    void deleteLikelog(@Param("sysNoList") List<String> sysNoList);
}