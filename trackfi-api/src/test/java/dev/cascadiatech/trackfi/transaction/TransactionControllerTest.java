package dev.cascadiatech.trackfi.transaction;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cascadiatech.trackfi.core.Datastore;
import dev.cascadiatech.trackfi.core.NotFoundException;
import java.time.LocalDate;
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

/**
 * Tests security features and functionality of {@link TransactionController}
 */
@WebMvcTest(TransactionController.class)
@ComponentScan("dev.cascadiatech.trackfi.config")
final
class TransactionControllerTest {

  @MockitoBean
  private Datastore<Integer, WriteTransaction, Transaction> datastore;

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Test object serialization, deserialization, and integration with {@link Datastore}
   */
  @Test
  @WithMockUser
  void create() throws Exception {
    WriteTransaction writeTransaction = new WriteTransaction("vendor", 1f, LocalDate.parse("2020-10-10"));
    when(datastore.create(eq(writeTransaction), anyString())).thenReturn(new Transaction(1, "2", writeTransaction.vendor(), 10f, writeTransaction.date()));

    mockMvc.perform(
      post("/api/v1/transactions")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(writeTransaction))
    ).andExpect(
      MockMvcResultMatchers.status().is(CREATED.value())
    ).andExpect(
      MockMvcResultMatchers.content().json("{id:  1, userId:  '2', vendor:  'vendor', amount:  10.0, date:  '2020-10-10'}")
    );
  }

  /**
   * Test that transaction object validation errors are reported back to the client
   */
  @Test
  @WithMockUser
  void createBadObject() throws Exception {
    mockMvc.perform(
      post("/api/v1/transactions")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(Map.of(
          "vendor", "",
          "date", LocalDate.now().plusYears(1)
        )))
    ).andExpect(
      MockMvcResultMatchers.status().isUnprocessableEntity()
    ).andExpect(
      MockMvcResultMatchers.content().json("{fieldErrors:  {vendor:  ['must not be blank'], amount:  ['must not be null'], date:  ['must be a past date']}}")
    );

    mockMvc.perform(
      post("/api/v1/transactions")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(Map.of(
          "vendor", ""
        )))
    ).andExpect(
      MockMvcResultMatchers.status().isUnprocessableEntity()
    ).andExpect(
      MockMvcResultMatchers.content().json("{fieldErrors:  {vendor:  ['must not be blank'], amount:  ['must not be null'], date:  ['must not be null']}}")
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/transactions[POST]
   */
  @Test
  @WithAnonymousUser
  void createNoAuth() throws Exception {
    mockMvc.perform(
      post("/api/v1/transactions")
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
    when(datastore.list(anyString())).thenReturn(Collections.singletonList(new Transaction(1, "userId", "vendor", 1f, LocalDate.of(2020, 10, 10))));

    mockMvc.perform(
      get("/api/v1/transactions")
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("[{id:  1, userId:  'userId', vendor:  'vendor', amount:  1.0, date: '2020-10-10'}]")
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/transactions[GET]
   */
  @Test
  @WithAnonymousUser
  void listNoAuth() throws Exception {
    mockMvc.perform(
      get("/api/v1/transactions")
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }

  /**
   * Test object serialization, deserialization, and integration with {@link Datastore}
   */
  @Test
  @WithMockUser
  void getTransaction() throws Exception {
    when(datastore.get(anyInt(), anyString())).thenReturn(new Transaction(1, "userId", "vendor", 10f, LocalDate.parse("2020-10-10")));

    mockMvc.perform(
      get("/api/v1/transactions/1")
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("{id:  1, userId:  'userId', vendor:  'vendor', amount:  10.0, date: '2020-10-10'}")
    );
  }

  /**
   * Tests that when a transaction cannot be found by the datastore, this endpoint returns a 404 status code
   */
  @Test
  @WithMockUser
  void getNotFound() throws Exception {
    when(datastore.get(anyInt(), anyString())).thenThrow(new NotFoundException("transaction not found"));

    mockMvc.perform(
      get("/api/v1/transactions/1")
    ).andExpect(
      MockMvcResultMatchers.status().isNotFound()
    ).andExpect(
      MockMvcResultMatchers.content().json("{requestErrors:  ['transaction not found']}")
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/transactions/{id}[GET]
   */
  @Test
  @WithAnonymousUser
  void getNoAuth() throws Exception {
    mockMvc.perform(
      get("/api/v1/transactions/1")
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }

  /**
   * Test integration with datastore and returns no content status code
   */
  @Test
  @WithMockUser
  void deleteTransaction() throws Exception {
    when(datastore.get(anyInt(), anyString())).thenReturn(new Transaction(1, "userId", "vendor", 10f, LocalDate.parse("2020-10-10")));

    mockMvc.perform(
      delete("/api/v1/transactions/1")
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );
  }

  /**
   * Tests that when a transaction cannot be found by the datastore, this endpoint returns a 404 status code
   */
  @Test
  @WithMockUser
  void deleteNotFound() throws Exception {
    doThrow(new NotFoundException("not found exception")).when(datastore).delete(anyInt(), anyString());

    mockMvc.perform(
      delete("/api/v1/transactions/1")
    ).andExpect(
      MockMvcResultMatchers.status().isNotFound()
    ).andExpect(
      MockMvcResultMatchers.content().json("{requestErrors:  ['not found exception']}")
    );
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/transactions/{id}[DELETE]
   */
  @Test
  @WithAnonymousUser
  void deleteNoAuth() throws Exception {
    mockMvc.perform(
      delete("/api/v1/transactions/1")
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }
}
