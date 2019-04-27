package com.example.springbootapp.entity;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Fim Monitoring profile entity
 */

@Data
@Table(name="Product")
public class Product {

    @PartitionKey
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String type;

    @Column
    private String category;

}
