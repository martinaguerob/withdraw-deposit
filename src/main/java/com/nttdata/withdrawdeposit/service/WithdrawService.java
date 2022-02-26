package com.nttdata.withdrawdeposit.service;

import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.model.MovementBankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WithdrawService extends CrudService<Withdraw, String> {

    //Withdraw
    Flux<Withdraw> findByIdBankAccount (String idBankAccount);
    Mono<Long> countWithdrawPerMonth(String idBankAccount);

    //Account
    Mono<BankAccount> getAccount(String idBankAccount);

    //Movement
    Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Double amount, String description);
}
