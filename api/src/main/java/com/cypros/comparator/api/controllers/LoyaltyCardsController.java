package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.requests.LoyaltyCardsRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.LoyaltyCardResponse;
import com.cypros.comparator.api.services.LoyaltyCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class LoyaltyCardsController {

  private final LoyaltyCardService loyaltyCardService;

  public LoyaltyCardsController(LoyaltyCardService loyaltyCardService) {
    this.loyaltyCardService = loyaltyCardService;
  }

  @GetMapping("/cards/{id}")
  public List<LoyaltyCardResponse> getUserCards(@PathVariable("id") Long id) {
    return loyaltyCardService.getUserCards(id);
  }

  @PostMapping("/create_card")
  @ResponseBody
  public CrudResponse createCard(@RequestBody LoyaltyCardsRequest loyaltyCardsRequest) {
    return loyaltyCardService.createCard(loyaltyCardsRequest);
  }

  @GetMapping("/delete_card/{id}")
  public CrudResponse deleteCard(@PathVariable Long id) {
    return loyaltyCardService.deleteCard(id);
  }

}
