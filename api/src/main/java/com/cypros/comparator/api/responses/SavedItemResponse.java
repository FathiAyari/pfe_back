package com.cypros.comparator.api.responses;

import com.cypros.comparator.api.entities.Product;
import lombok.Data;
import java.util.Date;
@Data
public class SavedItemResponse {
  private Product product;
  private  Date addedAt;

}
