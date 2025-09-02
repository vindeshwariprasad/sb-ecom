package com.ecommerce.project.service;

import com.ecommerce.project.Exception.APIException;
import com.ecommerce.project.Exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponse getCategories(Integer pagenumber, Integer pagesize, String sortBy, String sortOrder) {
        Sort sortbyandorder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pagedetail = PageRequest.of(pagenumber,pagesize,sortbyandorder);
        Page<Category> categoryPage = categoryRepository.findAll(pagedetail);
        List<Category> categories= categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("Category size is zero");
        }
        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElement(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createcategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category saved = categoryRepository.findByCategoryName(category.getCategoryName());
        if (saved!=null){
            throw new APIException("Category with name already exit");
        }
        Category cat = categoryRepository.save(category);
        return modelMapper.map(cat,CategoryDTO.class);


    }
    @Override
    public CategoryDTO deletecategory(Long categoryId){
        Optional<Category> changecategory = categoryRepository.findById(categoryId);
        Category change = changecategory.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        categoryRepository.delete(change);
        return modelMapper.map(change, CategoryDTO.class);


    }
    @Override
    public CategoryDTO updatecategory(CategoryDTO categoryDTO, Long categoryId) {

        Optional<Category> changecategory = categoryRepository.findById(categoryId);
        changecategory.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category change;
        category.setCategoryId(categoryId);
        change = categoryRepository.save(category);
        return modelMapper.map(change,CategoryDTO.class);
    }


}
