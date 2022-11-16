package com.cypros.comparator.api.services;

import com.cypros.comparator.api.entities.SavedItems;
import com.cypros.comparator.api.entities.ShoppingList;
import com.cypros.comparator.api.entities.ShoppingListItem;
import com.cypros.comparator.api.enumerate.Results;
import com.cypros.comparator.api.reposetories.ShoppingListItemRepository;
import com.cypros.comparator.api.reposetories.ShoppingListsRepository;
import com.cypros.comparator.api.requests.ShoppingListItemRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListItemServices {

  private final ShoppingListsRepository shoppingListsRepository;
  private final ShoppingListItemRepository shoppingListItemRepository;

  public ShoppingListItemServices(ShoppingListsRepository shoppingListsRepository,
      ShoppingListItemRepository shoppingListItemRepository) {
    this.shoppingListsRepository = shoppingListsRepository;
    this.shoppingListItemRepository = shoppingListItemRepository;
  }


  public CrudResponse addItem(ShoppingListItemRequest shoppingListItemRequest)
      throws ParseException {

    Optional<ShoppingList> shoppingList = shoppingListsRepository.findById(
        shoppingListItemRequest.getListId());
    List<String> productsId = shoppingList.get().getShoppingListItemList().stream()
        .map(ShoppingListItem::getProductId).toList();

    if (shoppingList.isPresent() && !productsId.contains(shoppingListItemRequest.getProductId())) {
      ShoppingListItem shoppingListItem = new ShoppingListItem();
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      shoppingListItem.setAddedAt(formatter.parse(formatter.format(new Date())));
      shoppingListItem.setProductId(shoppingListItemRequest.getProductId());
      shoppingListItem.setShoppingList(shoppingList.get());
      shoppingListItem.setQuantity(1);
      shoppingListItemRepository.save(shoppingListItem);
      return new CrudResponse(Results.SUCCESSFULLY);

    }
    return new CrudResponse(Results.FAILED);
  }

  public CrudResponse deleteItemFromList(Long listId, Long itemId) {
    Optional<ShoppingList> shoppingList = shoppingListsRepository.findById(listId);
    Optional<ShoppingListItem> shoppingListItem = shoppingListItemRepository.findByIdAndShoppingListId(itemId, listId);
    if (shoppingList.isPresent() && shoppingListItem.isPresent()) {

      shoppingListItemRepository.delete(shoppingListItem.get());

      return new CrudResponse(Results.SUCCESSFULLY);
    }
    return new CrudResponse(Results.FAILED);
  }

  public  CrudResponse setQuantity(Long itemId, Integer quantity) {
    Optional<ShoppingListItem> shoppingListItem = shoppingListItemRepository.findById(itemId);
    if (shoppingListItem.isPresent()) {

      shoppingListItem.get().setQuantity(quantity);
      shoppingListItemRepository.save(shoppingListItem.get());

      return new CrudResponse(Results.SUCCESSFULLY);
    }
    return new CrudResponse(Results.FAILED);
  }
}
