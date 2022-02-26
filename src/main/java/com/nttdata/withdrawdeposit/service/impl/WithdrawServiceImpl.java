package com.nttdata.withdrawdeposit.service.impl;

import com.nttdata.withdrawdeposit.config.WebClientConfig;
import com.nttdata.withdrawdeposit.entity.Withdraw;
import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.model.MovementBankAccount;
import com.nttdata.withdrawdeposit.repository.WithdrawRepository;
import com.nttdata.withdrawdeposit.service.Calculate;
import com.nttdata.withdrawdeposit.service.Commission;
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

    public Mono<Withdraw> functionX(Withdraw entity){
        Mono<Long> count = withdrawRepository.findByIdBankAccount(entity.getIdBankAccount()).count();
        return count.block() > 5 ? saveWithCommision(entity) : saveWithdraw(entity);
    }
    @Override
    public Mono<Withdraw> save(Withdraw entity) {
        entity.setStatus(true);
        entity.setDate(new Date());
        return withdrawRepository.save(entity);
    }
    public Mono<Withdraw> saveWithCommision(Withdraw entity){
        Mono<BankAccount> account = webClientConfig.getBankAccountById(entity.getIdBankAccount());
        return account
                .flatMap(ac -> {
                    //Calcular comision
                    Commission calCom = (monto, tasa) -> monto * tasa /100;
                    Double calTasa = this.calTasa(ac.getCodProfile());
                    Double commission = calCom.Calcular(entity.getAmount(), calTasa);

                   //Actualiza cuenta

                    Double newAmount = commission + entity.getAmount();
                    Calculate cal = (monto, saldo) ->saldo-monto;
                    Double newBalance = cal.Calcular(newAmount, ac.getBalance());
                    ac.setBalance(newBalance);
                    webClientConfig.updateBankAccount(ac, ac.getId()).subscribe();

                    //Guardar movimiento de retiro
                    String description = "Retiro de la cuenta";
                    this.saveMovementBankAccount(ac.getNumberAccount(), entity.getAmount(), description).subscribe();

                    //Guardar movimiento de comisión
                    String descriptionCommission = "Comisión por retiro";
                    this.saveMovementBankAccount(ac.getNumberAccount(), commission, descriptionCommission).subscribe();

                    return save(entity);
                });
    }

    public double calTasa(String codProfile){
        double tasa =  codProfile.equals("vip001") ? 3.15
                : codProfile.equals("pyme001") ? 3.25
                : codProfile.equals("personalb001") ? 15
                : 17.7;
        return tasa;
    }

    public Mono<Withdraw> saveWithdraw(Withdraw entity){
        Mono<BankAccount> account = webClientConfig.getBankAccountById(entity.getIdBankAccount());
        return account
                .flatMap(ac -> {
                    //Actualizar cuenta
                    Calculate cal = (monto, saldo) ->saldo-monto;
                    Double nuevoSaldo = cal.Calcular(entity.getAmount(), ac.getBalance());
                    ac.setBalance(nuevoSaldo);
                    webClientConfig.updateBankAccount(ac, ac.getId()).subscribe();

                    //Guardar movimiento
                    String description = "Retiro de la cuenta";
                    this.saveMovementBankAccount(ac.getNumberAccount(), entity.getAmount(), description).subscribe();

                    return save(entity);
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
    public Flux<Withdraw> findByIdBankAccount(String idBankAccount) {
        return withdrawRepository.findByIdBankAccount(idBankAccount);
    }

    @Override
    public Mono<Long> countWithdrawPerMonth(String idBankAccount) {
         return withdrawRepository.findByIdBankAccount(idBankAccount).count();
    }

    @Override
    public Mono<BankAccount> getAccount(String idBankAccount) {
        return webClientConfig.getBankAccountById(idBankAccount);
    }

    @Override
    public Mono<MovementBankAccount> saveMovementBankAccount(String numberAccount, Double amount, String description) {
        System.out.println("Se llegó a la función guardar movimiento");
        MovementBankAccount movementBankAccount = new MovementBankAccount();
        movementBankAccount.setAmount(amount);
        movementBankAccount.setDescription(description);
        movementBankAccount.setNumberAccount(numberAccount);
        movementBankAccount.setStatus(true);
        return  webClientConfig.saveMovementBankAccount(movementBankAccount);
    }
}
