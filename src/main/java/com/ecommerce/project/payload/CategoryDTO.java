package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
}
