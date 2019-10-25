package com.example.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.repository.CustomerRepository;
import com.example.api.service.CustomerService;

@RunWith(SpringRunner.class)
public class CustomerServiceTest {
	
	@InjectMocks
	private CustomerService service;
	
	@Mock
	private CustomerRepository repository;
	
	@Test
	public void deveBuscarCustomersComPaginacao() {
		when(repository.findAllByOrderByNameAsc(PageRequest.of(0, 10)))
			.thenReturn(Arrays.asList(new Customer()));
		
		List<Customer> customers = service.findAll(0, 10);
				
		assertNotNull(customers);
		assertTrue(customers.size() > 0);
	}
	
	@Test(expected = ResponseStatusException.class)
	public void deveValidarPaginacaoQuandoBuscarCustomers() {
		service.findAll(-1, 0);
	}
			
	@Test
	public void deveBuscarCustomerPorId() {
		Customer customer = genCustomer();
		when(repository.findById(1L)).thenReturn(Optional.of(customer));
		
		Customer resposta = service.findById(1L);
		
		assertNotNull(customer);
		assertEquals(customer, resposta);
	}
	
	@Test(expected = ResponseStatusException.class)
	public void deveVerificarSeOCustomerExisteQuandoBuscarPorId() {
		service.findById(-1L);
	}
	
	@Test
	public void deveRetornarCustomerQuandoCriarNovo() {
		Customer customer = genCustomer();
		when(repository.save(customer)).thenReturn(customer);
		
		Customer resposta = service.create(customer);
		assertNotNull(customer);
		assertEquals(customer, resposta);
	}
	
	@Test
	public void deveAtualizarCustomer() {
		Customer customer = genCustomer();
		when(repository.findById(1L)).thenReturn(Optional.of(customer));
		when(repository.save(customer)).thenReturn(customer);
		
		Customer resposta = service.update(1L, customer);
		assertNotNull(customer);
		assertEquals(customer, resposta);
	}
	
	@Test(expected = ResponseStatusException.class)
	public void deveValidarCustomerExistenteQuandoAtualizar() {
		service.update(1L, genCustomer());
	}
	
	@Test
	public void deveDeletarUmCustomer() {
		when(repository.findById(1L)).thenReturn(Optional.of(genCustomer()));
		service.delete(1L);
	}
	
	@Test(expected = ResponseStatusException.class)
	public void deveValidarUsuarioExistenteQuandoDeletar() {
		service.delete(1L);
	}
	
	
	private Customer genCustomer() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName("joselito");
		customer.setEmail("joselito@email.com");
		return customer;
	}
	
}
