package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notifications,Long> {
  List<Notifications> findNotificationsByReceiverIdOrderBySentAtDesc(Long receiverId);

}
