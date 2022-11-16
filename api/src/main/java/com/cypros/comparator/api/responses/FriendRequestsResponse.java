package com.cypros.comparator.api.responses;

import com.cypros.comparator.api.dto.UserDto;
import lombok.Data;
import java.util.Date;
@Data
public class FriendRequestsResponse {
  private Long id;
  private Long receiverId;
  private UserDto sender;
  private Date sentAt;

}
