package repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, String> {
    /* notification 저장 */
	void deleteByBoardSysNoAndUserId(String boardSysNo, String UserSysNo);
	
	/* notification 목록 조회 */
	List<Notification> findByUserSysNoOrderByCreateDateDesc(String userSysNo);
	
	/* notification 조회 */
	Optional<Notification> findById(String sysNo);
}