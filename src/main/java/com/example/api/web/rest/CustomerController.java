package com.example.api.web.rest;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.api.domain.Customer;
import com.example.api.service.CustomerService;

@RestController
@RequestMapping("/customers")
@CrossOrigin
@RequestScope
public class CustomerController {

	private CustomerService service;

	@Autowired
	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping("/pageable/{page}/{itens}")
	public List<Customer> findAll(@PathVariable Integer page, @PathVariable Integer itens) {
		return service.findAll(page, itens);
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return service.findById(id);
	}
	
	@PostMapping
	public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer,
			HttpServletResponse response) {
		
		Customer resposta = service.create(customer);
		
		URI uri =  ServletUriComponentsBuilder
				.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(resposta.getId()).toUri();
		
		return ResponseEntity.created(uri).body(resposta);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Customer> update(@PathVariable Long id, @Valid @RequestBody Customer customer) {
		Customer resposta = service.update(id, customer);
		return ResponseEntity.ok(resposta);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
