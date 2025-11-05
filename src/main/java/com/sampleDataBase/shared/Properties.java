package com.sampleDataBase.shared;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class Properties {
    @Value("${secret.key}")
    private String secretKey;
}
