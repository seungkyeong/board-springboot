package repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    /* 게시물 조회 */
	Optional<User> findById(String sysNo);
	Optional<User> findByEmail(String email);
	Optional<User> findByUserId(String id);
	
    /* 중복 검사: 중복(true), 중복X(false) */
    boolean existsByUserId(String id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndSysNoNot(String email, String sysNo);
}