package com.savoryswift.orderservice.repository;

import com.savoryswift.orderservice.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserIdAndCheckedOutFalse(String userId);
}
