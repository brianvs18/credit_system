package com.bank.credit_system.usecase.handler;

import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditHandlerUseCase {
     private final CreditRepository creditRepository;

     public Flux<CreditDTO> findAll(){
         return creditRepository.findAll()
                 .map(creditDocument -> CreditDTO.builder()
                         .id(creditDocument.getId())
                         .creditValue(creditDocument.getCreditValue())
                         .accountId(creditDocument.getAccountId())
                         .creationDate(creditDocument.getCreationDate())
                         .build());
     }

     public Mono<CreditDTO> findById(String creditId){
         return creditRepository.findById(creditId)
                 .map(creditDocument -> CreditDTO.builder()
                         .id(creditDocument.getId())
                         .creditValue(creditDocument.getCreditValue())
                         .accountId(creditDocument.getAccountId())
                         .creationDate(creditDocument.getCreationDate())
                         .build());
     }
}
