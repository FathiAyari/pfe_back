package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.requests.ShoppingListRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.ShoppingListResponse;
import com.cypros.comparator.api.services.ShoppingListServices;
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
public class ShoppingListController {

  private final ShoppingListServices shoppingListServices;

  public ShoppingListController(ShoppingListServices shoppingListServices) {
    this.shoppingListServices = shoppingListServices;
  }

  @GetMapping(path = "/get_shopping_lists/{user_id}")
  public List<ShoppingListResponse> getShoppingLists(@PathVariable Long user_id) {
    return shoppingListServices.getShoppingLists(user_id);
  }

  @PostMapping(path = "/add_shopping_list")
  public CrudResponse createShoppingList(@RequestBody ShoppingListRequest shoppingListRequest)
      throws ParseException {
    System.out.println(shoppingListRequest);
    return shoppingListServices.createShoppingList(shoppingListRequest);
  }
  @GetMapping(path = "/delete_shopping_list/{shopping_list_id}")
  public CrudResponse deleteShoppingList(@PathVariable Long shopping_list_id) {
    return shoppingListServices.deleteShoppingList(shopping_list_id);
  }
  @GetMapping(path = "/share_shopping_list/{shopping_list_id}/{user_id}/{sender_id}")
  public CrudResponse shareShoppingList(@PathVariable Long shopping_list_id, @PathVariable Long user_id, @PathVariable Long sender_id)
      throws ParseException {
    return shoppingListServices.shareShoppingList(shopping_list_id, user_id,sender_id);
  }
}
