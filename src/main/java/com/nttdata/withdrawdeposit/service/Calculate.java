package com.nttdata.withdrawdeposit.service;

@FunctionalInterface
public interface Calculate {

    Float Calcular(Float monto, Float saldo);
}
