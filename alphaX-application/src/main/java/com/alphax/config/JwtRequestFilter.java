package com.alphax.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alphax.common.rest.message.service.MessageService;
import com.alphax.common.exception.AlphaXException;
import com.alphax.common.exception.ExceptionMessages;
import com.alphax.service.mb.AS400UsersService;
import com.alphax.service.mb.impl.JwtUserDetailsServiceImpl;
import com.ibm.as400.access.AS400;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	AS400UsersService as400UsersService;
	
	@Autowired
	private MessageService messageService;
	
	private Claims claimsUsingToken;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				
				String computerName = jwtTokenUtil.getClintIPAddress(request);
				log.info("Client IP Address for each request: "+computerName + " And User Name :"+username);
				log.info("<------------------------> ");
				log.info("Get All object from Map :"+as400UsersService.getAll());
				log.info("<------------------------>");
				AS400 as400 = null;
				if(as400UsersService.getAll() != null && !as400UsersService.getAll().isEmpty() 
						&& as400UsersService.isUserAvailable(username+computerName)){
					as400 = (AS400) as400UsersService.getAs400users(username+computerName);
					log.info("Current object : {} "+as400);
				}
				if(as400!=null){
					as400UsersService.setAs400Object(as400);
				}else{
					AlphaXException exception = new AlphaXException(messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.AUTH_FAILED_MSG_KEY));
					log.error(messageService.getReadableMessage(ExceptionMessages.AUTH_FAILED_MSG_KEY), exception);
					throw exception;	
				}
				
			}catch(AlphaXException ex) {
				throw ex;
			}catch (ExpiredJwtException e) {
				log.info("JWT Token has expired");
				AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.TOKEN_EXPIRED_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.TOKEN_EXPIRED_MSG_KEY), exception);
				throw exception;
				
			} catch(Exception e){
				log.info("JWT Token Not Valid");
				AlphaXException exception = new AlphaXException(e, messageService.createApiMessage(LocaleContextHolder.getLocale(), ExceptionMessages.TOKEN_INVALID_MSG_KEY));
				log.error(messageService.getReadableMessage(ExceptionMessages.TOKEN_INVALID_MSG_KEY), exception);
				throw exception;
			}
		} else {
			log.warn("JWT Token does not begin with Bearer String");
			
		}

		//Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				claimsUsingToken = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
			}
		}
		chain.doFilter(request, response);
	}

	
	/**
	 * @return the claimsUsingToken
	 */
	public Claims getClaimsUsingToken() {
		return claimsUsingToken;
	}

}
