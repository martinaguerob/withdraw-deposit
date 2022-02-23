package com.nttdata.withdrawdeposit.service;

import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.model.MovementBankAccount;
import reactor.core.publisher.Mono;

public interface WithdrawService extends CrudService<Withdraw, String> {

    Mono<BankAccount> getAccount(String idBankAccount);

    //Movement
    Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Float amount);
}
