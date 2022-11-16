package com.cypros.comparator.api.entities;

import com.cypros.comparator.api.enumerate.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shopping_list")
public class ShoppingList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "status", nullable = false)
  private StatusType status = StatusType.PENDING;
  private Date creationDate;
  @ManyToOne
  private User sharedWith;
  @ManyToOne
  private User owner;
  @OneToMany(mappedBy = "shoppingList" ,cascade = CascadeType.REMOVE)
  private List<ShoppingListItem> shoppingListItemList;


}
