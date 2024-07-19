package com.sunbeam.Pojos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user_tb")
public class UserPojo implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull(message = "Field 'First Name' cannot be null.")
    @NotBlank(message = "Field 'First Name' cannot be blank.")
	private String fName;
	@NotNull(message = "Field 'Last Name' cannot be null.")
    @NotBlank(message = "Field 'Last Name' cannot be blank.")
	private String lName;
	
	@NotNull(message = "Field 'Email' cannot be null.")
    @NotBlank(message = "Field 'Email' cannot be blank.")
	@Column(unique = true)
	private String email;
	@NotNull(message = "Field 'Password' cannot be null.")
    @NotBlank(message = "Field 'Password' cannot be blank.")
	private String password;
	@Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    @Column(name = "phone_number", length = 10)
    private String phoneNumber;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Lob
    private byte[] profilePicture;

	@OneToMany(mappedBy = "userPojo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<QuotePojo> quotePojo = new ArrayList<>();


	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_favorite_quotes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "quote_id"))
	private List<QuotePojo> favoriteQuote = new ArrayList<>();
	
	
	public void addQuote(QuotePojo quotePojo)
	{
		quotePojo.setUserPojo(this);
		this.quotePojo.add(quotePojo);
	}
	
	public void removeQuote(QuotePojo quotePojo)
	{
		quotePojo.setUserPojo(null);
		this.quotePojo.remove(quotePojo);
	}
	
	public void likeQuote(QuotePojo quotePojo)
	{
		quotePojo.getUsersFavPojo().add(this);
		favoriteQuote.add(quotePojo);
	}
	
	public void unlikeQuote(QuotePojo quotePojo)
	{
		quotePojo.getUsersFavPojo().remove(this);
		favoriteQuote.remove(quotePojo);
	}

	//JWT SECURITY
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		
		return this.password;
	}

}
