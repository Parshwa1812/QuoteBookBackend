package com.sunbeam.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sunbeam.Dto.QuotesResDto;
import com.sunbeam.ExceptionHandling.QuoteException;
import com.sunbeam.ExceptionHandling.UserExceptions;
import com.sunbeam.Pojos.QuotePojo;
import com.sunbeam.Pojos.UserPojo;
import com.sunbeam.Repository.QuoteRepository;
import com.sunbeam.Repository.UserRepository;
import com.sunbeam.security.JWTService;

@Transactional
@Service
public class FavoriteQuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JWTService jwtService;

    public String likeQuote(String token, Long quoteId) {

        String email = jwtService.extractUserEmail(token);
        UserPojo userPojo = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions("User Not Found"));
        QuotePojo quotePojo = quoteRepository.findById(quoteId).orElseThrow(() -> new QuoteException("Quote Not Found"));

        if (quotePojo.getUserPojo().equals(userPojo)) {
            throw new QuoteException("Users cannot like their own quotes");
        }

        Optional<QuotePojo> checkIfAlreadyLiked = userPojo.getFavoriteQuote().stream()
                .filter(x -> x.getId().equals(quoteId)).findFirst();

        if (checkIfAlreadyLiked.isPresent()) {
            throw new QuoteException("Quote already liked");
        }

        quotePojo.setLikeCount(quotePojo.getLikeCount() + 1);
        userPojo.likeQuote(quotePojo);
        quoteRepository.save(quotePojo);
        userRepository.save(userPojo);

        return "Quote Liked Successfully";
    }

    public String unlikeQuote(String token, Long quoteId) {
        String email = jwtService.extractUserEmail(token);
        UserPojo userPojo = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions("User Not Found"));
        QuotePojo quotePojo = quoteRepository.findById(quoteId).orElseThrow(() -> new QuoteException("Quote Not Found"));

        if (!userPojo.getFavoriteQuote().contains(quotePojo)) {
            throw new QuoteException("Quote not liked by user");
        }

        quotePojo.setLikeCount(quotePojo.getLikeCount() - 1);
        userPojo.unlikeQuote(quotePojo);
        quoteRepository.save(quotePojo);
        userRepository.save(userPojo);

        return "Quote Unliked Successfully";
    }

    public List<QuotesResDto> getFavoriteQuotes(String token) {
        String email = jwtService.extractUserEmail(token);
        UserPojo userPojo = userRepository.findByEmail(email).orElseThrow(() -> new UserExceptions("User Not Found"));

        List<QuotePojo> quotePojos = userPojo.getFavoriteQuote();
        if (quotePojos != null && !quotePojos.isEmpty()) {
            return getFavoriteQuotesConverter(quotePojos);
        } else {
            return new ArrayList<>();
        }
    }

    private List<QuotesResDto> getFavoriteQuotesConverter(List<QuotePojo> quotePojos) {
        List<QuotesResDto> list = new ArrayList<>();

        for (QuotePojo quotePojo : quotePojos) {
            QuotesResDto quoteDto = new QuotesResDto();
            quoteDto.setId(quotePojo.getId());
            quoteDto.setQuoteText(quotePojo.getQuoteText());
            quoteDto.setDate(quotePojo.getDate());
            quoteDto.setAuthor(quotePojo.getAuthor());
            quoteDto.setLikeCount(quotePojo.getLikeCount());
            quoteDto.setFullname(quotePojo.getUserPojo().getFName() + " " + quotePojo.getUserPojo().getLName());
            quoteDto.setLiked(true);
            list.add(quoteDto);
        }
        return list;
    }
}
