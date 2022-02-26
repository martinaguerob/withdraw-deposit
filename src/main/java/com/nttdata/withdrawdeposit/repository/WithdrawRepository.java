package com.nttdata.withdrawdeposit.repository;

import com.nttdata.withdrawdeposit.entity.Withdraw;
import lombok.With;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface WithdrawRepository extends ReactiveMongoRepository<Withdraw, String> {

    Flux<Withdraw> findByIdBankAccount (String idBankAccount);
}
