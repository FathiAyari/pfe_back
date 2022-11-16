package com.cypros.comparator.api.requests;

import lombok.Data;

@Data
public class ShoppingListRequest {
  private String name;
  private Long ownerId;
}
