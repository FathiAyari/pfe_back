package com.cypros.comparator.api.requests;

import com.cypros.comparator.api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
public class FriendRequestRequest {
  private final Long id;
  private final Long receiverId;
  private final Long userId;
  private  final Date sentAt;


}
