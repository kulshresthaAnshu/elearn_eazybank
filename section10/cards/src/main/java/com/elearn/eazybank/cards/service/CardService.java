package com.elearn.eazybank.cards.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearn.eazybank.cards.constants.CardConstant;
import com.elearn.eazybank.cards.dto.CardDTO;
import com.elearn.eazybank.cards.entity.Card;
import com.elearn.eazybank.cards.exception.CardAlreadyExistsException;
import com.elearn.eazybank.cards.exception.ResourceNotFoundException;
import com.elearn.eazybank.cards.mapper.CardMapper;
import com.elearn.eazybank.cards.repo.CardRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardService {
	@Autowired
	CardMapper cardMapper;

	@Autowired
	CardRepo cardRepo;

	/**
	 * @param mobileNumber - Mobile Number of the Customer
	 */
	public void createCard(String mobileNumber) {
		Optional<Card> optionalcard = cardRepo.findByMobileNumber(mobileNumber);
		if (optionalcard.isPresent()) {
			throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
		}
		cardRepo.save(createNewCard(mobileNumber));
	}

	/**
	 * @param mobileNumber - Mobile Number of the Customer
	 * @return the new card details
	 */
	private Card createNewCard(String mobileNumber) {
		Card newCard = new Card();
		long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
		newCard.setCardNumber(Long.toString(randomCardNumber));
		newCard.setMobileNumber(mobileNumber);
		newCard.setCardType(CardConstant.CREDIT_CARD);
		newCard.setTotalLimit(CardConstant.NEW_CARD_LIMIT);
		newCard.setAmountUsed(0);
		newCard.setAvailableAmount(CardConstant.NEW_CARD_LIMIT);
		return newCard;
	}

	/**
	 *
	 * @param mobileNumber - Input mobile Number
	 * @return Card Details based on a given mobileNumber
	 */
	public CardDTO fetchCard(String mobileNumber) {
		Card card = cardRepo.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
		return cardMapper.convertCardEntityToDTO(card);
	}

	/**
	 *
	 * @param cardDTO - cardDTO Object
	 * @return boolean indicating if the update of card details is successful or not
	 */
	public boolean updateCard(CardDTO cardDTO) {
		Card card = cardRepo.findByCardNumber(cardDTO.getCardNumber())
				.orElseThrow(() -> new ResourceNotFoundException("Card", "CardNumber", cardDTO.getCardNumber()));
		cardMapper.convertCardDTOtoEntity(cardDTO, card);
		cardRepo.save(card);
		return true;
	}

	/**
	 * @param mobileNumber - Input MobileNumber
	 * @return boolean indicating if the delete of card details is successful or not
	 */
	public boolean deleteCard(String mobileNumber) {
		Card card = cardRepo.findByMobileNumber(mobileNumber)
				.orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
		cardRepo.deleteById(card.getCardId());
		return true;
	}

}