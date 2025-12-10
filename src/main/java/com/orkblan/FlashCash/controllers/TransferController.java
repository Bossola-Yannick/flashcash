package com.orkblan.FlashCash.controllers;

import com.orkblan.FlashCash.domain.Account;
import com.orkblan.FlashCash.domain.Transfer;
import com.orkblan.FlashCash.domain.User;
import com.orkblan.FlashCash.repositories.AccountRepository;
import com.orkblan.FlashCash.repositories.TransferRepository;
import com.orkblan.FlashCash.repositories.UserRepository;
import com.orkblan.FlashCash.services.TransferService;
import com.orkblan.FlashCash.services.UserConnected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
public class TransferController {

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private UserConnected userConnected;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransferService transferService;

    @RequestMapping("transfer/homeTransfer")
    public String homeTransfer(Model model, Authentication authentication) {
        User user = userConnected.userConnected(authentication);
        model.addAttribute("user", user);
        Account account = accountRepository.findAccountByUserid(user.getUser_id());
        model.addAttribute("account", account);
        List<Transfer> transfer = transferRepository.findTransferByAccount(account);
        model.addAttribute("transfers", transfer);
        return "transfer/homeTransfer";
    }

    // transfer de ma banque vers mon flashcash
    @RequestMapping("transfer/depositMoney")
    public String depositMoney(Model model) {
        model.addAttribute("account", new Account());
        return "transfer/depositMoney";
    }

    @RequestMapping("transfer/validatDeposit")
    public String validatDepositMoney(@Validated Account account, BindingResult result, Authentication authentication) {
        if (!result.hasErrors()) {
            User user = userConnected.userConnected(authentication);
            Transfer transfer = new Transfer();
            Account userAccount = accountRepository.findAccountByUserid(user.getUser_id());
            transferService.transferBankToFlashCash(user,account,userAccount, transfer);

            return "redirect:/user/profil";
        }
        return "transfer/depositMoney";
    }

    // transfer de mon flashcash vers ma banque
    @RequestMapping("transfer/outmoney")
    public String outMoney(Model model) {
        model.addAttribute("transfer", new Transfer());
        return "transfer/outmoney";
    }

    @RequestMapping("transfer/validatOutOFMoney")
    public String validateOutMoney(@Validated Transfer transfer, BindingResult result, Authentication authentication) {
        if (!result.hasErrors()) {
            User user = userConnected.userConnected(authentication);
            Account account = accountRepository.findAccountByUserid(user.getUser_id());
            transferService.transferFlashcashToBank(transfer,user,account);
            return "redirect:/user/profil";
        }
        return "transfer/outmoney";
    }


    // transfer de mon flashcash vers flashcash autre user
    @GetMapping("transfer/payment/{id}")
    public String payment(@PathVariable Integer id, Model model) {
        model.addAttribute("transfer", new Transfer());
        model.addAttribute("linkedUserId",id);
        return "transfer/payment";
    }

    @PostMapping ("transfer/payment/{id}")
        public String paymentToLink(@PathVariable("id") Integer id,@Validated Transfer transfer, BindingResult result, Authentication authentication){
        if(!result.hasErrors()){
            User user = userConnected.userConnected(authentication);
            Account accountUser = accountRepository.findAccountByUserid(user.getUser_id());
            User userLinked = userRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid Id User Linked"+ id));
            Account accountUserLinked = accountRepository.findAccountByUserid(userLinked.getUser_id());
            transferService.transferUserToUser(user,userLinked, accountUser, accountUserLinked, transfer);
            return "redirect:/user/profil";
        }
        return "transfer/payment";
        }
}