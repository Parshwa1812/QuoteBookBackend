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
public class QuotesDto {
	private String quoteText;
	private String author;
	private Long likeCount;
	private boolean isLiked;
	private LocalDateTime date;
}
