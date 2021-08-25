package com.tweetapp.tweetapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * author Sairam
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

	private String secretkey = "tweetapp";

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader("Authorization");
		LOGGER.info("Auth Header: " + header);
		if (header == null || !header.startsWith("Bearer ")) {
			LOGGER.info("UnAuthenticated");
			chain.doFilter(req, res);
			return;
		}
		LOGGER.info("Authenticated");
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		LOGGER.info("Authenticated attribute", authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null) {
			Jws<Claims> jws;
			try {
				jws = Jwts.parser()
						.setSigningKey(secretkey)
						.parseClaimsJws(token.replace("Bearer ", ""));
				String userId = jws.getBody().getSubject();
				LOGGER.debug(userId);
				if (userId != null) {
					return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
				}
			} catch (JwtException ex) {
				return null;
			}
			return null;
		}
		return null;
	}
}
