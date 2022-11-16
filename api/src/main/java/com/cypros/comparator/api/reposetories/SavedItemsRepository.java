package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.SavedItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedItemsRepository extends JpaRepository<SavedItems,Long> {
  SavedItems findByProductId( String itemId);

}
