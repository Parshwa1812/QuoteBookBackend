package com.sunbeam.Dto;


import org.springframework.stereotype.Component;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@Component
public class UserResDto {

	private Long id;
	private String fName;
	private String lName;
	private String email;
	//private String password;
	private String phoneNumber;
	
   

}
