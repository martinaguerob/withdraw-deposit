package com.nttdata.withdrawdeposit.controller;

import com.netflix.discovery.converters.Auto;
import com.nttdata.withdrawdeposit.entity.Deposit;
import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.service.DepositService;
import com.nttdata.withdrawdeposit.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
public class WithdrawController {

    @Autowired
    WithdrawService withdrawService;

    @Autowired
    DepositService depositService;

    @GetMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Withdraw>getWithdraw(){
        System.out.println("Listar cuentas bancarias");
        return withdrawService.findAll();
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Withdraw> saveWithdraw(@RequestBody Withdraw withdraw){
        return withdrawService.save(withdraw);
    }

    @GetMapping("/withdraw/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BankAccount>getWithdrawByBankAccount(@PathVariable String id){
        System.out.println("Listar retiros de id");
        return withdrawService.getAccount(id);
    }


    @GetMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Deposit>getDeposit(){
        System.out.println("Listar cuentas bancarias");
        return depositService.findAll();
    }

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Deposit> saveDeposit(@RequestBody Deposit deposit){
        return depositService.save(deposit);
    }

    @GetMapping("/deposit/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BankAccount>getDepositByBankAccount(@PathVariable String id){
        System.out.println("Listar retiros de id");
        return depositService.getAccount(id);
    }
}
