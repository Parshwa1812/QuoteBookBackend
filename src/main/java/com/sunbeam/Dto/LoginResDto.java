package com.sunbeam.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResDto {

    private String token;
    private Long id;
    private String fName;
    private String lName;
    private String email;
    private String phoneNumber;
	
}
