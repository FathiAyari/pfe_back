package com.cypros.comparator.api.entities;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"creationDate"})
@NoArgsConstructor
@AllArgsConstructor
public class Price {

  LocalDateTime creationDate;
  Double amount;
}
