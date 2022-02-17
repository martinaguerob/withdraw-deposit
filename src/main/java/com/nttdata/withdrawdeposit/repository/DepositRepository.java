package com.nttdata.withdrawdeposit.repository;

import com.nttdata.withdrawdeposit.entity.Deposit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DepositRepository extends ReactiveMongoRepository<Deposit, String> {
}
