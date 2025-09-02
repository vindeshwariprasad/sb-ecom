package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;


    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories( @RequestParam(name = "pageNumber", defaultValue = AppConstant.PageNumber, required = false) Integer pageNumber,
                                                           @RequestParam(name = "pageSize", defaultValue = AppConstant.PageSize, required = false) Integer pageSize, @RequestParam (name = "sortBy", defaultValue = AppConstant.SortBy, required = false) String sortBy,
                                                           @RequestParam (name = "sortOrder", defaultValue = AppConstant.SortOrder, required = false) String sortOrder

    ){
        CategoryResponse categoryResponse = categoryService.getCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }


    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createcategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedcategorydto = categoryService.createcategory(categoryDTO);
        return new ResponseEntity<>(savedcategorydto, HttpStatus.CREATED);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deletecategoty(@PathVariable Long categoryId){


            CategoryDTO status = categoryService.deletecategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);


    }
    @PutMapping("/public/categories/{categoryId}")
    public  ResponseEntity<CategoryDTO> updatecategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){

            CategoryDTO status = categoryService.updatecategory(categoryDTO, categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);


    }
}
