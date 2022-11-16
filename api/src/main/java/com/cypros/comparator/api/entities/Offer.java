package com.cypros.comparator.api.entities;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

  private String link;
  @Indexed
  private String externalId;
  private int storeId;
  private Set<Price> priceList;

}