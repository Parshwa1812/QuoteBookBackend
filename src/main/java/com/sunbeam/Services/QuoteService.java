package com.sunbeam.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunbeam.Dto.AddQuoteReqDto;
import com.sunbeam.Dto.AddQuoteResDto;
import com.sunbeam.Dto.QuotesResDto;
import com.sunbeam.Dto.UpdateQuoteReqDto;
import com.sunbeam.ExceptionHandling.QuoteException;
import com.sunbeam.ExceptionHandling.UserExceptions;
import com.sunbeam.ExceptionHandling.UnauthorizedAccessException;
import com.sunbeam.Pojos.QuotePojo;
import com.sunbeam.Pojos.UserPojo;
import com.sunbeam.Repository.QuoteRepository;
import com.sunbeam.Repository.UserRepository;
import com.sunbeam.security.JWTService;

@Service
@Transactional
public class QuoteService {
	
	@Autowired
	private QuoteRepository quoteRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private JWTService jwtService;
	
	public AddQuoteResDto addQuote(AddQuoteReqDto quoteDto, String token) {
		String email = jwtService.extractUserEmail(token);
		UserPojo userPojo = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions("User Not Found"));
		
		QuotePojo quotePojo = new QuotePojo();
		quotePojo.setQuoteText(quoteDto.getQuoteText());
		quotePojo.setAuthor(quoteDto.getAuthor());
		quotePojo.setDate(LocalDateTime.now());
		quotePojo.setLikeCount(0L);
		quotePojo.setUserPojo(userPojo);

		quotePojo = quoteRepository.save(quotePojo);
		AddQuoteResDto res = modelMapper.map(quotePojo, AddQuoteResDto.class);
		res.setToken(token);
		return res;
	}
	
	public String updateQuote(UpdateQuoteReqDto quoteDto, Long quoteId, String token) {
		String email = jwtService.extractUserEmail(token);
		QuotePojo quotePojo = quoteRepository.findById(quoteId).orElseThrow(() -> new QuoteException("Quote Not Found"));
		
		if (!quotePojo.getUserPojo().getEmail().equals(email)) {
			throw new UnauthorizedAccessException("Unauthorized access to update the quote");
		}
		
		quotePojo.setQuoteText(quoteDto.getQuoteText());
		quotePojo.setAuthor(quoteDto.getAuthor());
		quotePojo.setDate(LocalDateTime.now());
		quoteRepository.save(quotePojo);
		return "Quote Updated Successfully.";
	}
	
	public String deleteQuote(Long quoteId, String token) {
		String email = jwtService.extractUserEmail(token);
		QuotePojo quotePojo = quoteRepository.findById(quoteId).orElseThrow(() -> new QuoteException("Quote Not Found"));
		
		if (!quotePojo.getUserPojo().getEmail().equals(email)) {
			throw new UnauthorizedAccessException("Unauthorized access to delete the quote");
		}
		
		quoteRepository.delete(quotePojo);
		return "Quote Deleted Successfully.";
	}
	
	public List<QuotesResDto> getQuotesByUserId(String token) {
		String email = jwtService.extractUserEmail(token);
		UserPojo userPojo = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions("User Not Found"));
		
		List<QuotePojo> quotePojos = quoteRepository.findByUserPojo(userPojo);
		return getQuotesByUserIdConverter(quotePojos);
	}
	
	public List<QuotesResDto> getAllQuotes(String token) {
		String email = jwtService.extractUserEmail(token);
		UserPojo userPojo = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions("User Not Found"));
		
		if(userPojo!=null)
		{
			
		List<QuotePojo> quotePojos = quoteRepository.findAll();
		return getQuotesByUserIdConverter(quotePojos);
		}else
			throw new UnauthorizedAccessException("Unauthorized access to get all quotes");
	}
	
	private List<QuotesResDto> getQuotesByUserIdConverter(List<QuotePojo> quotePojos) {
        List<QuotesResDto> quoteDtos = new ArrayList<>();

        for (QuotePojo quotePojo : quotePojos) {
            QuotesResDto quoteDto = new QuotesResDto();
            quoteDto.setId(quotePojo.getId());
            quoteDto.setQuoteText(quotePojo.getQuoteText());
            quoteDto.setDate(quotePojo.getDate());
            quoteDto.setAuthor(quotePojo.getAuthor());
            quoteDto.setLikeCount(quotePojo.getLikeCount());
            quoteDto.setFullname(quotePojo.getUserPojo().getFName() + " " + quotePojo.getUserPojo().getLName());
            quoteDtos.add(quoteDto);
        }	

        return quoteDtos;
    }
	
	private List<QuotesResDto> getQuotesByUserIdConverter(List<QuotePojo> quotePojos, List<QuotePojo> list) {
        List<QuotesResDto> quoteDtos = new ArrayList<>();

        for (QuotePojo quotePojo : quotePojos) {
            QuotesResDto quoteDto = new QuotesResDto();
            quoteDto.setId(quotePojo.getId());
            quoteDto.setQuoteText(quotePojo.getQuoteText());
            quoteDto.setDate(quotePojo.getDate());
            quoteDto.setAuthor(quotePojo.getAuthor());
            quoteDto.setLikeCount(quotePojo.getLikeCount());
            quoteDto.setFullname(quotePojo.getUserPojo().getFName() + " " + quotePojo.getUserPojo().getLName());
            quoteDto.setLiked(list.contains(quotePojo));
            quoteDtos.add(quoteDto);
        }	

        return quoteDtos;
    }
}
