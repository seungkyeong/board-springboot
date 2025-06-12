package repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    /* Role 조회 */
	Optional<Role> findByRole(String role);
}