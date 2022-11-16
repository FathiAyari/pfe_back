package com.cypros.comparator.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class SavedItemRequest {
private  String productId;
private  Long userId;
}
