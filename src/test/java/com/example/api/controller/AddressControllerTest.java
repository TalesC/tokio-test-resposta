package com.example.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

import com.example.api.domain.Address;
import com.example.api.service.AddressService;
import com.example.api.web.rest.AddressController;

import net.minidev.json.JSONObject;

@WebAppConfiguration
@RunWith(MockitoJUnitRunner.class)
public class AddressControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private AddressController controller;
	
	@Mock
	private AddressService service;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void deveRetornar200OkQuandoBuscarTodos() throws Exception {
		mockMvc.perform(get("/address")).andExpect(status().isOk());
	}
	
	@Test
	public void deveRetornar200OkQuandoBuscarporIdCustomer() throws Exception {
		when(service.findByCustomer(anyLong())).thenReturn(Arrays.asList(new Address()));
		mockMvc.perform(get("/address/customer/{idCustumer}", 1L)).andExpect(status().isOk());
	}
	
	@Test
	public void deveRetornar404NotFoundQuandoBuscarporIdCustomer() throws Exception {
		when(service.findByCustomer(anyLong()))
			.thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "...") );
		mockMvc.perform(get("/address/customer/{idCustumer}", 1L)).andExpect(status().isNotFound());
	}
	
	@Test
	public void deveRetornar200OkQuandoCriarAddress() throws Exception {
		when(service.save(any(Address.class))).thenReturn(new Address());
		mockMvc.perform(post("/address")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new JSONObject(genMap()).toString())
		).andExpect(status().isCreated());
	}
	
	@Test
	public void deveRetornar$00BadRequestQuandoValidarNovoAddress() throws Exception {
		mockMvc.perform(post("/address")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new JSONObject().toString())
		).andExpect(status().isBadRequest());
	}
	
	private Map<String, Object> genMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("street", "teste");
		map.put("number", 11);
		map.put("other", "teste");
		return map;
	}
	
	
}
