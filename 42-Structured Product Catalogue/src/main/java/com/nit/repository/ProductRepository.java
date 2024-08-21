package com.nit.repository;

import com.nit.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p " +
            "JOIN p.categories c " +
            "JOIN p.attributes a " +
            "WHERE (:name IS NULL OR p.name = :name) " +
            "AND (:categoryName IS NULL OR c.categoryName = :categoryName) " +
            "AND (:attributeKey IS NULL OR a.key = :attributeKey) " +
            "AND (:attributeValue IS NULL OR a.value = :attributeValue)")
    List<Product> findByFilters(@Param("name") String name,
                                @Param("categoryName") String categoryName,
                                @Param("attributeKey") String attributeKey,
                                @Param("attributeValue") String attributeValue);

    public Product findByName(String productName);
}
