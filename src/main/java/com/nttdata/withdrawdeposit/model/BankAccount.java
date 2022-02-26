package com.nttdata.withdrawdeposit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    private String id;
    private String type; // Ahorro, corriente o fijo
    private String numberAccount; //NúmeroDeCuenta
    private String idCustomer; //Id del cliente
    private String codProfile; //Codigo de perfil del cliente
    private Double balance; //Saldo
    private Date date;
    private Boolean status;

}
