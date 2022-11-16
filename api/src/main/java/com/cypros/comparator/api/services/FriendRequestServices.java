package com.cypros.comparator.api.services;

import com.cypros.comparator.api.dto_convertor.UserConvertor;
import com.cypros.comparator.api.entities.FriendRequest;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.enumerate.NotificationType;
import com.cypros.comparator.api.reposetories.FriendRequestRepository;
import com.cypros.comparator.api.reposetories.UserRepository;
import com.cypros.comparator.api.requests.FriendRequestRequest;
import com.cypros.comparator.api.requests.NotificationRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.FriendRequestsResponse;
import com.cypros.comparator.api.enumerate.Results;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestServices {

  private final FriendRequestRepository friendRequestRepository;
  private final UserRepository userRepository;
  private  final NotificationServices notificationServices;

  public FriendRequestServices(FriendRequestRepository friendRequestRepository,
      UserRepository userRepository,
      NotificationServices notificationServices) {
    this.friendRequestRepository = friendRequestRepository;
    this.userRepository = userRepository;
    this.notificationServices = notificationServices;
  }

  public List<FriendRequestsResponse> getFriendRequests(Long id) {
    Optional<User> receiver = userRepository.findById(id);
    List<FriendRequest> friendRequests = friendRequestRepository.getFriendRequestByReceiverIdOrderBySentAtDesc(
        receiver.get().getId());
    return friendRequests.stream()
        .map(friendRequest -> {
          FriendRequestsResponse friendRequestsResponse = new FriendRequestsResponse();
          friendRequestsResponse.setSentAt(friendRequest.getSentAt());
          friendRequestsResponse.setSender(UserConvertor.userToDto(friendRequest.getUser()));
          friendRequestsResponse.setReceiverId(friendRequest.getReceiver().getId());
          friendRequestsResponse.setId(friendRequest.getId());
          return friendRequestsResponse;
        }).toList();

  }

  public CrudResponse sendFriendRequest(FriendRequestRequest friendRequestRequest)
      throws ParseException {
    Optional<User> sender = userRepository.findById(friendRequestRequest.getUserId());
    Optional<User> receiver = userRepository.findById(friendRequestRequest.getReceiverId());
    if (sender.isPresent() && receiver.isPresent()) {
      boolean checkFriendRequest = friendRequestRepository.findByUserIdAndReceiverId(
          sender.get().getId(),
          receiver.get().getId()
      ).isPresent();

      if (checkFriendRequest) {
        return new CrudResponse(Results.FAILED);
      }

      FriendRequest friendRequest = new FriendRequest();

      friendRequest.setReceiver(receiver.get());
      friendRequest.setUser(sender.get());
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      friendRequest.setSentAt(formatter.parse(formatter.format(new Date())));
      friendRequestRepository.save(friendRequest);
      NotificationRequest notificationRequest = new NotificationRequest();
      notificationRequest.setReceiverId(receiver.get().getId());
      notificationRequest.setSenderId(sender.get().getId());
      notificationRequest.setType(NotificationType.FRIEND_REQUEST);
      notificationRequest.setMessage("You have received a friend request from " + sender.get().getName() + " " +sender.get().getLastName());
      return    notificationServices.sendNotificationToUser(notificationRequest);
    }
    return new CrudResponse(Results.FAILED);
  }

  public CrudResponse deleteFriendRequest(Long id) {
    Optional<FriendRequest> friendRequest=friendRequestRepository.findById(id);
    if(friendRequest.isPresent()){
      friendRequestRepository.deleteById(id);
      return new CrudResponse(Results.SUCCESSFULLY);
    }
    return new CrudResponse(Results.FAILED);
  }

  public CrudResponse acceptFriendRequest(FriendRequestRequest friendRequestRequest) {
    Optional<User> sender = userRepository.findById(friendRequestRequest.getUserId());
    Optional<User> receiver = userRepository.findById(friendRequestRequest.getReceiverId());
    Optional<FriendRequest> friendRequest = friendRequestRepository.findById(friendRequestRequest.getId());

    if (friendRequest.isPresent()) {
      if (sender.isPresent() && receiver.isPresent()) {
        List<User> senderFriends = sender.get().getFriends();
        senderFriends.add(receiver.get());
        sender.get().setFriends(senderFriends);

        List<User> receiverFriends = receiver.get().getFriends();
        receiverFriends.add(sender.get());
        receiver.get().setFriends(receiverFriends);

        userRepository.save(sender.get());
        userRepository.save(receiver.get());

        friendRequestRepository.deleteById(friendRequest.get().getId());
        return new CrudResponse(Results.SUCCESSFULLY);
      }
    }
    return new CrudResponse(Results.FAILED);
  }
}
