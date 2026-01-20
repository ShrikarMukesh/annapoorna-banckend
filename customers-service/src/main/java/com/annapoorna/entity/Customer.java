package com.annapoorna.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "customers")
@ToString
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    private String customerId;

    @NotBlank(message = "First Name cannot be blank")
    @Size(min = 3, max = 50, message = "First Name must be between 3 and 50 characters")
    private String firstName;
    
    private String userName;
    
    private String password;

    @Size(min = 3, max = 50, message = "Last Name must be between 3 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
	
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp="(^$|[0-9]{10})", message="Invalid phone number, should have exactly 10 digits")
    private String phoneNumber;
    
    private List<Order> orders;

    //private Set<Role> roles;
   //@DBRef
    private Set<Role> roles;

    @NotNull(message = "Created timestamp cannot be null")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated timestamp cannot be null")
    private LocalDateTime updatedAt;

    private List<Address> address;

    private List<Payment> payments;

    private List<Referral> referrals;

    public Customer() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
}

