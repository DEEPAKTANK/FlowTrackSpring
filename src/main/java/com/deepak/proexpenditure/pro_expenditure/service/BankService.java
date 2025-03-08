package com.deepak.proexpenditure.pro_expenditure.service;

import com.deepak.proexpenditure.pro_expenditure.dto.BankDTO;
import com.deepak.proexpenditure.pro_expenditure.entity.BankDetails;
import com.deepak.proexpenditure.pro_expenditure.entity.User;
import com.deepak.proexpenditure.pro_expenditure.exception.ResourceNotFoundException;
import com.deepak.proexpenditure.pro_expenditure.repository.BankRepository;
import com.deepak.proexpenditure.pro_expenditure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final UserRepository userRepository;

    // Create Bank Account
    public BankDTO createBankAccount(String userId, BankDTO bankDTO) {
        User user = userRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Active user not found"));

        BankDetails bankDetails = BankDetails.builder()
                .bankName(bankDTO.getBankName())
                .accountType(bankDTO.getAccountType())
                .ifscCode(bankDTO.getIfscCode())
                .branchName(bankDTO.getBranchName())
                .balance(bankDTO.getBalance())
                .user(user)
                .active(true) // Default active
                .build();

        BankDetails savedBank = bankRepository.save(bankDetails);
        return BankDTO.fromEntity(savedBank);
    }

    public List<BankDTO> getActiveBanks() {
        return bankRepository.findByActiveTrue();
    }

    public List<BankDTO> getInactiveBanks() {
        return bankRepository.findByActiveFalse();
    }



//    // Soft Delete Bank (Set Active to False)
    public void deactivateBank(String bankId) {
        BankDetails bank = bankRepository.findByBankIdAndActiveTrue(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Active bank not found"));
        bank.setActive(false);
        bankRepository.save(bank);
    }

    // Restore Deleted Bank (Set Active to True)
    public void restoreBank(String bankId) {
        BankDetails bank = bankRepository.findByBankIdAndActiveTrue(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found"));
        bank.setActive(true);
        bankRepository.save(bank);
    }

    public List<BankDTO> getBanksByUserId(String userId) {
        return bankRepository.findByUserUserIdAndActiveTrue(userId)
                .stream()
                .map(BankDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
