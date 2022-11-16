package com.cypros.comparator.api.dto_convertor;

import com.cypros.comparator.api.dto.UserDto;
import com.cypros.comparator.api.entities.User;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserConvertor {

  public static UserDto userToDto(User user) {
    UserDto userDto = new UserDto();
    userDto.setName(user.getName());
    userDto.setId(user.getId());
    userDto.setLastName(user.getLastName());
    userDto.setPhoneNumber(user.getPhoneNumber());
    userDto.setEmail(user.getEmail());
    return userDto;
  }

  public User dtoToUser(UserDto userDto) {
    User user = new User();
    user.setName(userDto.getName());
    user.setLastName(userDto.getLastName());
    user.setEmail(userDto.getEmail());
    return user;
  }

}
