package com.sunbeam.Dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
public class AddQuoteResDto {
	private String quoteText;
	private String author;
	private String token;
	private LocalDateTime date;
}
