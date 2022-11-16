package com.cypros.comparator.api.services;

import com.cypros.comparator.api.dto.UserDto;
import com.cypros.comparator.api.dto_convertor.UserConvertor;
import com.cypros.comparator.api.requests.LoginRequest;
import com.cypros.comparator.api.requests.RegistrationRequest;
import com.cypros.comparator.api.entities.User;
import com.cypros.comparator.api.reposetories.UserRepository;
import com.cypros.comparator.api.responses.CrudResponse;
import com.cypros.comparator.api.enumerate.Results;
import com.cypros.comparator.api.responses.AuthenticationResponse;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServices implements UserDetailsService {

  private final static String USER_NOT_FOUND_MSG = "user with email %s not found";


  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserServices(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }


  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
  }

  public AuthenticationResponse registerService(RegistrationRequest request) {
    boolean checkUser = this.userRepository.findByEmail(request.getEmail()).isPresent();

    if (checkUser) {
      AuthenticationResponse authenticationResponse = new AuthenticationResponse();
      authenticationResponse.setAuthenticationResult(Results.USEREXISTS);
      return authenticationResponse;
    }

    User user = new User();
    String encodedPassword;
    if (request.getPassword() != null) {
      encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
    } else {
      encodedPassword = request.getPassword();
    }
    user.setPassword(encodedPassword);
    user.setName(request.getName());
    user.setLastName(request.getLastName());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setEmail(request.getEmail());
    userRepository.save(user);

    return new AuthenticationResponse(UserConvertor.userToDto(user), Results.SUCCESSFULLY);
  }


  public AuthenticationResponse loginService(LoginRequest loginRequest) {
    Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
    if (user.isPresent() && bCryptPasswordEncoder.matches(loginRequest.getPassword(),
        user.get().getPassword())) {
      AuthenticationResponse authenticationResponse = new AuthenticationResponse();
      authenticationResponse.setAuthenticationResult(Results.SUCCESSFULLY);
      authenticationResponse.setUser(UserConvertor.userToDto(user.get()));
      return authenticationResponse;
    }
    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    authenticationResponse.setAuthenticationResult(Results.FAILED);
    return authenticationResponse;
  }

  public List<UserDto> getFriends(Long id) {
    Optional<User> user = userRepository.findById(id);
    return user.map(value -> value.getFriends().stream().map(UserConvertor::userToDto).toList())
        .orElse(null);
  }

  public CrudResponse deleteFriend(Long userId, Long friendId) {
    Optional<User> user = userRepository.findById(userId);
    Optional<User> friend = userRepository.findById(friendId);

    if (user.isPresent() && friend.isPresent()) {
      List<User> userFriends = user.get().getFriends();
      List<User> friendFriends = friend.get().getFriends();

      if (userFriends.contains(friend.get()) && friendFriends.contains(user.get())) {
        userFriends.remove(friend.get());
        friendFriends.remove(user.get());
        user.get().setFriends(userFriends);
        friend.get().setFriends(friendFriends);

        userRepository.save(user.get());
        userRepository.save(friend.get());

        return new CrudResponse(Results.SUCCESSFULLY);
      }
      return new CrudResponse(Results.FAILED);
    }
    return new CrudResponse(Results.FAILED);

  }

  public List<UserDto> searchUser(String name) {
    List<User> users =  userRepository.findByName(name);
    return users.stream().map(UserConvertor::userToDto).toList();
  }

}
