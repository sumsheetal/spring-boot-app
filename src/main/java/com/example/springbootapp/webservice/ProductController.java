package com.example.springbootapp.webservice;

import com.example.springbootapp.entity.Product;
import com.example.springbootapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity createProduct(@RequestBody Product product){
        try {
            if (product == null)
                throw new Exception("Product to add cannnot be null");
            product.setId(UUID.randomUUID());
            Product productAdded = productService.saveProduct(product);
            return new ResponseEntity(productAdded, HttpStatus.OK);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PATCH)
    public ResponseEntity updateProduct(@PathVariable UUID id, @RequestBody Product incomingProduct){
        Product product =  productService.updateProduct(id, incomingProduct);
        return new ResponseEntity(product,HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
    public ResponseEntity getProduct(@PathVariable UUID id){
        return new ResponseEntity(productService.getProduct(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable UUID id){
        productService.deleteProduct(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
