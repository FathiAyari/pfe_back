package com.cypros.comparator.api.controllers;

import com.cypros.comparator.api.dto.UserDto;
import com.cypros.comparator.api.dto_convertor.UserConvertor;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.reposetories.UserRepository;
import com.cypros.comparator.api.requests.LoginRequest;
import com.cypros.comparator.api.requests.RegistrationRequest;
import com.cypros.comparator.api.responses.AuthenticationResponse;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.services.UserServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1")
public class UserController {

  private final UserServices userServices;
  private final UserRepository userRepository;

  public UserController(UserServices userServices,
      UserRepository userRepository) {
    this.userServices = userServices;
    this.userRepository = userRepository;
  }

  @PostMapping("/register")
  public AuthenticationResponse register(@RequestBody RegistrationRequest registrationRequest) {
    return userServices.registerService(registrationRequest);
  }

  @PostMapping("/login")
  public  AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
    return  userServices.loginService(loginRequest);
  }

  @GetMapping("/friends/{id}")
  public List<UserDto> getFriends(@PathVariable("id") Long id) {
    return userServices.getFriends(id);
  }

  @GetMapping("/delete_friend/{userId}/{friendId}")
  public CrudResponse deleteFriend(@PathVariable Long userId,@PathVariable Long friendId){
    return  userServices.deleteFriend(userId, friendId);
  }
  @GetMapping("/search/{name}")
  public List<UserDto> searchForFriend(@PathVariable String name){
    return  userServices.searchUser(name);
  }
}
