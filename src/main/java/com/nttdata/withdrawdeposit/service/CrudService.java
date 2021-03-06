package com.nttdata.withdrawdeposit.service;

import com.nttdata.withdrawdeposit.entity.Withdraw;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudService<T, ID> {
    Flux<T> findAll();
    Mono<T> save(T entity);
    Mono<T> update(T entity);
    Mono<T> deleteById(ID id);
}
