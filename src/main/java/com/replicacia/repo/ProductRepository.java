package com.replicacia.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.replicacia.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
