package com.nttdata.withdrawdeposit.service;
@FunctionalInterface
public interface Commission {

    Double Calcular(Double monto, Double tasa);
}
