package com.sunbeam.Dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Component
public class QuotesResDto {
	
	private Long id;
	private String fullname;
	private String quoteText;
	private String author;
	private Long likeCount;
	private boolean isLiked;
	private LocalDateTime date;
}
