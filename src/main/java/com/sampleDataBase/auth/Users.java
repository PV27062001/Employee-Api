package com.sampleDataBase.auth;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users
{
    @Id
    private int id;
    private String userName;
    private String password;
}
