package com.example.commandeservice.IService;

import com.example.commandeservice.Entity.Product;
import org.springframework.stereotype.Service;

@Service
public interface IProductService {

     void addProduct(Product product, String userId);
      void addProducct(Product product) ;

}
