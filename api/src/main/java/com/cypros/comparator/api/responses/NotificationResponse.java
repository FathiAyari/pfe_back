package com.cypros.comparator.api.responses;

import com.cypros.comparator.api.dto.UserDto;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.enumerate.NotificationType;
import lombok.Data;
import java.util.Date;

@Data

public class NotificationResponse {
  private Long id;
  private UserDto receiver;
  private UserDto sender;
  private String message;
  private NotificationType type;
  private Long external_id;
  private boolean seen;
  private Date sentAt;

}