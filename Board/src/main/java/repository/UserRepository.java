package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    /* 사용자 조회 */
	Optional<User> findById(String sysNo);
	Optional<User> findByEmail(String email);
	Optional<User> findByUserIdAndEmail(String id, String email); 

	@EntityGraph(attributePaths = "role")
	Optional<User> findWithRoleByUserId(String userId); //사용자 정보 + Role 정보 조회

	
    /* 중복 검사: 중복(true), 중복X(false) */
    boolean existsByUserId(String id);
    boolean existsByEmail(String email);
    boolean existsByEmailAndSysNoNot(String email, String sysNo);
}