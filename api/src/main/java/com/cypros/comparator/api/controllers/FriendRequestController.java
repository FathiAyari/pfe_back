package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.requests.FriendRequestRequest;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.responses.FriendRequestsResponse;
import com.cypros.comparator.api.services.FriendRequestServices;
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
public class FriendRequestController {


  final private FriendRequestServices friendRequestServices;

  public FriendRequestController(FriendRequestServices friendRequestServices) {
    this.friendRequestServices = friendRequestServices;
  }


  @GetMapping("/friend_requests/{id}")
  public List<FriendRequestsResponse> getUserCards(@PathVariable("id") Long id) {
    return friendRequestServices.getFriendRequests(id);
  }
  @PostMapping("/add_friend")
  public CrudResponse sendFriendRequest(@RequestBody
      FriendRequestRequest friendRequestRequest) throws ParseException {
    return friendRequestServices.sendFriendRequest(friendRequestRequest);
  }
  @GetMapping("/delete_request/{id}")
  public CrudResponse deleteCard(@PathVariable Long id) {
    return friendRequestServices.deleteFriendRequest(id);
  }

  @PostMapping("/accept_request")
  public CrudResponse acceptFriendRequest(@RequestBody
      FriendRequestRequest friendRequestRequest) {
    return friendRequestServices.acceptFriendRequest(friendRequestRequest);
  }
}
