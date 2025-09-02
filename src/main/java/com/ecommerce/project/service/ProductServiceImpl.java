package com.ecommerce.project.service;

import com.ecommerce.project.Exception.APIException;
import com.ecommerce.project.Exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.project.repositories.CartRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private  FileService fileService;
    @Value("${project.images}")
    private String path;
    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO product1) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));
        boolean isproductnot = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(product1.getProductName())) {
                isproductnot = false;
                break;
            }
        }
        if (isproductnot){
            Product product = modelMapper.map(product1, Product.class);
            product.setCategory(category);
            double specialPrice = product.getPrice()- ((product.getDiscount()*0.01)* product.getPrice());
            product.setImage("dafault.png");
            product.setSpecialPrice(specialPrice);
            Product saved = productRepository.save(product);
            return modelMapper.map(saved,ProductDTO.class);
        }else {
            throw new APIException("Product already exits");
        }


    }

    @Override
    public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortbyorder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortbyorder);
        Page<Product> pageproduct = productRepository.findAll(pageable);
        List<Product> products = pageproduct.getContent();
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageproduct.getNumber());
        productResponse.setPageSize(pageproduct.getSize());
        productResponse.setTotalElement(pageproduct.getTotalElements());
        productResponse.setTotalPage(pageproduct.getTotalPages());
        productResponse.setLastPage(pageproduct.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","CategoryId",categoryId));
        Sort sortbyorder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortbyorder);
        Page<Product> pageproduct = productRepository.findByCategoryOrderByPriceAsc(category, pageable);
        List<Product> products = pageproduct.getContent();
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        if (products.isEmpty()){
            throw new APIException("Product not found with keyword");
        }


        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageproduct.getNumber());
        productResponse.setPageSize(pageproduct.getSize());
        productResponse.setTotalElement(pageproduct.getTotalElements());
        productResponse.setTotalPage(pageproduct.getTotalPages());
        productResponse.setLastPage(pageproduct.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortbyorder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortbyorder);
        Page<Product> pageproduct = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%', pageable);
        List<Product> products = pageproduct.getContent();
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        if (products.isEmpty()){
            throw new APIException("Product not found with keyword");
        }


        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageproduct.getNumber());
        productResponse.setPageSize(pageproduct.getSize());
        productResponse.setTotalElement(pageproduct.getTotalElements());
        productResponse.setTotalPage(pageproduct.getTotalPages());
        productResponse.setLastPage(pageproduct.isLast());
        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO product2) {
        Product product = modelMapper.map(product2,Product.class);
        Product product1 = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Procduct","productId",productId));
        product1.setProductName(product.getProductName());
        product1.setDescription(product.getDescription());
        product1.setQuantity(product.getQuantity());
        product1.setPrice(product.getPrice());
        product1.setDiscount(product.getDiscount());
        product1.setSpecialPrice(product.getPrice()- ((product.getDiscount()*0.01)* product.getPrice()));
        Product product3 = productRepository.save(product1);
        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));
        return modelMapper.map(product3,ProductDTO.class);

    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product1 = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Procduct","productId",productId));
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.delete(product1);
        return modelMapper.map(product1,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImages(Long productId, MultipartFile image) throws IOException {
        Product product1 = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Procduct","productId",productId));
//        String path = "images/";
        String fileName = fileService.uploadImage(path, image);
        product1.setImage((fileName));
        Product update = productRepository.save(product1);
        return  modelMapper.map(update,ProductDTO.class);
    }


}
