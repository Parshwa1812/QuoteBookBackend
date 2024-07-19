package com.sunbeam.Controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.Dto.UserReqDto;
import com.sunbeam.Dto.UserResDto;
import com.sunbeam.Services.UserService;

@RestController
@RequestMapping("/app/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	@Autowired
	private UserService userService;

	@PutMapping("/update-user")
	public ResponseEntity<?> updateUser(@javax.validation.Valid @RequestBody UserReqDto userdto,
			HttpServletRequest request) {
		String token = request.getHeader("Authorization").substring(7);
		UserResDto data = userService.updateUser(userdto, token);
		return ResponseEntity.status(HttpStatus.OK).body(data);

	}

	@GetMapping("/get-my-user-details")
	public ResponseEntity<?> getUserById(HttpServletRequest request) {
		String token = request.getHeader("Authorization").substring(7);
		UserResDto data = userService.getUserById(token);
		return ResponseEntity.status(HttpStatus.OK).body(data);

	}
	
}
