package com.example.commandeservice.Controller;

import com.example.commandeservice.Entity.Product;
import com.example.commandeservice.IService.IProductService;
import com.example.commandeservice.model.ProductDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/product")
@RestController
public class ProductController {

    private IProductService productService;

    // Constructor-based dependency injection
    public ProductController(IProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/add")
    public void addProduct(@RequestBody Product productDTO, @RequestParam String userId) {
        productService.addProduct(productDTO, userId);
    }

    @PreAuthorize("hasRole('ROLE_admin')") // Utilisation correcte de 'ROLE_admin'
    @PostMapping("/addd")
    public void addProducct(@RequestBody Product productDTO) {
        productService.addProducct(productDTO);
    }











}
