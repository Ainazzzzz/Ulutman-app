package com.ulutman.repository;

import com.ulutman.model.entities.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BankCard b WHERE b.bankName = :name AND b.cardNumber = :cardNumber")
    boolean existsByNameAndCardNumber(@Param("name") String name, @Param("cardNumber") String cardNumber);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM BankCard b WHERE b.bankName = :name")
    boolean existsByName(@Param("name") String name);
}
