package com.example.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.api.domain.Address;
import com.example.api.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository repository;
	
	@Autowired
	private CustomerService customerService;

	
	public List<Address> findAll() {
		return repository.findAll();
	}
	
	public List<Address> findByCustomer(Long idCustumer) {
		customerService.findById(idCustumer);
		return repository.findByCustomerId(idCustumer);
	}

	public Address save(Address address) {
		return repository.save(address);
	}

	
	
}
