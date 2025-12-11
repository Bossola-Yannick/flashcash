package com.orkblan.FlashCash.services;

import com.orkblan.FlashCash.domain.Account;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void addIban(Account account, User user){
        Account accountUser =  accountRepository.findAccountByUserid(user.getUser_id());
        accountUser.setIban(account.getIban());
        accountRepository.save(accountUser);
    }

    public void initialCount(User user){
        Account account = new Account();
        account.setUser(user);
        account.setSolde(0.0);
        accountRepository.save(account);
    }
}
