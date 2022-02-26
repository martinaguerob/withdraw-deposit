package com.nttdata.withdrawdeposit.service.impl;

import com.nttdata.withdrawdeposit.config.WebClientConfig;
import com.nttdata.withdrawdeposit.entity.Deposit;
import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.model.MovementBankAccount;
import com.nttdata.withdrawdeposit.repository.DepositRepository;
import com.nttdata.withdrawdeposit.service.Calculate;
import com.nttdata.withdrawdeposit.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;

@Service
public class DepositServiceImpl implements DepositService {

    @Autowired
    DepositRepository depositRepository;

    private WebClientConfig webClientConfig = new WebClientConfig();

    @Override
    public Flux<Deposit> findAll() {
        return depositRepository.findAll();
    }

    @Override
    public Mono<Deposit> save(Deposit entity) {

        Mono<BankAccount> account = webClientConfig.getBankAccountById(entity.getIdBankAccount());
        return account.flatMap(ac -> {
            Calculate cal = (monto, saldo) ->saldo+monto;
            Double nuevoSaldo = cal.Calcular(entity.getAmount(), ac.getBalance());
            System.out.println(nuevoSaldo);
            ac.setBalance(nuevoSaldo);
            entity.setStatus(true);
            entity.setDate(new Date());
            webClientConfig.updateBankAccount(ac, ac.getId()).subscribe();
            this.saveMovementBankAccount(ac.getNumberAccount(), entity.getAmount()).subscribe();
            return depositRepository.save(entity);
        });
    }

    @Override
    public Mono<Deposit> update(Deposit entity) {
        return  depositRepository.findById(entity.getId())
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setAmount(entity.getAmount());
                    origin.setIdBankAccount(entity.getIdBankAccount());
                    origin.setStatus(entity.getStatus());
                    return depositRepository.save(origin);
                });
    }

    @Override
    public Mono<Deposit> deleteById(String id) {
        return  depositRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setStatus(false);
                    return depositRepository.save(origin);
                });
    }

    @Override
    public Mono<BankAccount> getAccount(String idBankAccount) {
        return webClientConfig.getBankAccountById(idBankAccount);
    }

    @Override
    public Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Double amount) {
        System.out.println("Se llegó a la función guardar movimiento");
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(amount);
        movementBankAccount.setDescription("Deposito a la cuenta");
        movementBankAccount.setNumberAccount(numberAccount);
        movementBankAccount.setStatus(true);
        return  webClientConfig.saveMovementBankAccount(movementBankAccount);
    }
}
