package com.sunbeam.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sunbeam.Dto.LoginReqDto;
import com.sunbeam.Dto.LoginResDto;
import com.sunbeam.Dto.RegisterUserDto;
import com.sunbeam.Dto.UserResponseDto;
import com.sunbeam.Services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class CommonController {

	@Autowired
	private UserService userService;
	


	 @PostMapping("/register")
	    public ResponseEntity<?> addUser(@RequestBody RegisterUserDto userDto) {
		 UserResponseDto userResponse = userService.addUser(userDto);
	        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
	    }

	@PostMapping("/login")
	public ResponseEntity<LoginResDto> getUser(@RequestBody LoginReqDto jwtRequest) throws Exception {

		LoginResDto jwtResponse = userService.getUser(jwtRequest);
		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);

	}
	
	@PostMapping("/find-user-forgot-password/{email}")
	public ResponseEntity<String> findUserbyEmail(@PathVariable String email)
	{
		if(userService.getUserByEmail(email)!=null)
		return new ResponseEntity<>(HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
	}
	
	@PostMapping("/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            @RequestParam("token") String token) {
        try {
            // Pass the token and image file to the service
            userService.uploadProfilePicture(token, file);

            return ResponseEntity.ok("Profile picture uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Profile picture upload failed");
        }
    }

	

	

}
