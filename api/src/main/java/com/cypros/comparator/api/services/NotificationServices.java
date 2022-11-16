package com.cypros.comparator.api.services;

import com.cypros.comparator.api.dto_convertor.UserConvertor;
import com.cypros.comparator.api.entities.Notifications;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.enumerate.Results;
import com.cypros.comparator.api.reposetories.NotificationRepository;
import com.cypros.comparator.api.reposetories.UserRepository;
import com.cypros.comparator.api.requests.NotificationRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.FriendRequestsResponse;
import com.cypros.comparator.api.responses.NotificationResponse;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServices {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  public NotificationServices(NotificationRepository notificationRepository,
      UserRepository userRepository) {
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
  }

  public CrudResponse sendNotificationToUser(NotificationRequest notificationRequest)
      throws ParseException {
    Optional<User> receiver = userRepository.findById(notificationRequest.getReceiverId());
    Optional<User> sender = userRepository.findById(notificationRequest.getSenderId());
    Notifications notification = new Notifications();
    notification.setMessage(notificationRequest.getMessage());
    notification.setType(notificationRequest.getType());
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    notification.setSentAt(formatter.parse(formatter.format(new Date())));
    notification.setReceiver(receiver.get());
    notification.setSender(sender.get());
    if (notification.getExternal_id() != null) {
      notification.setExternal_id(notification.getExternal_id());
    }
    notificationRepository.save(notification);
    return new CrudResponse(Results.SUCCESSFULLY);
  }

  public List<NotificationResponse> getNotifications(Long receiverId) {
    Optional<User> receiver = userRepository.findById(receiverId);
    List<Notifications> notifications = notificationRepository.findNotificationsByReceiverIdOrderBySentAtDesc(
        receiver.get().getId());
    return notifications.stream()
        .map(notif -> {
          NotificationResponse notification = new NotificationResponse();
         notification.setReceiver(UserConvertor.userToDto(notif.getReceiver()));
         notification.setSender(UserConvertor.userToDto(notif.getSender()));
         notification.setMessage(notif.getMessage());
         notification.setType(notif.getType());
          notification.setSentAt(notif.getSentAt());
          notification.setId(notif.getId());
          notification.setSeen(notif.isSeen());
          if(notif.getExternal_id() != null) {
            notification.setExternal_id(notif.getExternal_id());
          }
          return notification;
        }).toList();

  }

  public void updateNotificationState(Long notificationId) {
    Optional<Notifications> notification = notificationRepository.findById(notificationId);
 if(notification.isPresent()) {
   notification.get().setSeen(true);
   notificationRepository.save(notification.get());
  }
}}
