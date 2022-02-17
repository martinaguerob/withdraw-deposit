package com.nttdata.withdrawdeposit.repository;

import com.nttdata.withdrawdeposit.entity.Withdraw;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface WithdrawRepository extends ReactiveMongoRepository<Withdraw, String> {
}
