package repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {
    /* 댓글 목록 조회 */
	List<Comment> findByBoardSysNoOrderByCreateDateDesc(String boardSysNo);
	/* 댓글 조회 */
	Optional<Comment> findById(String sysNo);
	
	/* 댓글 삭제(다중) */
	@Modifying
    @Query("DELETE FROM Comment t WHERE t.boardSysNo IN :sysNoList")
    void deleteComment(@Param("sysNoList") List<String> sysNoList);
}