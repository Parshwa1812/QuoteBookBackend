package com.sunbeam.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.Pojos.UserPojo;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<UserPojo, Long> {
	
	UserPojo findByEmailAndPassword(String email,String password);
	Optional<UserPojo> findByEmail(String email);

}
