package com.example.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;
import com.example.api.repository.AddressRepository;
import com.example.api.service.AddressService;
import com.example.api.service.CustomerService;

@RunWith(SpringRunner.class)
public class AddressServiceTest {
	
	@InjectMocks
	private AddressService service;
	
	@Mock
	private AddressRepository repository;
	
	@Mock
	private CustomerService custService;
	
	
	@Test
	public void deveBuscarTodos() {
		when(repository.findAll()).thenReturn(Arrays.asList(new Address()));
		List<Address> addresses = service.findAll();
		
		assertNotNull(addresses);
		assertTrue(addresses.size() > 0);
	}
	
	@Test(expected = Exception.class)
	public void deveVerificarErrosQuandoBuscarTodos() {
		when(repository.findAll()).thenThrow(new SQLException());
		service.findAll();
	}
	
	@Test
	public void deveBuscarAddressPeloIdCustomer() {
		when(repository.findByCustomerId(1L)).thenReturn(Arrays.asList(new Address()));
		List<Address> addresses = service.findByCustomer(1L);
		
		assertNotNull(addresses);
		assertTrue(addresses.size() > 0);
	}
	
	@Test(expected = ResponseStatusException.class)
	public void deveVerificarExistenciaDeCustomerQuandoBuscarAddress() {
		when(custService.findById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
		service.findByCustomer(1L);
	}
	
	@Test
	public void deveCriarUmAddressNovo() {
		Address address = genAddress();
		when(repository.save(address)).thenReturn(address);
		Address resposta = service.save(address);
		
		assertNotNull(resposta);
		assertEquals(address, resposta);
	}
	
	@Test(expected = Exception.class)
	public void deveVerificarExcecoesQuandoCriarUmNovoAddress() {
		Address address = genAddress();
		when(repository.save(address)).thenThrow(new SQLException());
		service.save(address);
	}

	private Address genAddress() {
		Address address = new Address();
		address.setId(1L);
		address.setStreet("teste");
		address.setOther("teste");
		address.setNumber(11);
		address.setCustomer(new Customer());
		return address;
	}
	
}
