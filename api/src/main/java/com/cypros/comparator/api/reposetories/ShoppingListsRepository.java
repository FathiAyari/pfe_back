package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShoppingListsRepository extends JpaRepository<ShoppingList, Long> {
Optional<ShoppingList> findById(Long id);

}
