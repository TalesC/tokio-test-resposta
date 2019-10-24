package com.example.api.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.repository.CustomerRepository;

@Service
public class CustomerService {

	private CustomerRepository repository;

	@Autowired
	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public List<Customer> findAll(Integer page, Integer itens) {
		Pageable pageItens = PageRequest.of(page, itens);
		return repository.findAllByOrderByNameAsc(pageItens);
	}

	public Customer findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
	}

	public Customer save(Customer customer) {
		return repository.save(customer);
	}

	public Customer update(Long id, @Valid Customer customer) {
		Customer existente = this.findById(id);
		BeanUtils.copyProperties(customer, existente, "id");
		return repository.save(existente);
	}

	public void delete(Long id) {
		this.findById(id);
		repository.deleteById(id);
	}
	

}
