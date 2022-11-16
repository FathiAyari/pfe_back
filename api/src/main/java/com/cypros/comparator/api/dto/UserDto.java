package com.cypros.comparator.api.dto;

import com.cypros.comparator.api.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
private Long id ;
private String name ;
private String lastName;
private String phoneNumber;
private String email;
}
