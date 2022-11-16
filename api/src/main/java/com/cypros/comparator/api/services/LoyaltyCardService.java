package com.cypros.comparator.api.services;

import com.cypros.comparator.api.entities.LoyaltyCard;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.reposetories.LoyaltyCardsRepository;
import com.cypros.comparator.api.reposetories.UserRepository;
import com.cypros.comparator.api.requests.LoyaltyCardsRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.LoyaltyCardResponse;
import com.cypros.comparator.api.enumerate.Results;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LoyaltyCardService {

  private final LoyaltyCardsRepository loyaltyCardsRepository;
  private final UserRepository userRepository;

  public LoyaltyCardService(
      LoyaltyCardsRepository loyaltyCardsRepository,
      UserRepository userRepository
  ) {
    this.loyaltyCardsRepository = loyaltyCardsRepository;
    this.userRepository = userRepository;
  }

  public List<LoyaltyCardResponse> getUserCards(Long id) {
    List<LoyaltyCard> loyaltyCards = userRepository.findById(id).get().getLoyaltyCards();
    return loyaltyCards.stream()
        .map(loyaltyCard -> {
          LoyaltyCardResponse loyaltyCardResponse = new LoyaltyCardResponse();
          loyaltyCardResponse.setName(loyaltyCard.getName());
          loyaltyCardResponse.setBarCode(loyaltyCard.getBarCode());
          loyaltyCardResponse.setStoreId(loyaltyCard.getStoreId());
          loyaltyCardResponse.setId(loyaltyCard.getId());
          return loyaltyCardResponse;
        }).toList();
  }

  public CrudResponse createCard(LoyaltyCardsRequest loyaltyCardsRequest) {
    Optional<User> user = userRepository.findById(loyaltyCardsRequest.getUserId());
    Optional<LoyaltyCard> loyaltyCard = loyaltyCardsRepository
        .findByBarCode(loyaltyCardsRequest.getBarCode());

    if (user.isPresent() && !loyaltyCard.isPresent()) {
      LoyaltyCard newLoyaltyCard = new LoyaltyCard();
      newLoyaltyCard.setBarCode(loyaltyCardsRequest.getBarCode());
      newLoyaltyCard.setName(loyaltyCardsRequest.getName());
      newLoyaltyCard.setStoreId(loyaltyCardsRequest.getStoreId());

      newLoyaltyCard.setUser(user.get());
      loyaltyCardsRepository.save(newLoyaltyCard);
      return new CrudResponse(Results.SUCCESSFULLY);
    }
    return new CrudResponse(Results.FAILED);
  }

  public CrudResponse deleteCard(Long id) {
    Optional<LoyaltyCard> loyaltyCard = loyaltyCardsRepository.findById(id);
    if (loyaltyCard.isPresent()) {
      loyaltyCardsRepository.deleteById(id);
      return new CrudResponse(Results.SUCCESSFULLY);
    }
    return new CrudResponse(Results.FAILED);
  }
}
