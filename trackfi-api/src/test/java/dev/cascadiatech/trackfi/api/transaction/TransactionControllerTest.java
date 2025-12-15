package dev.cascadiatech.trackfi.api.transaction;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cascadiatech.trackfi.api.core.Datastore;
import java.time.LocalDate;
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

@WebMvcTest(TransactionController.class)
@ComponentScan("dev.cascadiatech.trackfi.api.config")
class TransactionControllerTest {

  @MockitoBean
  private Datastore<WriteTransaction, Transaction> datastore;

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

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

  @Test
  @WithAnonymousUser
  void createNoAuth() throws Exception {
    mockMvc.perform(
      post("/api/v1/transactions")
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }

}
