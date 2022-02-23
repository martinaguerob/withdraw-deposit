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
public class MovementBankAccount {

    private String id;
    private String description;
    private Float amount;
    private Date date;
    private Boolean status;
    private String numberAccount;

}
