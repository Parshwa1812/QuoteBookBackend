package com.sunbeam.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTService {

	private static final String SECRET_KEY = "e06626bb4845257dc030da9051810f8d541e322b580a366e25d0d4bf4998aa868fdd2e5f5df2a5f46816aff7cae62d110335576a36370d2ce30c6f29917fb238836e36ac1b5277831b985f8173d23e00f4e67a13f00545ea43710782c1774957c17ae7a352aebd1c9cef04a03d6638546aafe841177426875767eee50780e979"
			+ "78a194cad60052b20b2ec09c6782da0298d88947d986bdce282fd3a7555d60d31509434fb55451f2250a224bb1b149142c7f7cf77613f68b39b77f795c2f11637ab3b7d8eacd6a862b891480773a72ada10a9dcf84650668247d976c8f60578daa6aaf7a093bb104e78d4e7b479be85a0c86200e73fa4f1b62114c285804f7d2"
			+ "0ca3f6b8762645c77edd80f2219caf2eee98a9b5b1030c9ccb3f0d56c956daf3a2d0309e286e597057feabb94dbd5f7260427eed88fa613ecfa12dbd26ad10af69bf78ebe0cbf56e47c0b421d90b5420624247355e21606c7c8f27d8049c7558a1f34c1a4743ae182e80e56baa7119cf2759919b795b6b74935a53c7cf430f5d"
			+ "a4cbeeca8381ea2518d4728a5175a72736103cfc7cadc77b09e55ddd9948defa7c51e9753d08722ebe845d9d07a8f7efd0eeaea56aad74461be2f0dd743e8bd83a2bc088df745e6aec7602e0c2f1734ee8af128dbd54aa76008ae007480bf798c09f69322b7d4a1fd8d6806b1ce385d7bba3b0341213b240f646583bddff5a64"
			+ "9521c498fb3e84b6bc3e80aba1a710e7da3174cce716c1a04646e5c46e11066d126134a2e98ccb4b63e5a8b9a80eac538588286979b7c7f366fe4be602c214bc1ca9cf728bb58a47ae86eaef5e63e54b00cc702d72d0abe0c96d13a291b090af48a69f226e9cb88521586e40863e6394e21989e90a61fde211293e17ddb62c59"
			+ "6cdf28dbe4d2a2af5b68859453862067a1033d10a2741eb1eb8d46b0fdd5c2cc9ea8e72ddaa311313ea68514c9737904144a7f6ee566f7210af5d1550114539c9704da6a162343a1da19abd5fec8fe6ef7ff70e9873266060dd21446ac84dd566af227c99ad764b8b9fd583f535607ac22c68391ee80f3ce09b38a22bf65ccf5"
			+ "b826873c09e09d7740fd9577692e469e1d8b281d74921de03b92623b6ebed718b33ee9b8f1fc1a61432213e89edadebcc8c45f48db0cb7a9b3698559b3ecca91275f895f3eae5f83088018448a8068b84bf26cc61e14761f658942e0bba7909656a2cdd57bc29aa2dfe4c9fba78af1ee19681625df94dd2f255cda3f2f8559ff"
			+ "9c25a95f9ff1ded90809b873341308b50351fbbd21fcd021cb6a4b917c29efb4de546cc18e4c50c35b2f5ee66eea1ccb8143d7da623ece70587d7934a3e5d2daa18c07a14caa73c2dd4d89daf72d20c0f9ff74fe857d29a5778c3abc01f19756e759a6fde6acd19151de9e6a26c7e0f115df385a9692a6328078b4dbdcbc604d"
			+ "0e57a13ea9de5504a3fdc85f1fecb3449f4f5dcf652b76473c7287b1d67692241331fd2c8dbd7596f1759d05fe9d472d13a91243da958fcc391e2a3cd09b29a3f5dbf4700dd10bb7daee1eedd04da8c66055a0ff0ed780d8f17f42487303a45b83cb1bd47d9efe94f7125b313fa54c3412c545befdfb697151f018222119c2a7"
			+ "6393cd1b5234f2d28e2d9051776768ff5bf7d4cc19b1060ddfeece6415e1888685c6ca8d63037ed0c44f444a4cc0631aba2cebcf09975b2c35ba317d5644e9ab55691ed31405c2614e33b5aeb1fad32fdc651b659f8dd94d78be92abcf836d1db7022e5460b63045fb4157bd1e05be0c9f56718ce3600d16eff89440b366d588"
			;
	public String extractUserEmail(String jwt) {
		return extractClaims(jwt,Claims::getSubject);
	}
	
	public <T> T extractClaims(String Token,Function<Claims, T> claimResolver)
	{
		final Claims claims =extractAllClaims(Token);
		return claimResolver.apply(claims);
	}
	
	public String generateToken(UserDetails userDetails)
	{
		return generateToken(new HashMap<>(),userDetails);
	}
	
	public boolean isTokenValid(String Token,UserDetails userDetails)
	{
		final String userName=extractUserEmail(Token);
		
		
		return (userName.equals(userDetails.getUsername() )&& !isTokenExpired(Token));
	}
	
	public String extractToken(String token)
	{
		return extractToken(token);
	}
	
	private boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}

	public String generateToken(Map<String,Object> extractClaims,UserDetails userDetails)
	{
		return Jwts.
				builder().
				setClaims(extractClaims).
				setSubject(userDetails.getUsername()).
				setIssuedAt(new Date(System.currentTimeMillis())).
				setExpiration(new Date(System.currentTimeMillis()+1000*60*24)).
				signWith(getSignKey(),SignatureAlgorithm.HS256).
				compact();
	}
	
	
	private Claims extractAllClaims(String token)
	{
		return Jwts.
				parserBuilder().
				setSigningKey(getSignKey()).
				build().parseClaimsJws(token)
				.getBody();
				
	}

	private Key getSignKey() {
	    byte[] keyBytes = DatatypeConverter.parseHexBinary(SECRET_KEY);
	    return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
	}

	
	

}
