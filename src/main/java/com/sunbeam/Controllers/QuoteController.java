package com.sunbeam.Controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.Dto.AddQuoteReqDto;
import com.sunbeam.Dto.AddQuoteResDto;
import com.sunbeam.Dto.QuotesResDto;
import com.sunbeam.Dto.UpdateQuoteReqDto;
import com.sunbeam.Services.QuoteService;
import com.sunbeam.ExceptionHandling.UnauthorizedAccessException;

@RestController
@RequestMapping("/app/quote")
@CrossOrigin(origins = "http://localhost:3000")
public class QuoteController {

	@Autowired
	private QuoteService quoteService;

	@PostMapping("/add-quote")
	public ResponseEntity<?> addQuote(@RequestBody AddQuoteReqDto quoteDto, HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			AddQuoteResDto quotePojo = quoteService.addQuote(quoteDto, token);
			return ResponseEntity.status(HttpStatus.CREATED).body(quotePojo);
		} else {
			throw new UnauthorizedAccessException("Authorization header missing or invalid");
		}
	}

	@PutMapping("/update-quote/{quoteId}")
	public ResponseEntity<?> updateQuote(@RequestBody UpdateQuoteReqDto quoteDto, @PathVariable Long quoteId,
			HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			String value = quoteService.updateQuote(quoteDto, quoteId, token);
			return ResponseEntity.status(HttpStatus.OK).body(value);
		} else {
			throw new UnauthorizedAccessException("Authorization header missing or invalid");
		}
	}

	@DeleteMapping("/delete-quote/{quoteId}")
	public ResponseEntity<?> deleteQuote(@PathVariable Long quoteId, HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			String data = quoteService.deleteQuote(quoteId, token);
			return ResponseEntity.status(HttpStatus.OK).body(data);
		} else {
			throw new UnauthorizedAccessException("Authorization header missing or invalid");
		}
	}

	@GetMapping("/get-my-quotes")
	public ResponseEntity<?> getQuotesByUserId(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			List<QuotesResDto> data = quoteService.getQuotesByUserId(token);
			return ResponseEntity.status(HttpStatus.OK).body(data);
		} else {
			throw new UnauthorizedAccessException("Authorization header missing or invalid");
		}
	}

	@GetMapping("/get-all-quotes")
	public ResponseEntity<?> getAllQuotes(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			List<QuotesResDto> data = quoteService.getAllQuotes(token);
			return ResponseEntity.status(HttpStatus.OK).body(data);
		} else {
			throw new UnauthorizedAccessException("Authorization header missing or invalid");
		}
	}
}
