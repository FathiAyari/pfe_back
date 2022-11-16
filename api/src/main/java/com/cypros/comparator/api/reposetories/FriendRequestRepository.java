package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {
  List<FriendRequest> getFriendRequestByReceiverIdOrderBySentAtDesc(Long id);
  Optional<FriendRequest> findByUserIdAndReceiverId(Long userId, Long receiverId);
}
