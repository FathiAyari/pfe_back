package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.reposetories.SavedItemsRepository;
import com.cypros.comparator.api.requests.SavedItemRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.SavedItemResponse;
import com.cypros.comparator.api.services.SavedItemsServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class SavedItemsControllers {
  private  final SavedItemsServices savedItemsServices;

  public SavedItemsControllers(SavedItemsServices savedItemsServices) {
    this.savedItemsServices = savedItemsServices;
  }
  @PostMapping(path = "/check")
  public CrudResponse checkItem(@RequestBody  SavedItemRequest savedItemRequest) throws ParseException {
    return savedItemsServices.checkProduct(savedItemRequest);
  }
  @GetMapping(path = "/saved_items/{user_id}")
  public List<SavedItemResponse> getSavedItems(@PathVariable Long user_id)  {
    return savedItemsServices.getSavedItems(user_id);
  }
}
