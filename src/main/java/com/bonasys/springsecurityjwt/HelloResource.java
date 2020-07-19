package com.bonasys.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bonasys.springsecurityjwt.models.AuthenticationRequest;
import com.bonasys.springsecurityjwt.models.AuthenticationResponse;
import com.bonasys.springsecurityjwt.services.MyUserDetailsService;
import com.bonasys.springsecurityjwt.util.JwtUtil;

@RestController
public class HelloResource {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailService;
	
	@Autowired JwtUtil jwtUtil;

	@RequestMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
					);
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid username or password", e);
		}
		
		final UserDetails userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
