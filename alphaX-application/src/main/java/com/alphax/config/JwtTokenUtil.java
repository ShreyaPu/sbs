package com.alphax.config;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.vo.mb.UserPermissionsVO;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.common.constants.AlphaXTokenConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;
	
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.permission.secret}")
	private String permissionSecret;
	
	@Autowired
	private MessageService messageService;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails, List<String> userRoleList) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername() , userRoleList);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject, List<String> userRoleList) {
		
		claims.put(AlphaXTokenConstants.RETURN_CODE, userRoleList.get(0).trim());
		claims.put(AlphaXTokenConstants.DATALIB, userRoleList.get(1).trim());
		claims.put(AlphaXTokenConstants.SCHEMA, userRoleList.get(2).trim());
		claims.put(AlphaXTokenConstants.WSID, userRoleList.get(3).trim());
		claims.put(AlphaXTokenConstants.PODLIB, userRoleList.get(4).trim());
		claims.put(AlphaXTokenConstants.SAVLIB, userRoleList.get(5).trim());
		claims.put(AlphaXTokenConstants.NOSAVLIB, userRoleList.get(6).trim());
		claims.put(AlphaXTokenConstants.WSPRT, userRoleList.get(7).trim());
		claims.put(AlphaXTokenConstants.SYSPRT, userRoleList.get(8).trim());
		claims.put(AlphaXTokenConstants.PRTKURZ, userRoleList.get(9).trim());
		claims.put(AlphaXTokenConstants.LOGIN, userRoleList.get(10).trim());
		claims.put(AlphaXTokenConstants.USERID, userRoleList.get(11).trim());
		claims.put(AlphaXTokenConstants.USERNAME, userRoleList.get(12).trim());
		claims.put(AlphaXTokenConstants.MANDANT, userRoleList.get(13).trim());
		
		
		//claims.put("LoginUserName",subject);
		claims.put(AlphaXTokenConstants.PWDTOCHANGE, "0");
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	
	public String generatePermissions(UserPermissionsVO userPermissions) {
		Map<String, Object> permissionClaims = new HashMap<>();
		return doGeneratePermissions(permissionClaims, userPermissions );
	}
	
	private String doGeneratePermissions(Map<String, Object> permissionClaims, UserPermissionsVO userPermissions) {
		
		String subject = "userPermissions";
		permissionClaims.put("customerOne",userPermissions.isCustomerOne());
		
		return Jwts.builder().setClaims(permissionClaims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.signWith(SignatureAlgorithm.HS512, permissionSecret).compact();
	}

	
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String getClintIPAddress(HttpServletRequest request){
		String computerName="";
		try {
	        InetAddress inetAddress = InetAddress.getByName(request.getRemoteAddr());
	        computerName = inetAddress.getHostName();
	        
	        if (computerName.equalsIgnoreCase("localhost")) {
	            computerName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
	        } 
	    } catch (Exception e) {
	    	AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(),
	    			ExceptionMessages.CLIENT_IP_INVALID_MSG_KEY));
			log.error(messageService.getReadableMessage(ExceptionMessages.CLIENT_IP_INVALID_MSG_KEY), exception);
			throw exception;
	       }
		
		return computerName;
	}
	
	public String generateAuthorizationToken(Map<String, Object> authorizationKeyValue) {
		
		String subject = String.valueOf(authorizationKeyValue.get(AlphaXTokenConstants.LOGIN));
		return Jwts.builder().setClaims(authorizationKeyValue).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
