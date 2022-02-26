package com.nttdata.withdrawdeposit.service;

@FunctionalInterface
public interface Calculate {
    Double Calcular(Double monto, Double saldo);
}