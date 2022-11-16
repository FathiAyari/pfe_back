package com.cypros.comparator.api.services;

import com.cypros.comparator.api.entities.SavedItems;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.enumerate.Results;
import com.cypros.comparator.api.reposetories.ProductsRepository;
import com.cypros.comparator.api.reposetories.SavedItemsRepository;
import com.cypros.comparator.api.reposetories.UserRepository;
import com.cypros.comparator.api.requests.SavedItemRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.SavedItemResponse;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SavedItemsServices {

  private final SavedItemsRepository savedItemsRepository;
  private final UserRepository userRepository;
  private final ProductsRepository productsRepository;

  public SavedItemsServices(SavedItemsRepository savedItemsRepository,
      UserRepository userRepository,
      ProductsRepository productsRepository) {
    this.savedItemsRepository = savedItemsRepository;
    this.userRepository = userRepository;
    this.productsRepository = productsRepository;
  }

  public CrudResponse checkProduct(SavedItemRequest savedItemRequest) throws ParseException {
    Optional<User> user = userRepository.findById(savedItemRequest.getUserId());
    List<SavedItems> mySavedItems = user.get().getSavedItemsList();
   List<String> productsId= mySavedItems.stream().map(SavedItems::getProductId).toList();
    if (productsId.contains(savedItemRequest.getProductId())) {
      savedItemsRepository.deleteById(
          savedItemsRepository.findByProductId(savedItemRequest.getProductId()).getId());
      return new CrudResponse(Results.FAILED);
    }
    SavedItems savedItems = new SavedItems();
    savedItems.setUser(user.get());
    savedItems.setProductId(savedItemRequest.getProductId());
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    savedItems.setAddedAt(formatter.parse(formatter.format(new Date())));
    savedItemsRepository.save(savedItems);
    return new CrudResponse(Results.SUCCESSFULLY);

  }

  public List<SavedItemResponse> getSavedItems(Long user_id) {
    Optional<User> user = userRepository.findById(user_id);
    List<SavedItems> mySavedItems = user.get().getSavedItemsList();
    return mySavedItems.stream().map(savedItem -> {
      SavedItemResponse savedItemResponse = new SavedItemResponse();
      savedItemResponse.setAddedAt(savedItem.getAddedAt());
      savedItemResponse.setProduct(productsRepository.findById(savedItem.getProductId()).get());
      return savedItemResponse;
    }).toList();
  }
}
