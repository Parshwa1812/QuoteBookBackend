package com.sunbeam.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.Pojos.QuotePojo;
import java.util.List;
import com.sunbeam.Pojos.UserPojo;


public interface QuoteRepository extends JpaRepository<QuotePojo, Long>{
	
	 QuotePojo findByUserPojoAndId(UserPojo userPojo, Long id);
	 List<QuotePojo> findByUserPojo(UserPojo userPojo);

}
