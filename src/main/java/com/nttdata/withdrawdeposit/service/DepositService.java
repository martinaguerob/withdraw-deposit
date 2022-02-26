package com.nttdata.withdrawdeposit.service;

import com.nttdata.withdrawdeposit.entity.Deposit;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.model.MovementBankAccount;
import reactor.core.publisher.Mono;

public interface DepositService extends CrudService<Deposit, String> {

    Mono<BankAccount> getAccount(String idBankAccount);

    //Movement
    Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Double amount);
}
