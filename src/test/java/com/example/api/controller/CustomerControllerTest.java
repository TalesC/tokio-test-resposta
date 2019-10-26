package com.example.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.service.CustomerService;
import com.example.api.web.rest.CustomerController;

@WebAppConfiguration
@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {
	
	private MockMvc mockMvc;
	
	@InjectMocks
	private CustomerController controller;
	
	@Mock
	private CustomerService service;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void deveRetornar200OkQuandoBuscarTodosPaginado() throws Exception {
		mockMvc.perform(get("/customers/pageable/{page}/{itens}", 0, 10))
			.andExpect(status().isOk());
	}
	
	@Test
	public void deveRetornar400BadRequestQuandoBuscarTodosPaginado() throws Exception {
		when(service.findAll(-1, -1))
			.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "..."));
		
		mockMvc.perform(get("/customers/pageable/{page}/{itens}", -1, -1))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void deveRetornar200OkQuandoBuscarPorId() throws Exception {
		mockMvc.perform(get("/customers/{id}", 1)).andExpect(status().isOk());
	}
	
	@Test
	public void deveRetornar404NotFoundQuandoBuscarPorId() throws Exception {
		when(service.findById(99L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "..."));
		mockMvc.perform(get("/customers/{id}", 99)).andExpect(status().isNotFound());
	}
	
	@Test
	public void deveRetornar200OKQuandoCriarCustomerNovo() throws Exception {
		Map<String, String> map = genHashMap();
		Customer customer = genCustomer();
		when(service.create(any(Customer.class))).thenReturn(customer);
		
		mockMvc.perform(post("/customers")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new JSONObject(map).toString()))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void deveRetornar400BadRequestQuandoValidarCustomerNovo() throws Exception {
		mockMvc.perform(post("/customers")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new JSONObject().toString()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deveRetornar200OKQuandoAtualizarCustomer() throws Exception {
		Map<String, String> map = genHashMap();
		Customer customer = genCustomer();
		when(service.update(anyLong(), any(Customer.class))).thenReturn(customer);
		
		mockMvc.perform(put("/customers/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(new JSONObject(map).toString()))
		.andExpect(status().isOk());
	}
	
	@Test
	public void deveRetornar400BadRequestQuandoValidarAtualizarCustomer() throws Exception {
		mockMvc.perform(put("/customers/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(new JSONObject().toString()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deveRetornar404NotFoundQuandoValidarCustomerParaAtualizar() throws Exception {
		Map<String, String> map = genHashMap();
		when(service.update(anyLong(), any(Customer.class)))
			.thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "..."));
		
		mockMvc.perform(put("/customers/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(new JSONObject(map).toString()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void deveRetornar200OkQuandoDeletarCustomer() throws Exception {
		mockMvc.perform(delete("/customers/{id}", 1L)).andExpect(status().isNoContent());
	}
	
	@Test
	public void deveRetornar404NotFoundQuandoDeletarCustomer() throws Exception {
		doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "...")).when(service).delete(anyLong());
		mockMvc.perform(delete("/customers/{id}", 1L)).andExpect(status().isNotFound());
	}
	
	private Map<String, String> genHashMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "teste");
		map.put("email", "teste@email.com");
		return map;
	}
	
	
	private Customer genCustomer() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName("teste");
		customer.setEmail("teste@email.com");
		return customer;
	}
	
}
