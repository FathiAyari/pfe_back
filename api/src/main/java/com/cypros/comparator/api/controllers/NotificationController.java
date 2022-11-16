package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.entities.Notifications;
import com.cypros.comparator.api.requests.FriendRequestRequest;
import com.cypros.comparator.api.requests.NotificationRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.NotificationResponse;
import com.cypros.comparator.api.services.NotificationServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.List;

@RequestMapping(path = "api/v1")
@RestController
public class NotificationController {

  private final NotificationServices notificationServices;

  public NotificationController(NotificationServices notificationServices) {
    this.notificationServices = notificationServices;
  }

  @PostMapping("/send_notification")
  public CrudResponse sendFriendRequest(@RequestBody NotificationRequest notificationRequest)
      throws ParseException {
    return notificationServices.sendNotificationToUser(notificationRequest);
  }

  @GetMapping("/get_notifications/{receiverId}")
  public List<NotificationResponse> getNotifications(@PathVariable Long receiverId) {
    return notificationServices.getNotifications(receiverId);
  }
  @GetMapping("/seen/{notificationId}")
  public void updateNotificationState(@PathVariable Long notificationId) {
     notificationServices.updateNotificationState(notificationId);
  }

}
