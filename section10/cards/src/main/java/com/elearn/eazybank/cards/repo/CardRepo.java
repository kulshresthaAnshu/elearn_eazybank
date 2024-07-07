package com.elearn.eazybank.cards.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearn.eazybank.cards.entity.Card;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

	Optional<Card> findByMobileNumber(String mobileNumber);

	Optional<Card> findByCardNumber(String cardNumber);

}