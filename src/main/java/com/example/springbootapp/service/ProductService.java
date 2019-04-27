package com.example.springbootapp.service;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.annotations.Table;
import com.example.springbootapp.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.datastax.driver.core.DataType.*;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private MappingManager mappingManager;

    private Mapper<Product> mapper;

    @Autowired
    private Session session;


    @PostConstruct
    public void init() {
        createTable(session);
        mapper = mappingManager.mapper(Product.class);
    }

    private void createTable(Session session) {
        session.execute(
                SchemaBuilder.createTable("Product")
                        .ifNotExists()
                        .addPartitionKey("id", uuid())
                        .addColumn("name", text())
                        .addColumn("description", text())
                        .addColumn("type", text())
                        .addColumn("category", text()));
    }

    public Product saveProduct(Product product) {
        mapper.save(product);
        return getProduct(product.getId());
    }

    public Product updateProduct(UUID id, Product product) {
        Product productInDb = getProduct(id);
        if(!StringUtils.isEmpty(product.getCategory()))
        productInDb.setCategory(product.getCategory());
        if(!StringUtils.isEmpty(product.getDescription()))
        productInDb.setDescription(product.getDescription());
        if (!StringUtils.isEmpty(product.getName()))
        productInDb.setName(product.getName());
        if (!StringUtils.isEmpty(product.getType()))
        productInDb.setType(product.getType());
        mapper.save(productInDb);
        return productInDb;
    }

    public Product getProduct(UUID id) {
        return mapper.get(id);
    }

    public void deleteProduct(UUID id) {
        try {
            mapper.delete(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
