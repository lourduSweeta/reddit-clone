package com.example.redditclone.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.redditclone.dto.AuthenticationResponse;
import com.example.redditclone.dto.LoginRequest;
import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.User;
import com.example.redditclone.model.VerificationToken;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VerificationTokenRepository;
import com.example.redditclone.security.JwtProvider;
import com.example.redditclone.util.Constants;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	
	private final UserRepository userRepository;
	
	private final VerificationTokenRepository verificationTokenRepository;
	
	private final MailContentBuilder mailContentBuilder;
	
	private final MailService mailService;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtProvider jwtProvider;

	@Transactional
	public void signup(RegisterRequest registerRequest)
	{
		User user = new User();
		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setCreated(Instant.now());
		user.setEnabled(false);
		
		userRepository.save(user);
		
		String token = generateVerificationToken(user);
		String message = mailContentBuilder.build("Thank you for signing up to Spring Reddit Clone Application"
				+ "Please click below link to activate your account: "
				+ Constants.ACTIVATION_EMAIL+"/"+token);
		mailService.sendMail(new NotificationEmail("Please Activate your account",
				user.getEmail(), message));
	}
	

	private String generateVerificationToken(User user) {
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationTokenRepository.save(verificationToken);
		return token;
	}


	public void verifyAccount(String token) {

		Optional<VerificationToken> verificationToken =  verificationTokenRepository.findByToken(token);
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserandEnable(verificationToken.get());
	}


	@Transactional
	private void fetchUserandEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user =  userRepository.findByUsername(username).orElseThrow(()-> new SpringRedditException("User not found with the name - "+username));
		user.setEnabled(true);
		userRepository.save(user);
	}


	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
				loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate);
		return new AuthenticationResponse(token, loginRequest.getUsername());
	}
	
	@Transactional
	public User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User)
				SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUsername(principal.getUsername())
				.orElseThrow(() -> new SpringRedditException("User name not found -"+principal.getUsername()));
	}
}
