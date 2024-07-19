package com.sunbeam.Services;

import java.io.IOException;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sunbeam.Dto.LoginReqDto;
import com.sunbeam.Dto.LoginResDto;
import com.sunbeam.Dto.RegisterUserDto;
import com.sunbeam.Dto.UserReqDto;
import com.sunbeam.Dto.UserResDto;
import com.sunbeam.Dto.UserResponseDto;
import com.sunbeam.ExceptionHandling.*;

import com.sunbeam.Pojos.Role;
import com.sunbeam.Pojos.UserPojo;
import com.sunbeam.Repository.UserRepository;
import com.sunbeam.security.JWTService;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ModelMapper mapper;

    public UserResponseDto addUser(RegisterUserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserExceptions("User with email " + userDto.getEmail() + " already exists");
        }

        UserPojo userPojo = new UserPojo();
        userPojo.setFName(userDto.getFName());
        userPojo.setLName(userDto.getLName());
        userPojo.setEmail(userDto.getEmail());
        userPojo.setPhoneNumber(userDto.getPhoneNumber());
        userPojo.setPassword(encoder.encode(userDto.getPassword()));
        userPojo.setRole(Role.CUSTOMER);

        userPojo = userRepository.save(userPojo);

        UserResponseDto user = new UserResponseDto();
        user.setEmail(userPojo.getEmail());
        user.setFName(userPojo.getFName());
        user.setLName(userPojo.getLName());
        user.setPhoneNumber(userPojo.getPhoneNumber());
        user.setRole(userPojo.getRole().toString());

        return user;
    }

    public LoginResDto getUser(LoginReqDto jwtRequest) {
        Authentication out = manager
                .authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));

        UserPojo userPojo = userRepository.findByEmail(jwtRequest.getEmail())
                .orElseThrow(() -> new UserExceptions("User not found"));

        LoginResDto jwtResponse = new LoginResDto();
        jwtResponse.setId(userPojo.getId());
        jwtResponse.setFName(userPojo.getFName());
        jwtResponse.setLName(userPojo.getLName());
        jwtResponse.setEmail(userPojo.getEmail());
        jwtResponse.setPhoneNumber(userPojo.getPhoneNumber());
        jwtResponse.setToken(jwtService.generateToken(userPojo));
        return jwtResponse;
    }

    public UserResDto updateUser(UserReqDto userdto, String token) {
        String email = jwtService.extractUserEmail(token);
        UserPojo userPojo = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("User not found"));

        userPojo.setFName(userdto.getFName());
        userPojo.setLName(userdto.getLName());
        userPojo.setEmail(userdto.getEmail());
        userPojo.setPhoneNumber(userdto.getPhoneNumber());
        userPojo.setPassword(encoder.encode(userdto.getPassword()));
        userRepository.save(userPojo);
       
        return mapper.map(userPojo, UserResDto.class);
    }

    public UserResDto getUserById(String token) {
        String email = jwtService.extractUserEmail(token);
        UserPojo pojo = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("User not found"));
        return mapper.map(pojo, UserResDto.class);
    }

    public String getUserByEmail(String email) {
        return userRepository.findByEmail(email).isPresent() ? "valid" : null;
    }

    public void uploadProfilePicture(String token, MultipartFile file) {
        String email = jwtService.extractUserEmail(token);
        UserPojo userPojo = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserExceptions("User not found"));

        try {
            userPojo.setProfilePicture(file.getBytes());
            userRepository.save(userPojo);
        } catch (IOException e) {
            throw new UserExceptions("Failed to upload profile picture");
        }
    }
}
