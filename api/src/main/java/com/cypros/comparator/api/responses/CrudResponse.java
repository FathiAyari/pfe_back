package com.cypros.comparator.api.responses;

import com.cypros.comparator.api.enumerate.Results;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrudResponse {
  Results result;

}
