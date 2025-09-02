package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO product,
                                                 @PathVariable Long categoryId){
        ProductDTO productDTO = productService.addProduct(categoryId,product);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);

    }
//    @GetMapping("/public/products")
//    public ResponseEntity<ProductResponse> getAllProduct(@RequestParam(name = "pageNumber",defaultValue = AppConstant.PageNumber, required = false) Integer PageNumber,
//                                                         @RequestParam(name = "pageSize",defaultValue = AppConstant.PageSize, required = false) Integer PageSize,
//                                                         @RequestParam(name = "sortBy", defaultValue = AppConstant.SortByProduct, required = false) String sortBy,
//                                                         @RequestParam(name = "SortOrder",defaultValue = AppConstant.SortOrder, required = false) String sortOrder){
//        ProductResponse productResponse = productService.getAllProduct(PageNumber, PageSize, sortBy, sortOrder);
//        return new ResponseEntity<>(productResponse,HttpStatus.OK);
//    }
@GetMapping("/public/products")
public ResponseEntity<ProductResponse> getAllProduct(
        @RequestParam(name = "pageNumber", defaultValue = AppConstant.PageNumber, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstant.PageSize, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstant.SortByProduct, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstant.SortOrder, required = false) String sortOrder) {

    ProductResponse productResponse = productService.getAllProduct(pageNumber, pageSize, sortBy, sortOrder);
    return new ResponseEntity<>(productResponse, HttpStatus.OK);
}

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstant.PageNumber, required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstant.PageSize, required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstant.SortByProduct, required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstant.SortOrder, required = false) String sortOrder){
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsBykeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber", defaultValue = AppConstant.PageNumber, required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstant.PageSize, required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy", defaultValue = AppConstant.SortByProduct, required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder", defaultValue = AppConstant.SortOrder, required = false) String sortOrder){
        ProductResponse productResponse = productService.searchByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProductById(@Valid @RequestBody ProductDTO product, @PathVariable Long productId){
        ProductDTO productDTO = productService.updateProduct(productId,product);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);

    }
    @PutMapping("/products/{productId}/images")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO productDTO = productService.updateProductImages(productId, image);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
