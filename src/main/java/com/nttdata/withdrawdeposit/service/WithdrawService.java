package com.nttdata.withdrawdeposit.service;

import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import reactor.core.publisher.Mono;

public interface WithdrawService extends CrudService<Withdraw, String> {

    Mono<BankAccount> getAccount(String idBankAccount);
}
