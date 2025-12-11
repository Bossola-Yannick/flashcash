package com.orkblan.FlashCash.services;

import com.orkblan.FlashCash.domain.Account;
import com.orkblan.FlashCash.domain.Transfer;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.AccountRepository;
import com.orkblan.FlashCash.repositories.TransferRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public Double transferTaxe(Double amount){
        double valueTaxe;
        Double taxe =  amount * 5 / 100;
        valueTaxe = amount + taxe;
        return valueTaxe;
    }

    // transfer account flashcash User to account flashcash User linked
    public void transferUserToUser(User user,User userLinked, Account accountUser,Account accountUserLinked, Transfer transfer) {

        // make transfer for issuer
        Double amountWithTaxe = transferTaxe(transfer.getValue());
        Transfer transferIssuer = new Transfer();
        transferIssuer.setDate(new Date());
        transferIssuer.setRecipient(userLinked.getName());
        transferIssuer.setIssuer(user.getName());
        transferIssuer.setType("DEBIT");
        transferIssuer.setValue(amountWithTaxe);
        transferIssuer.setAccount(accountUser);
        accountUser.setSolde(accountUser.getSolde() - amountWithTaxe);
        transferRepository.save(transferIssuer);
        accountRepository.save(accountUser);

        // make transfer for recipient
        Transfer transferRecipient = new Transfer();
        transferRecipient.setDate(new Date());
        transferRecipient.setRecipient(userLinked.getName());
        transferRecipient.setIssuer(user.getName());
        transferRecipient.setType("CREDIT");
        transferRecipient.setValue(transfer.getValue());
        transferRecipient.setAccount(accountUserLinked);
        accountUserLinked.setSolde(accountUserLinked.getSolde() + transfer.getValue());
        transferRepository.save(transferRecipient);
        accountRepository.save(accountUserLinked);
    }

    // transfer from account flashcash to user bank
    public void transferFlashcashToBank(Transfer transfer, User user, Account account) {
        transfer.setDate(new Date());
        transfer.setRecipient(user.getName());
        transfer.setIssuer(user.getName());
        transfer.setType("DEBIT");
        transfer.setAccount(account);
        account.setSolde(account.getSolde() - transfer.getValue());
        transferRepository.save(transfer);
        accountRepository.save(account);
    }

    // transfer from user bank to account flashcash
    public void transferBankToFlashCash(User user, Account account,Account userAccount, Transfer transfer) {
        transfer.setDate(new Date());
        transfer.setIssuer(user.getName());
        transfer.setRecipient(user.getName());
        transfer.setType("CREDIT");
        transfer.setValue(account.getSolde());
        transfer.setAccount(userAccount);
        userAccount.setSolde(userAccount.getSolde() + account.getSolde());
        transferRepository.save(transfer);
        accountRepository.save(userAccount);
    }
}
