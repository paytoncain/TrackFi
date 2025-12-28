package com.github.paytoncain.trackfi.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
@ComponentScan("com.github.paytoncain.trackfi.config")
public abstract class CRDControllerTest<ID, W, T, P extends PageParameters> {

  @MockitoBean
  private Datastore<ID, W, T, P> datastore;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * get v1 api segment /api/v1/{returnValue}
   * @return api segment name
   */
  protected abstract String endpoint();

  /**
   * create object for POST endpoint input
   * @return object for POST endpoint input
   */
  protected abstract W createWriteView();

  /**
   * create output object for simulating datastore output
   * @return output object
   */
  protected abstract T createOutputView();

  /**
   * Test object serialization, deserialization, and integration with {@link Datastore}
   */
  @Test
  @WithMockUser
  void create() throws Exception {
    W writeView = createWriteView();
    T outputView = createOutputView();
    when(datastore.create(eq(writeView), anyString())).thenReturn(outputView);

    mockMvc.perform(
      post("/api/v1/%s".formatted(endpoint()))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(createWriteView()))
    ).andExpect(
      MockMvcResultMatchers.status().is(CREATED.value())
    ).andExpect(
      MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(createOutputView()))
    );
  }

  /**
   * Test that object validation errors are reported back to the client
   */
  protected void createBadObject(Map<String, Object> invalidObject, String expectedResponseBody) throws Exception {
    mockMvc.perform(
      post("/api/v1/%s".formatted(endpoint()))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(invalidObject))
    ).andExpect(
      MockMvcResultMatchers.status().isUnprocessableEntity()
    ).andExpect(
      MockMvcResultMatchers.content().json(expectedResponseBody)
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/%s[POST]
   */
  @Test
  @WithAnonymousUser
  void createNoAuth() throws Exception {
    mockMvc.perform(
      post("/api/v1/%s".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }

  /**
   * Test object serialization, deserialization, and integration with {@link Datastore}
   */
  @Test
  @WithMockUser
  void list() throws Exception {
    T outputView = createOutputView();

    when(datastore.list(any(), anyString())).thenReturn(
      PageView.<T>builder()
        .page(1)
        .itemsPerPage(1)
        .totalItems(1L)
        .totalPages(1)
        .items(Collections.singletonList(outputView))
        .build()
    );

    mockMvc.perform(
      get("/api/v1/%s".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("""
        {"page": 1, "itemsPerPage": 1, "totalPages": 1, "totalItems": 1, "items": [%s]}
        """.formatted(objectMapper.writeValueAsString(outputView)))
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/%s[GET]
   */
  @Test
  @WithAnonymousUser
  void listNoAuth() throws Exception {
    mockMvc.perform(
      get("/api/v1/%s".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }

  /**
   * Test object serialization, deserialization, and integration with {@link Datastore}
   */
  @Test
  @WithMockUser
  void getObject() throws Exception {
    T outputView = createOutputView();
    when(datastore.get(any(), anyString())).thenReturn(outputView);

    mockMvc.perform(
      get("/api/v1/%s/1".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(outputView))
    );
  }

  /**
   * Tests that when an object cannot be found by the datastore, this endpoint returns a 404 status code
   */
  @Test
  @WithMockUser
  void getNotFound() throws Exception {
    when(datastore.get(any(), anyString())).thenThrow(new NotFoundException("not found"));

    mockMvc.perform(
      get("/api/v1/%s/1".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isNotFound()
    ).andExpect(
      MockMvcResultMatchers.content().json("{requestErrors:  ['not found']}")
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/%s/{id}[GET]
   */
  @Test
  @WithAnonymousUser
  void getNoAuth() throws Exception {
    mockMvc.perform(
      get("/api/v1/%s/1".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }

  /**
   * Test integration with datastore and returns no content status code
   */
  @Test
  @WithMockUser
  void deleteObject() throws Exception {
    when(datastore.get(any(), anyString())).thenReturn(createOutputView());

    mockMvc.perform(
      delete("/api/v1/%s/1".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );
  }

  /**
   * Tests that when an object cannot be found by the datastore, this endpoint returns a 404 status code
   */
  @Test
  @WithMockUser
  void deleteNotFound() throws Exception {
    doThrow(new NotFoundException("not found exception")).when(datastore).delete(any(), anyString());

    mockMvc.perform(
      delete("/api/v1/%s/1".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isNotFound()
    ).andExpect(
      MockMvcResultMatchers.content().json("{requestErrors:  ['not found exception']}")
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/%s/{id}[DELETE]
   */
  @Test
  @WithAnonymousUser
  void deleteNoAuth() throws Exception {
    mockMvc.perform(
      delete("/api/v1/%s/1".formatted(endpoint()))
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }

}