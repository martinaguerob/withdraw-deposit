package com.nttdata.withdrawdeposit.controller;

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
public class TransactionController {

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
        System.out.println("Se guarda un retiro");
        return withdrawService.save(withdraw);
    }

    @GetMapping("/withdraw/account/data/{idBankAccount}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BankAccount>getBankAccount(@PathVariable String idBankAccount){
        System.out.println("Ver datos de la cuenta");
        return withdrawService.getAccount(idBankAccount);
    }

    @GetMapping("/withdraw/account/{idBankAccount}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Withdraw>getWithdrawByBankAccount(@PathVariable String idBankAccount){
        System.out.println("Ver retiros de la cuenta");
        return withdrawService.findByIdBankAccount(idBankAccount);
    }


    @GetMapping("/withdraw/count/{idBankAccount}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Long> getCountWithdraw(@PathVariable String idBankAccount){
        System.out.println("Contar número de retiros");
        return withdrawService.countWithdrawPerMonth(idBankAccount);
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
