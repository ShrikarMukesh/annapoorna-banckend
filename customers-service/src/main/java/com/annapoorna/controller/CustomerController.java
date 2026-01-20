package com.annapoorna.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.annapoorna.auth.AuthRequest;
import com.annapoorna.auth.AuthenticationResponse;
import com.annapoorna.auth.JwtService;
import com.annapoorna.dto.CustomerResponseDTO;
import com.annapoorna.entity.Customer;
import com.annapoorna.service.CustomerServiceImpl;

//import jakarta.validation.Valid;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

	@Autowired
	private JwtService jwtService;

	private final CustomerServiceImpl customerService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	public CustomerController(CustomerServiceImpl customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@PostMapping("/register")
	public ResponseEntity<CustomerResponseDTO> register(@Valid @RequestBody Customer customer) {
		return ResponseEntity.ok(customerService.register(customer));
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<Customer>> getAllCustomer(){
		return ResponseEntity.ok(customerService.getAllCustomer());
	}

	@GetMapping("/{email}")
	public ResponseEntity<Customer> findByEmail(@PathVariable String email) {
		return ResponseEntity.ok(customerService.findByEmail(email));
	}

	@PutMapping("/update")
	public ResponseEntity<Customer> update(@RequestBody Customer customer) {
		return ResponseEntity.ok(customerService.update(customer));
	}

	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

	@PostMapping("/authentication")
	public AuthenticationResponse authenticationAndGetToken(@RequestBody AuthRequest authRequest) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						authRequest.getUsername(),
						authRequest.getPassword())
				);
		var user = customerService.findByEmail(authRequest.getUsername());
		var token = jwtService.generateToken(user.getUserName());
		return AuthenticationResponse.builder().token(token).build();

		//		if (authentication.isAuthenticated()) {
		//			return jwtService.generateToken(authRequest.getUsername());
		//		} else {
		//			throw new UsernameNotFoundException("invalid user request !");
		//		}
	}
}
