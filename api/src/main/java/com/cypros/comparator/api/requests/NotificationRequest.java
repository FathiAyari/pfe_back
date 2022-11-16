package com.cypros.comparator.api.requests;

import com.cypros.comparator.api.enumerate.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

  private Long receiverId;
  private Long senderId;
  private String message;
  private NotificationType type;
  private Long external_id;

}
