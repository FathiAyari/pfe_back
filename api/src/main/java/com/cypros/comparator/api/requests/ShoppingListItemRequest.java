package com.cypros.comparator.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ShoppingListItemRequest {
  private String productId;
  private Long listId;

}
