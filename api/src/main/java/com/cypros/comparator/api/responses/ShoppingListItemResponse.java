package com.cypros.comparator.api.responses;

import com.cypros.comparator.api.entities.Product;
import lombok.Data;
import java.util.Date;

@Data
public class ShoppingListItemResponse {

  private Product product;
  private int quantity;
  private boolean status;
  private Date addedAt;
}
