package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.Customer;

@Controller
public class BankController {
	
	@Autowired
	RestTemplate rs;
	
	@RequestMapping("/admin")
	public ModelAndView readall() {
		ModelAndView mv = new ModelAndView("admindisplay.html");
		Customer cust = rs.getForObject("http://ADMIN/admin/read", Customer.class);
		mv.addObject(cust);
		return mv;
	}
	
	@RequestMapping("/home")
	public String home()
	{
		return "index.html";
		
	}
	
	@RequestMapping("/updatecustomer")
	public String update(Model model) {
		model.addAttribute("customerupdate",new Customer());
		return "updatecustomer.html";
	}
	
	@RequestMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("custcredentials",new Customer());
		return "login.html";
	}
	
	@GetMapping("/register")
	public String registrationform(Model model) {
		model.addAttribute("customer",new Customer());
		return "signup.html";
	}
	
	@PostMapping("/registration")
	public String customerReg(@ModelAttribute Customer customer) {
		HttpEntity<Customer> request = new HttpEntity<Customer>(customer);
		rs.postForObject("http://CUSTOMER/customer/add", request, Customer.class);
		return "successpage.html";
	}
	
	@PutMapping("/updatingcustomer/{acc_no}")
	public ResponseEntity<Customer> customerUpdating(@PathVariable long acc_no,@RequestBody Customer customer) {
		HttpEntity<Customer> request = new HttpEntity<Customer>(customer);
//		rs.postForObject("http://CUSTOMER/customer/update/"+customer.getAcc_no(), request, Customer.class);
		ResponseEntity<Customer> cust = rs.exchange("http://CUSTOMER/customer/update/"+acc_no, HttpMethod.PUT, request, Customer.class);
		return cust;
	}
	
	
	@PostMapping(value="/loggingin", consumes="application/x-www-form-urlencoded")
	public ModelAndView login(Customer cust) {
		ModelAndView mv = new ModelAndView();
		Customer cust1 = rs.getForObject((
				"http://CUSTOMER/customer/loggingin/"+cust.getUsername()+"/"+cust.getPassword()),
				Customer.class);
		if(cust1==null) {
			mv.setViewName("login.html");
			return mv;
		}
		mv.addObject("Customer",cust1);
		mv.setViewName("customerdetails.html");
		return mv;
		
	}
	
	
	@PutMapping("/deposit/{acc_no}/{balance}")
	public ResponseEntity<Customer> deposit(@PathVariable long acc_no, @PathVariable float balance) {
		ResponseEntity<Customer> cust = 
				rs.exchange("http://CUSTOMER/customer/deposit/"+acc_no+"/"+balance, 
						HttpMethod.PUT, null, Customer.class);
		return cust;
	}
	
	@PutMapping("/withdraw/{acc_no}/{balance}")
	public ResponseEntity<Customer> withdraw(@PathVariable long acc_no, @PathVariable float balance) {
		ResponseEntity<Customer> cust = 
				rs.exchange("http://CUSTOMER/customer/withdraw/"+acc_no+"/"+balance, 
						HttpMethod.PUT, null, Customer.class);
		return cust;
	}
	

}
