package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.requests.SavedItemRequest;
import com.cypros.comparator.api.requests.ShoppingListItemRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.services.ShoppingListItemServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;

@RestController
@RequestMapping(path = "api/v1")
public class ShoppingListItemController {
  private final ShoppingListItemServices listItemServices;

  public ShoppingListItemController(
      ShoppingListItemServices listItemServices) {
    this.listItemServices = listItemServices;
  }

  @PostMapping(path = "/insert_item")
public CrudResponse checkItem(@RequestBody ShoppingListItemRequest shoppingListItemRequest) throws ParseException {
    return listItemServices.addItem(shoppingListItemRequest);
}

  @GetMapping(path = "/delete_item/{list_id}/{item_id}")
  public CrudResponse deleteItemFromList(@PathVariable Long list_id,@PathVariable Long item_id) {
    return listItemServices.deleteItemFromList(list_id,item_id);
  }

  @GetMapping(path = "/update/{item_id}/{quantity}")
  public CrudResponse setQuantity (@PathVariable Long item_id,@PathVariable int quantity) {
    return listItemServices.setQuantity(item_id,quantity);
  }
}
