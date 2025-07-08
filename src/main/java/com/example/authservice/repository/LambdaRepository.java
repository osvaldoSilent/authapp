package com.example.authservice.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "lambdaClient", url = "https://w15dtc2la0.execute-api.us-east-2.amazonaws.com")
public interface LambdaRepository {
    @GetMapping("/holaLamda")
    String getHolaDesdeLambda();
}
