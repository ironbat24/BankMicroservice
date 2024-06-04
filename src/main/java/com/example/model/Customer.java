package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
	Long acc_no;
	String name;
	String username;
	String password;
	int age;
	String SSN;
	String address;
	String email;
	String phone;
	float balance;

}
