package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.model.dto.BankCardDTO;
import com.ulutman.model.dto.BankCardRequest;
import com.ulutman.model.entities.BankCard;
import com.ulutman.repository.BankCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankCardService {

    final BankCardRepository bankCardRepository;

    public BankCard createBankCard(BankCardRequest bankCardRequest) {
        String cardNumber = bankCardRequest.getCardNumber();
        String bankName = bankCardRequest.getBankName();


        if (cardNumber == null || bankName == null || !cardNumber.matches("\\d{16}")) {
            log.error("Invalid card number or bank name: {}", bankCardRequest);
            throw new IllegalArgumentException("Номер карты должен содержать ровно 16 цифр и банк не может быть пустым.");
        }

        if (bankCardRepository.existsByNameAndCardNumber(bankName,cardNumber)) {
            log.error("Bank with the name '{}' already exists.", bankName);
            throw new IllegalArgumentException("Банк с таким именем или номером уже существует.");
        }

        BankCard bankCard = new BankCard();
        bankCard.setCardNumber(cardNumber);
        bankCard.setBankName(bankName);

        BankCard savedBankCard = bankCardRepository.save(bankCard);
        log.info("Bank card created: {}", savedBankCard);
        return savedBankCard;
    }

    public List<BankCardDTO> getAllBankCards() {
        List<BankCard> bankCards = bankCardRepository.findAll();

        List<BankCardDTO> bankCardDTOs = bankCards.stream()
                .map(card -> new BankCardDTO(card.getBankName(),card.getBankName()))
                .collect(Collectors.toList());

        log.info("Retrieved all bank cards, count: {}", bankCardDTOs.size());
        return bankCardDTOs;
    }

    public BankCard getBankCardById(Long id) {
        Optional<BankCard> optionalBankCard = bankCardRepository.findById(id);

        if (optionalBankCard.isPresent()) {
            BankCard bankCard = optionalBankCard.get();
            log.info("Bank card retrieved: {}", bankCard);
            return bankCard;
        } else {
            log.warn("Bank card not found with id: {}", id);
            throw new NotFoundException("Bank card not found with id: " + id);
        }
    }

    public void deleteBankCardById(Long id) {
        if (!bankCardRepository.existsById(id)) { // Проверяем, существует ли карта перед удалением
            log.warn("Attempted to delete non-existing bank card with id: {}", id);
            throw new NotFoundException("Bank card not found with id: " + id);
        }

        bankCardRepository.deleteById(id);
        log.info("Bank card deleted with id: {}", id);
    }
}
