package com.gt.repository;

import com.gt.model.data.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderData, Long> {

    @Query(value = "SELECT COUNT(*) FROM ORDERS", nativeQuery = true)
    Integer getNumberOfOrdersInDatabase();

    Long removeById(Long id);

}
