package dev.cascadiatech.trackfi.api.transaction;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

  @MockitoBean
  private Datastore<WriteTransaction, Transaction> datastore;

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void create() throws Exception {
    WriteTransaction writeTransaction = new WriteTransaction(2, "vendor", 1f, LocalDate.parse("2020-10-10"));
    when(datastore.create(eq(writeTransaction))).thenReturn(new Transaction(1, 2, writeTransaction.vendor(), 10f, writeTransaction.date()));

    mockMvc.perform(
      post("/api/v1/transactions")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(writeTransaction))
    ).andExpect(
      MockMvcResultMatchers.status().is(CREATED.value())
    ).andExpect(
      MockMvcResultMatchers.content().json("{id:  1, userId:  2, vendor:  'vendor', amount:  10.0, date:  '2020-10-10'}")
    );
  }

  @Test
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
      MockMvcResultMatchers.content().json("{fieldErrors:  {userId:  ['must not be null'], vendor:  ['must not be blank'], amount:  ['must not be null'], date:  ['must be a past date']}}")
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
      MockMvcResultMatchers.content().json("{fieldErrors:  {userId:  ['must not be null'], vendor:  ['must not be blank'], amount:  ['must not be null'], date:  ['must not be null']}}")
    );
  }

}
