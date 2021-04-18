package com.lu.user.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.user.authentication.exceptions.ErrorDetails;
import com.lu.user.authentication.exceptions.UserAuthenticationException;
import com.lu.user.authentication.model.User;
import com.lu.user.authentication.model.UserState;
import com.lu.user.authentication.repository.AddressRepository;
import com.lu.user.authentication.repository.UserRepository;
import com.lu.user.authentication.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserAuthenticationControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserServiceImpl userService;

	@MockBean
	private AddressRepository addressRepository;

	@MockBean
	private UserRepository userRepository;

	@Test
	void validLoginReturns200() throws Exception {
		when(userRepository.findByUsername(any())).thenReturn(mockUser());

		mockMvc.perform(post("/user/login", 42L)
				.header(HttpHeaders.AUTHORIZATION, "Basic bG9yZW5hLmRhbmNpdUB5YWhvby5jb206dGVzdDEyMzQ=")
				.contentType("application/json")
					.content(""))
				.andExpect(status().isNoContent());
	}

	@Test
	void invalidLoginMissingAuthHeaderReturns401AndErrorResult() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/user/login", 42L)
          .contentType("application/json")
				.content(""))
          .andExpect(status().isUnauthorized())
				.andReturn();

		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Missing authentication header");

		String actualResponseBody =
				mvcResult.getResponse().getContentAsString();
		String expectedResponseBody =
				objectMapper.writeValueAsString(errorDetails);
		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(expectedResponseBody);
	}

	@Test
	void invalidLoginBadCredentialsReturns401AndErrorResult() throws Exception {
		when(userRepository.findByUsername(any())).thenReturn(mockUser());

		MvcResult mvcResult = mockMvc.perform(post("/user/login", 42L)
				.header(HttpHeaders.AUTHORIZATION, "Basic bG9yZW5hLmRhbmNpdUB5YWhvby5jb206dGVzdDEyMzQ0")
				.contentType("application/json")
				.content(""))
				.andExpect(status().isUnauthorized())
				.andReturn();

//		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(),
//				HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Authentication failed. Bad credentials.");
//
//		String actualResponseBody =
//				mvcResult.getResponse().getContentAsString();
//		String expectedResponseBody =
//				objectMapper.writeValueAsString(errorDetails);
//		assertThat(actualResponseBody)
//				.isEqualToIgnoringWhitespace(expectedResponseBody);
	}

	@Test
	void invalidLoginUserNotRegisteredReturns401AndErrorResult() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/user/login", 42L)
				.header(HttpHeaders.AUTHORIZATION, "Basic bG9yZW5hLmRhbmNpdUB5YWhvby5jb206dGVzdDEyMzQ0")
				.contentType("application/json")
				.content(""))
				.andExpect(status().isUnauthorized())
				.andReturn();


//		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(),
//				HttpStatus.UNAUTHORIZED.getReasonPhrase(), "User not found");
//
//
//		String actualResponseBody =
//				mvcResult.getResponse().getContentAsString();
//		String expectedResponseBody =
//				objectMapper.writeValueAsString(errorDetails);
//		assertThat(actualResponseBody)
//				.isEqualToIgnoringWhitespace(expectedResponseBody);
	}

	@Test
	void invalidLoginUserLockedReturns401AndErrorResult() throws Exception {
		when(userRepository.findByUsername(any())).thenReturn(mockUser(UserState.LOCKED));

		MvcResult mvcResult = mockMvc.perform(post("/user/login", 42L)
				.header(HttpHeaders.AUTHORIZATION, "Basic bG9yZW5hLmRhbmNpdUB5YWhvby5jb206dGVzdDEyMzQ0")
				.contentType("application/json")
				.content(""))
				.andExpect(status().isUnauthorized())
				.andReturn();

//		ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(),
//				HttpStatus.UNAUTHORIZED.getReasonPhrase(), "User is locked.");
//
//		String actualResponseBody =
//				mvcResult.getResponse().getContentAsString();
//		String expectedResponseBody =
//				objectMapper.writeValueAsString(errorDetails);
//		assertThat(actualResponseBody)
//				.isEqualToIgnoringWhitespace(expectedResponseBody);
	}

	private User mockUser() {
		User user = new User();
		user.setUsername("lorena.danciu@yahoo.com");
		user.setPassword("$2a$10$zalfUjwGQMIjIytT89W4EuU1OJA76Iq0bF6F4Hwz/BoLCB2afYtnS");
		user.setState(UserState.ACTIVE);

		return user;
	}

	private User mockUser(UserState state) {
		User user = mockUser();
		user.setState(state);

		return user;
	}
}
