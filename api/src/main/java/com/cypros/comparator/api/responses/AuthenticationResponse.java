package com.cypros.comparator.api.responses;

import com.cypros.comparator.api.dto.UserDto;
import com.cypros.comparator.api.enumerate.Results;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private UserDto user;
  private Results authenticationResult;
}
