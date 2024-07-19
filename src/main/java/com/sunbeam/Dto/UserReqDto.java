package com.sunbeam.Dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Component
public class UserReqDto {
	private Long id;
	private String fName;
	private String lName;
	private String email;
	private String password;
	private String phoneNumber;
}
