package dev.cascadiatech.trackfi.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Tests application workflow and integration of application features
 */
@SpringBootTest
@AutoConfigureMockMvc
class EndToEndTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Tests complete application workflow via REST API
   */
  @Test
  public void test() throws Exception {
    checkHealth();
    String transaction = createTransaction("{\"vendor\": \"vendor\", \"amount\": 10, \"date\": \"2020-10-10\"}");
    listTransactions(transaction);
    getTransaction(transaction);
    deleteTransaction(transaction);
    listTransactions("");
  }

  /**
   * query application health status
   */
  private void checkHealth() throws Exception {
    mockMvc.perform(
      get("/actuator/health")
        .with(anonymous())
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("{status:  'UP'}")
    );
  }

  /**
   * creates transaction as authenticated user and assert resulting content
   */
  private String createTransaction(String transaction) throws Exception {
    return mockMvc.perform(
      post("/api/v1/transactions")
        .with(user("user"))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(transaction)
    ).andExpect(
      MockMvcResultMatchers.status().isCreated()
    ).andExpect(
      MockMvcResultMatchers.content().json("{userId:  'user', vendor:  'vendor', amount:  10.0, date:  '2020-10-10'}")
    ).andReturn()
    .getResponse()
    .getContentAsString();
  }

  /**
   * list transactions belonging to authenticated user (should contain the same resulting transaction as {@link EndToEndTest#createTransaction(String)})
   */
  private void listTransactions(String expectedTransaction) throws Exception {
    mockMvc.perform(
      get("/api/v1/transactions")
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("[%s]".formatted(expectedTransaction))
    );
  }

  /**
   * get transaction belonging to authenticated user (should contain the same resulting transaction as {@link EndToEndTest#createTransaction(String)})
   */
  private void getTransaction(String expectedTransaction) throws Exception {
    mockMvc.perform(
      get("/api/v1/transactions/%s".formatted(getIdFromTransactionString(expectedTransaction)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json(expectedTransaction)
    );
  }

  /**
   * delete transaction belonging to authenticated user
   */
  private void deleteTransaction(String transaction) throws Exception {
    mockMvc.perform(
      delete("/api/v1/transactions/%d".formatted(getIdFromTransactionString(transaction)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );
  }

  private Integer getIdFromTransactionString(String transaction) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(transaction);
    return jsonNode.get("id").asInt();
  }

}
