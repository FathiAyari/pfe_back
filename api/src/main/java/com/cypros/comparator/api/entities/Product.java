package com.cypros.comparator.api.entities;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  private String id;

  private String name;
  private String brand;
  private String description;
  private String barCode;
  private String image;
  private String category;
  private String keywords;
  private List<Offer> offers;
  private List<ExtractedEntity> additional;
}