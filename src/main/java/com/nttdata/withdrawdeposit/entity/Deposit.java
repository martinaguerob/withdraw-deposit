package com.nttdata.withdrawdeposit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "deposit")
public class Deposit {

    @Id
    private String id;
    private Float amount;
    private String idBankAccount;
    private Date date;
    private Boolean status;
}