package com.sunbeam.Dto;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Component
public class AddQuoteReqDto {

	private String quoteText;
	private String author;


}