package com.gt.sokovia.repository;

import com.gt.common.data.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Pietro extends JpaRepository<OrderData, Long> {

    @Query(value = "SELECT COUNT(*) FROM ORDERS", nativeQuery = true)
    Integer getNumberOfOrdersInDatabase();

    Long removeById(Long id);

}
