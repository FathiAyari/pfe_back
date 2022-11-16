package com.cypros.comparator.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LoyaltyCardsRequest {
  private String barCode;
  private String name;
  private int storeId;
  private Long userId;


}
