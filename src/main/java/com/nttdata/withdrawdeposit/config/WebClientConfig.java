package com.nttdata.withdrawdeposit.config;

import com.nttdata.withdrawdeposit.model.BankAccount;
import com.nttdata.withdrawdeposit.model.MovementBankAccount;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    private final WebClient.Builder webClientBuilder = WebClient.builder();

    public Mono<BankAccount> getBankAccountById(String idBankAccount){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/accounts/bank-account/"+idBankAccount)
                .retrieve()
                .bodyToMono(BankAccount.class);
    }

    public Mono<BankAccount> updateBankAccount(BankAccount bankAccount, String id){
        System.out.println("Se llegó a updateBankAccount");
        return webClientBuilder.build()
                .put()
                .uri("http://localhost:8080/accounts/bank-account/update/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bankAccount))
                .retrieve()
                .bodyToMono(BankAccount.class);
    }

    public Mono<MovementBankAccount> saveMovementBankAccount(MovementBankAccount movementBankAccount){
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8080/movements/bank-account")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(movementBankAccount))
                .retrieve()
                .bodyToMono(MovementBankAccount.class);
    }
}
