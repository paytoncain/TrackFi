package dev.cascadiatech.trackfi.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

  /**
   * Tests complete application workflow via REST API
   */
  @Test
  public void test() throws Exception {
    checkHealth();
    createTransaction();
    listTransactions();
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
  private void createTransaction() throws Exception {
    mockMvc.perform(
      post("/api/v1/transactions")
        .with(user("user"))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
        {"userId": 1, "vendor": "vendor", "amount": 10, "date": "2020-10-10"}
        """)
    ).andExpect(
      MockMvcResultMatchers.status().isCreated()
    ).andExpect(
      MockMvcResultMatchers.content().json("{userId:  'user', vendor:  'vendor', amount:  10.0, date:  '2020-10-10'}")
    );
  }

  /**
   * list transactions belonging to authenticated user (should contain the same resulting transaction as {@link EndToEndTest#createTransaction()})
   */
  private void listTransactions() throws Exception {
    mockMvc.perform(
      get("/api/v1/transactions")
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("[{userId:  'user', vendor:  'vendor', amount: 10.0, date: '2020-10-10'}]")
    );
  }

}
