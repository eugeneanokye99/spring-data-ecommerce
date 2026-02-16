package com.shopjoy.repository;

import com.shopjoy.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUser_Id(int userId);
    
    Optional<CartItem> findByUser_IdAndProduct_Id(int userId, int productId);
    
    void deleteByUser_Id(int userId);
}
