package com.sunbeam.Controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.Dto.QuotesResDto;
import com.sunbeam.Services.FavoriteQuoteService;

@RestController
@RequestMapping("/app/favorite-quote")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteQuoteController {

    @Autowired
    private FavoriteQuoteService favoriteQuoteService;

    @GetMapping("/like-quote/{quoteId}")
    public ResponseEntity<?> likeQuote(@PathVariable Long quoteId, HttpServletRequest request) {
     
            	String authHeader = request.getHeader("Authorization");
                String token = authHeader.substring(7);
                String ret = favoriteQuoteService.likeQuote(token, quoteId);
                return ResponseEntity.status(HttpStatus.OK).body(ret);
           
    }

    @GetMapping("/unlike-quote/{quoteId}")
    public ResponseEntity<?> unlikeQuote(@PathVariable Long quoteId, HttpServletRequest request) {

    			String authHeader = request.getHeader("Authorization");
                String token = authHeader.substring(7);
                String ret = favoriteQuoteService.unlikeQuote(token, quoteId);
                return ResponseEntity.status(HttpStatus.OK).body(ret);
         
    }

    @GetMapping("/get-favorite-quotes")
    public ResponseEntity<?> getFavoriteQuotes(HttpServletRequest request) {
  
            	String authHeader = request.getHeader("Authorization");
                String token = authHeader.substring(7);
                List<QuotesResDto> quotesDto = favoriteQuoteService.getFavoriteQuotes(token);
                return ResponseEntity.status(HttpStatus.OK).body(quotesDto);
         
    }
}
