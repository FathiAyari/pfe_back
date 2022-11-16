package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem,Long> {
  Optional<ShoppingListItem>  findByIdAndShoppingListId(Long id,Long shoppingListId);

}
