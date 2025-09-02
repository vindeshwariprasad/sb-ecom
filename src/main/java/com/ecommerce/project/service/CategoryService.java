package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
//    CategoryResponse getCategories(Integer pageNumber, Integer pageSize);

    CategoryResponse getCategories(Integer pagenumber, Integer pagesize, String sortBy, String sortOrder);

    CategoryDTO createcategory(CategoryDTO categoryDTO);


    CategoryDTO deletecategory(Long categoryId);

    CategoryDTO updatecategory(CategoryDTO categoryDTO, Long categoryId);
}
