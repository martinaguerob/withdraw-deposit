package com.nttdata.withdrawdeposit.config;

import com.nttdata.withdrawdeposit.model.BankAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    //@Bean
    //public WebClient webClient(){
       // return WebClient.builder().build();
    //}

     private final WebClient.Builder webClientBuilder = WebClient.builder();

    public Mono<BankAccount> getBankAccount(String idBankAccount){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8080/accounts/bank-account/"+idBankAccount)
                .retrieve()
                .bodyToMono(BankAccount.class);
    }

    public Mono<BankAccount> updateBankAccount(BankAccount bankAccount){
        System.out.println("Se llegó a updateBankAccount");
        return webClientBuilder.build()
                .put()
                .uri("http://localhost:8080/accounts/bank-account/update")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(bankAccount))
                .retrieve()
                .bodyToMono(BankAccount.class);
    }
}
