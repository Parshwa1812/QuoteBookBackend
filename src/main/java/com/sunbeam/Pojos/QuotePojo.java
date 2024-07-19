package com.sunbeam.Pojos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="quote_table")
public class QuotePojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "Field 'Quote' cannot be null.")
    @NotBlank(message = "Field 'Quote' cannot be blank.")
	private String quoteText;
	@NotNull(message = "Field 'Author' cannot be null.")
    @NotBlank(message = "Field 'Author' cannot be blank.")
	private String author;
	private Long likeCount;
	private LocalDateTime date;
	
	
	@ManyToMany(mappedBy = "favoriteQuote")
	private Set<UserPojo> usersFavPojo = new HashSet<>();

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserPojo userPojo=new UserPojo();

}
