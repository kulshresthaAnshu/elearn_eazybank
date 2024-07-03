package com.elearn.eazybank.cards.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elearn.eazybank.cards.dto.CardDTO;
import com.elearn.eazybank.cards.entity.Card;

@Component
public class CardMapper extends ModelMapper {
	@Autowired
	ModelMapper modelMapper;

	public CardDTO convertCardEntityToDTO(Card entity) {
		CardDTO cardDTO = new CardDTO();
		modelMapper.map(entity, cardDTO);
		return cardDTO;
	}

	public Card convertCardDTOtoEntity(CardDTO cardDTO) {
		Card entity = new Card();
		modelMapper.map(cardDTO, entity);
		return entity;
	}

	public Card convertCardDTOtoEntity(CardDTO cardDTO, Card entity) {
		modelMapper.map(cardDTO, entity);
		return entity;
	}
}
