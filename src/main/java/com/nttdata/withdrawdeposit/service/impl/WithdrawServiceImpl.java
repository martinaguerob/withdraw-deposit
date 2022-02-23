package com.nttdata.withdrawdeposit.service.impl;

import com.nttdata.withdrawdeposit.config.WebClientConfig;
import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.model.MovementBankAccount;
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
        Mono<BankAccount> account = webClientConfig.getBankAccountById(entity.getIdBankAccount());
        return account.flatMap(ac -> {
            Calculate cal = (monto, saldo) ->saldo-monto;
            Float nuevoSaldo = cal.Calcular(entity.getAmount(), ac.getBalance());
            System.out.println(nuevoSaldo);
            ac.setBalance(nuevoSaldo);
            entity.setStatus(true);
            entity.setDate(new Date());
            webClientConfig.updateBankAccount(ac, ac.getId()).subscribe();
            this.saveMovementBankAccount(ac.getNumberAccount(), entity.getAmount()).subscribe();
            return withdrawRepository.save(entity);
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
        return webClientConfig.getBankAccountById(idBankAccount);
    }

    @Override
    public Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Float amount) {
        System.out.println("Se llegó a la función guardar movimiento");
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(amount);
        movementBankAccount.setDescription("Retiro de la cuenta");
        movementBankAccount.setNumberAccount(numberAccount);
        movementBankAccount.setStatus(true);
        return  webClientConfig.saveMovementBankAccount(movementBankAccount);
    }
}
