package com.nttdata.withdrawdeposit.service.impl;

import com.nttdata.withdrawdeposit.config.WebClientConfig;
import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.repository.WithdrawRepository;
import com.nttdata.withdrawdeposit.service.Calculate;
import com.nttdata.withdrawdeposit.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    WithdrawRepository withdrawRepository;

    private WebClientConfig webClientConfig = new WebClientConfig();

    @Override
    public Flux<Withdraw> findAll() {
        return  withdrawRepository.findAll();
    }

    @Override
    public Mono<Withdraw> save(Withdraw entity) {
        entity.setStatus(true);
        entity.setDate(new Date());
        return withdrawRepository.save(entity).doOnSuccess(x -> {
            System.out.println("ingreso a do on sucess");
            x.setStatus(true);
            Calculate op = (monto, saldo) -> saldo-monto;
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
    public Mono<Withdraw> update(Withdraw entity) {
        return  withdrawRepository.findById(entity.getId())
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setAmount(entity.getAmount());
                    origin.setIdBankAccount(entity.getIdBankAccount());
                    origin.setStatus(entity.getStatus());
                    return withdrawRepository.save(origin);
                });
    }

    @Override
    public Mono<Withdraw> deleteById(String id) {
        return  withdrawRepository.findById(id)
                .switchIfEmpty(Mono.empty())
                .flatMap(origin -> {
                    origin.setStatus(false);
                    return withdrawRepository.save(origin);
                });
    }

    @Override
    public Mono<Withdraw> findById(String id) {
        return withdrawRepository.findById(id);
    }

    @Override
    public Mono<BankAccount> getAccount(String idBankAccount) {
        return webClientConfig.getBankAccount(idBankAccount);
    }
}
