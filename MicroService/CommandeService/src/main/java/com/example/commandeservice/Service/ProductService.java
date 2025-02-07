package com.example.commandeservice.Service;

import com.example.commandeservice.Entity.Product;
import com.example.commandeservice.IService.IProductService;
import com.example.commandeservice.Repo.ProductRepo;
import com.example.commandeservice.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService implements IProductService {
    private final ProductRepo productRepo;

    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(ProductRepo productRepo, RestTemplate restTemplate,SimpMessagingTemplate messagingTemplate) {
        this.productRepo = productRepo;
        this.restTemplate = restTemplate;
        this.messagingTemplate=messagingTemplate;
    }
    @Override
    public void addProduct(Product product, String userId) {
        if (isAdmin(userId)) {
            productRepo.save(product);
            System.out.println("eddddee");

            messagingTemplate.convertAndSend("/topic/notifications", "Product added: " );
            System.out.println("eeee");
        } else {
            throw new UnauthorizedException("User is not authorized to add products");
        }
    }

    private boolean isAdmin(String userId) {
        String url = "http://localhost:8088/users/" + userId + "/isadmin"; // Assurez-vous que l'URL est correcte
        Boolean isAdmin = restTemplate.getForObject(url, Boolean.class);
        return isAdmin != null && isAdmin;
    }
    @Override
    public void addProducct(Product product) {
        productRepo.save(product);
    }

}
