package com.cypros.comparator.api.reposetories;

import com.cypros.comparator.api.entities.LoyaltyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LoyaltyCardsRepository extends JpaRepository<LoyaltyCard,Long> {
  List<LoyaltyCard> findLoyaltyCardByUserId(Long id);
  Optional<LoyaltyCard> findByBarCode(String barCode);

}
