package com.example.api.web.rest;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.api.domain.Address;
import com.example.api.service.AddressService;

@RestController
@RequestMapping("/address")
@RequestScope
@CrossOrigin
public class AddressController {
	
	@Autowired
	private AddressService service;	
	
	@GetMapping
	public List<Address> findByAll() {
		return service.findAll();
	}
	
	@GetMapping("/customer/{idCustumer}")
	public List<Address> findByCustomer(@PathVariable Long idCustumer) {
		return service.findByCustomer(idCustumer);
	}
	
	@PostMapping
	public ResponseEntity<Address> create(@Valid @RequestBody Address address) {
		Address resposta = service.save(address);
		
		URI uri =  ServletUriComponentsBuilder
				.fromCurrentRequestUri().path("/{id}")
				.buildAndExpand(resposta.getId()).toUri();
		
		return ResponseEntity.created(uri).body(resposta);
	}
	
}
