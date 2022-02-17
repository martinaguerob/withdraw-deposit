package com.nttdata.withdrawdeposit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    private String id;
    private String type; // Ahorro, corriente o fijo
    private String numberAccount; //NúmeroDeCuenta
    private String idCustomer; //Id del cliente
    private Float balance; //Saldo
    private Boolean status;
}
