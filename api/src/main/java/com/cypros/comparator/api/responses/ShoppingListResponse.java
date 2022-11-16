package com.cypros.comparator.api.responses;

import com.cypros.comparator.api.dto.UserDto;
import com.cypros.comparator.api.entities.ShoppingListItem;
import com.cypros.comparator.api.enumerate.StatusType;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ShoppingListResponse {
  private Long id;
  private String name;
  private StatusType status;
  private Date creationDate;
  private UserDto owner;
  private UserDto sharedWith;
  private List<ShoppingListItemResponse> shoppingListItemResponses;
}
