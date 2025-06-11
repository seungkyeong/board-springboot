package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    /* 사용자 조회 */
	Optional<User> findById(String sysNo);
	Optional<User> findByEmail(String email);
	Optional<User> findByUserId(String id); //일반적인 사용자 정보 조회

	@EntityGraph(attributePaths = "role")
	Optional<User> findWithRoleByUserId(String userId); // Role 정보까지 함께 가져올 때만 사용하는 메서드

	
    /* 중복 검사: 중복(true), 중복X(false) */
    boolean existsByUserId(String id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndSysNoNot(String email, String sysNo);
}