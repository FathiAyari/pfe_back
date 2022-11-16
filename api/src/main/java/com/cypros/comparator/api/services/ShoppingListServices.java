package com.cypros.comparator.api.services;

import com.cypros.comparator.api.dto.UserDto;
import com.cypros.comparator.api.dto_convertor.UserConvertor;
import com.cypros.comparator.api.entities.ShoppingList;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.enumerate.NotificationType;
import com.cypros.comparator.api.enumerate.Results;
import com.cypros.comparator.api.reposetories.ProductsRepository;
import com.cypros.comparator.api.reposetories.ShoppingListsRepository;
import com.cypros.comparator.api.reposetories.UserRepository;
import com.cypros.comparator.api.requests.NotificationRequest;
import com.cypros.comparator.api.requests.ShoppingListRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.ShoppingListItemResponse;
import com.cypros.comparator.api.responses.ShoppingListResponse;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ShoppingListServices {

  private final UserRepository userRepository;
  private final ShoppingListsRepository shoppingListsRepository;
  private final ProductsRepository productsRepository;
  private final NotificationServices notificationServices;

  public ShoppingListServices(UserRepository userRepository,
      ShoppingListsRepository shoppingListsRepository, ProductsRepository productsRepository,
      NotificationServices notificationServices) {
    this.userRepository = userRepository;
    this.shoppingListsRepository = shoppingListsRepository;
    this.productsRepository = productsRepository;
    this.notificationServices = notificationServices;
  }

  public List<ShoppingListResponse> getShoppingLists(Long user_id) {
    Optional<User> user = userRepository.findById(user_id);
    List<ShoppingList> ownedLists = user.get().getOwnedShoppingLists();
    List<ShoppingList> sharedWithLists = user.get().getSharedWithShoppingLists();
    List<ShoppingList> shoppingLists = Stream.concat(ownedLists.stream(), sharedWithLists.stream())
        .toList();

    return shoppingLists.stream().map(shoppingList -> {
      ShoppingListResponse list = new ShoppingListResponse();
      list.setId(shoppingList.getId());
      list.setOwner(UserConvertor.userToDto(shoppingList.getOwner()));
      list.setName(shoppingList.getName());
      list.setStatus(shoppingList.getStatus());
      list.setCreationDate(shoppingList.getCreationDate());
      list.setShoppingListItemResponses(
          shoppingList.getShoppingListItemList().stream().map(shoppingListItem -> {
            ShoppingListItemResponse shoppingListItemResponse = new ShoppingListItemResponse();
            shoppingListItemResponse.setQuantity(shoppingListItem.getQuantity());
            shoppingListItemResponse.setAddedAt(shoppingListItem.getAddedAt());
            shoppingListItemResponse.setProduct(
                productsRepository.findById(shoppingListItem.getProductId()).get());
            return shoppingListItemResponse;
          }).toList());

      if (shoppingList.getSharedWith() != null) {
        list.setSharedWith(UserConvertor.userToDto(shoppingList.getSharedWith()));
      }

      return list;
    }).toList();
  }

  public CrudResponse createShoppingList(ShoppingListRequest shoppingListRequest)
      throws ParseException {
    Optional<User> owner = userRepository.findById(shoppingListRequest.getOwnerId());
    if (owner.isPresent()) {
      ShoppingList shoppingList = new ShoppingList();

      shoppingList.setName(shoppingListRequest.getName());
      SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
      shoppingList.setCreationDate(formatter.parse(formatter.format(new Date())));
      shoppingList.setOwner(owner.get());

      shoppingListsRepository.save(shoppingList);
      return new CrudResponse(Results.SUCCESSFULLY);
    }
    return new CrudResponse(Results.FAILED);
  }

  public CrudResponse deleteShoppingList(Long shoppingList_id) {
    Optional<ShoppingList> shoppingList = shoppingListsRepository.findById(shoppingList_id);
    if (shoppingList.isPresent()) {
      shoppingListsRepository.deleteById(shoppingList_id);
      return new CrudResponse(Results.SUCCESSFULLY);
    }
    return new CrudResponse(Results.FAILED);
  }

  public CrudResponse shareShoppingList(Long shoppingList_id, Long user_id, Long sender_id)
      throws ParseException {
    Optional<ShoppingList> shoppingList = shoppingListsRepository.findById(shoppingList_id);
    Optional<User> sender = userRepository.findById(sender_id);
    Optional<User> sharedWith = userRepository.findById(user_id);
    if (shoppingList.isPresent()) {
      shoppingList.get().setSharedWith(sharedWith.get());
      shoppingListsRepository.save(shoppingList.get());
      NotificationRequest notificationRequest = new NotificationRequest();
      notificationRequest.setReceiverId(user_id);
      notificationRequest.setSenderId(sender_id);
      notificationRequest.setType(NotificationType.Share_SHOPPING_LIST);
      notificationRequest.setMessage(
          "You have received a shopping list from " + sender.get().getName() + " " + sender.get()
              .getLastName());
      return notificationServices.sendNotificationToUser(notificationRequest);

    }
    return new CrudResponse(Results.FAILED);
  }
}
