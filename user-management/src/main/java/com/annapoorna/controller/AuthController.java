package com.annapoorna.controller;

import com.annapoorna.config.JwtProvider;
import com.annapoorna.model.Cart;
import com.annapoorna.model.USER_ROLE;
import com.annapoorna.model.User;
import com.annapoorna.repository.CartRepository;
import com.annapoorna.repository.UserRepository;
import com.annapoorna.request.LoginRequest;
import com.annapoorna.response.AuthResponse;
import com.annapoorna.service.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailService customerUserDetailService;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new Exception("Email already exist");
        }

        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setFullName(user.getFullName());
        createUser.setRole(user.getRole());
        createUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createUser);

        if (savedUser.getRole() == USER_ROLE.ROLE_CUSTOMER) {
            Cart cart = new Cart();
            cart.setCartId(savedUser.getUserId());
            cart.setCustomer(savedUser);
            cartRepository.save(cart);
        }

        UserDetails userDetails = customerUserDetailService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(),
                userDetails.getAuthorities());
        // Authentication authentication = new
        // UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setRole(savedUser.getRole());
        authResponse.setUserId(savedUser.getUserId());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) throws Exception {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        String jwt = jwtProvider.generateToken(authentication);

        User user = userRepository.findByEmail(username);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        authResponse.setRole(USER_ROLE.valueOf(role));
        authResponse.setUserId(user.getUserId());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customerUserDetailService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username...");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password...");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Autowired
    private com.annapoorna.repository.VerificationCodeRepository verificationCodeRepository;

    @PostMapping("/otp/send")
    public ResponseEntity<String> sendOtp(@RequestBody LoginRequest request) throws Exception {
        String phoneNumber = request.getPhoneNumber();
        if (phoneNumber == null) {
            throw new Exception("Phone number is required");
        }

        String otp = com.annapoorna.utils.OtpUtil.generateOtp();
        System.out.println("OTP for " + phoneNumber + ": " + otp); // For testing

        com.annapoorna.model.VerificationCode verificationCode = verificationCodeRepository
                .findByPhoneNumber(phoneNumber);
        if (verificationCode == null) {
            verificationCode = new com.annapoorna.model.VerificationCode();
            verificationCode.setPhoneNumber(phoneNumber);
        }
        verificationCode.setOtp(otp);
        verificationCode.setExpiryDate(java.time.LocalDateTime.now().plusMinutes(5));
        verificationCodeRepository.save(verificationCode);

        return new ResponseEntity<>("OTP sent successfully", HttpStatus.OK);
    }

    @PostMapping("/otp/login")
    public ResponseEntity<AuthResponse> loginWithOtp(@RequestBody LoginRequest request) throws Exception {
        String phoneNumber = request.getPhoneNumber();
        String otp = request.getOtp();

        if (phoneNumber == null || otp == null) {
            throw new Exception("Phone number and OTP are required");
        }

        com.annapoorna.model.VerificationCode verificationCode = verificationCodeRepository
                .findByPhoneNumber(phoneNumber);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Invalid OTP");
        }

        if (verificationCode.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new Exception("OTP expired");
        }

        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setRole(USER_ROLE.ROLE_CUSTOMER);
            user.setFullName("User " + phoneNumber); // Default name
            user.setPassword(passwordEncoder.encode(otp)); // Temporary password or handle passwordless
            user = userRepository.save(user);

            if (user.getRole() == USER_ROLE.ROLE_CUSTOMER) {
                Cart cart = new Cart();
                cart.setCartId(user.getUserId());
                cart.setCustomer(user);
                cartRepository.save(cart);
            }
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getPhoneNumber(), // Use phone number as username for JWT
                user.getPassword(),
                authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        authResponse.setRole(user.getRole());
        authResponse.setUserId(user.getUserId());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
