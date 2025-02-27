package com.idea.springboot.webflux.app;

import com.idea.springboot.webflux.app.models.MessageResponse;
import com.idea.springboot.webflux.app.models.dtos.CategoryDTO;
import com.idea.springboot.webflux.app.models.dtos.ProductDTO;

import com.idea.springboot.webflux.app.services.CategoryService;
import com.idea.springboot.webflux.app.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private CategoryService categoryService;

	@Test
	public void listTest() {
		client.get()
				.uri("/api/v2/products/")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(ProductDTO.class)
				.hasSize(16);
	}

	@Test
	public void notEmptyListWithConsumeWithTest() {
		client.get()
				.uri("/api/v2/products/")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(ProductDTO.class)
				.consumeWith(response -> {
					Assertions.assertTrue(response.getResponseBody().size() > 0);
				});
	}

	@Test
	public void noRecordExistMessageAndStatusCodeTest() {
		client.get()
				.uri("/api/v2/products/")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().is4xxClientError()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.message").isEqualTo("No records exist");
	}

	@Test
	public void statusCodeNotFoundByIdTest() {
		String id = "67bf9ad0d572bc1cde0586a";

		client.get()
				.uri("/api/v2/products/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.header("id", id)
				.exchange()
				.expectStatus().is4xxClientError()
				.expectStatus().isNotFound()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.message").isEqualTo("Product with id '"+ id +"' not found")
				.jsonPath("$.status").isEqualTo(404);
	}

	@Test
	public void existProductByIdTest() {
		String id = "67bf9ad0d572bc1cde0586ab";

		client.get()
				.uri("/api/v2/products/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.header("id", id)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(ProductDTO.class)
				.consumeWith(response -> {
					ProductDTO responseDto = response.getResponseBody();
					Assertions.assertEquals(responseDto.getId(), id);
					Assertions.assertNotNull(responseDto.getName());
					Assertions.assertEquals(responseDto.getName(), "Samsung S23 FE 128GB");
					Assertions.assertNotNull(responseDto.getPrice());
					Assertions.assertEquals(responseDto.getPrice(), 389.99);

				});
	}

	@Test
	public void createTest() {
		CategoryDTO categoryDTO = categoryService.findByName("Technology").block();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Iphone 14 XS 256GB");
		productDTO.setPrice(1269.99);
		productDTO.setCategory(categoryDTO);

		client.post()
				.uri("/api/v2/products/create")
				.accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(productDTO))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(ProductDTO.class)
				.consumeWith(response -> {
					ProductDTO responseDto = response.getResponseBody();
					// Verifica que la respuesta no sea nula
					Assertions.assertNotNull(responseDto);

					// Verifica los campos del producto
					Assertions.assertNotNull(responseDto.getId());

					Assertions.assertEquals(responseDto.getName(), productDTO.getName());
					Assertions.assertEquals(responseDto.getPrice(), productDTO.getPrice());

					// Verifica los campos de categoría y created_at
					Assertions.assertNotNull(responseDto.getCategory());
					// Verifica que la categoría sea la misma
					Assertions.assertEquals(responseDto.getCategory().getId(), categoryDTO.getId());
					Assertions.assertEquals(responseDto.getCategory().getName(), categoryDTO.getName());
					Assertions.assertEquals(responseDto.getCategory().getCreatedAt(), categoryDTO.getCreatedAt());

					Assertions.assertNotNull(responseDto.getCreatedAt());
				});
	}

	@Test
	public void updateTest() {
		String id = "67c0a732362a641ac1b79ea1";

		CategoryDTO categoryDTO = categoryService.findByName("Electronics").block();
		ProductDTO productToUpdate = new ProductDTO();
		productToUpdate.setName("Xiaomi Redmi 4 256GB");
		productToUpdate.setPrice(459.99);
		productToUpdate.setCategory(categoryDTO);

		client.put()
				.uri("/api/v2/products/update/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.header("id", id)
				.body(BodyInserters.fromValue(productToUpdate))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(ProductDTO.class)
				.consumeWith(response -> {
					ProductDTO responseDto = response.getResponseBody();
					// Verifica que la respuesta no sea nula
					Assertions.assertNotNull(responseDto);

					// Verifica los campos del producto
					Assertions.assertEquals(responseDto.getId(), id);

					Assertions.assertEquals(responseDto.getName(), productToUpdate.getName());
					Assertions.assertEquals(responseDto.getPrice(), productToUpdate.getPrice());

					// Verifica los campos de categoría y created_at
					Assertions.assertNotNull(responseDto.getCategory());
					// Verifica que la categoría sea la misma
					Assertions.assertEquals(responseDto.getCategory().getId(), categoryDTO.getId());
					Assertions.assertEquals(responseDto.getCategory().getName(), categoryDTO.getName());
					Assertions.assertEquals(responseDto.getCategory().getCreatedAt(), categoryDTO.getCreatedAt());

					Assertions.assertNotNull(responseDto.getCreatedAt());
				});
	}

	@Test
	public void deleteTest() {
		String id = "67c0a732362a641ac1b79ea1";

		client.delete()
				.uri("/api/v2/products/delete/{id}", id)
				.header("id", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(MessageResponse.class)
				.consumeWith(response -> {
					Assertions.assertNotNull(response);

					MessageResponse messageResponse = response.getResponseBody();

					// Verifica que el mensaje sea el correcto
					Assertions.assertEquals("Successfully deleted", messageResponse.getMessage());
					Assertions.assertNotNull(messageResponse.getTimestamp());
				});
	}

}
