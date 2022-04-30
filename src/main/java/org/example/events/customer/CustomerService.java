package org.example.events.customer;

import org.example.events.email.EmailService;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerService
{

	private final CustomerRepository customerRepository;

	private final EmailService emailService;

	public void register(Customer customer)
	{
		customerRepository.save(customer);
		emailService.sendRegisterEmail(customer);
	}

	public void remove(Customer customer)
	{
		customerRepository.delete(customer);
	}
}
