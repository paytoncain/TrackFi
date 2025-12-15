package dev.cascadiatech.trackfi.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
  @WithMockUser
  public void test() throws Exception {
    createTransaction();
  }

  private void createTransaction() throws Exception {
    mockMvc.perform(
      post("/api/v1/transactions")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content("""
        {"userId": 1, "vendor": "vendor", "amount": 10, "date": "2020-10-10"}
        """)
    ).andExpect(
      MockMvcResultMatchers.status().isCreated()
    ).andExpect(
      MockMvcResultMatchers.content().json("{userId:  'user', vendor:  'vendor', amount:  10, date:  '2020-10-10'}")
    );
  }

}
