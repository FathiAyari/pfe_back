package com.cypros.comparator.api.responses;

import lombok.Data;

@Data
public class LoyaltyCardResponse {
  private Long id ;
  private String name ;
  private String barCode;
  private int storeId;

}
