package com.nttdata.withdrawdeposit.service.impl;

import com.nttdata.withdrawdeposit.config.WebClientConfig;
import com.nttdata.withdrawdeposit.entity.Deposit;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.repository.DepositRepository;
import com.nttdata.withdrawdeposit.service.Calculate;
import com.nttdata.withdrawdeposit.service.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        entity.setStatus(true);
        entity.setDate(new Date());
        return depositRepository.save(entity).doOnSuccess(x -> {
            System.out.println("ingreso a do on sucess");
            x.setStatus(true);
            Calculate op = (monto, saldo) -> saldo+monto;
            System.out.println("Aqui");
            webClientConfig.getBankAccount(x.getIdBankAccount())
                    .switchIfEmpty(Mono.empty())
                    .flatMap(p -> {
                        p.setBalance(op.Calcular(x.getAmount(),p.getBalance()));
                        System.out.println("se ingresó al subscribe");
                        return  webClientConfig.updateBankAccount(p);
                    });
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
    public Mono<Deposit> findById(String id) {
        return depositRepository.findById(id);
    }

    @Override
    public Mono<BankAccount> getAccount(String idBankAccount) {
        return webClientConfig.getBankAccount(idBankAccount);
    }
}
