package com.example.demo.user;

import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<FoodItem, Long> { }

