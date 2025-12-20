package dev.cascadiatech.trackfi;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    String category = createCategory(
      """
      {"name": "category"}
      """
    );
    String rule = createRule(
      """
      {"categoryId": %d, "vendor": "vendor"}
      """.formatted(getIdFromObjectString(category))
    );
    String transaction = createTransaction(
      """
      {"vendor": "vendor", "amount": 10, "date": "2020-10-10"}
      """
    );
    applyRules();
    transaction = getTransaction(
      """
      {"id": %s, "categoryId": %d, "vendor": "vendor", "amount": 10, "date": "2020-10-10"}
      """.formatted(getIdFromObjectString(transaction), getIdFromObjectString(category))
    );

    listTransactions(transaction);
    deleteTransaction(transaction);
    listTransactions("");

    listRules(rule);
    getRule(rule);
    deleteRule(rule);
    listRules("");

    listCategories(category);
    getCategory(category);
    deleteCategory(category);
    listCategories("");
  }

  /**
   * apply categoryIds to transactions via rules
   */
  private void applyRules() throws Exception {
    mockMvc.perform(
      patch("/api/v1/transactions/apply-rules")
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );
  }

  /**
   * Delete rule belonging to authenticated user
   */
  private void deleteRule(String rule) throws Exception {
    mockMvc.perform(
      delete("/api/v1/rules/%d".formatted(getIdFromObjectString(rule)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );
  }

  /**
   * Get rule belonging to authenticated user by id (should equal output from {@link EndToEndTest#createRule(String)}
   */
  private void getRule(String rule) throws Exception {
    mockMvc.perform(
      get("/api/v1/rules/%d".formatted(getIdFromObjectString(rule)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json(rule)
    );
  }

  /**
   * List rules belonging to authenticated user and assert output
   */
  private void listRules(String rule) throws Exception {
    mockMvc.perform(
      get("/api/v1/rules")
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("{items: [%s]}".formatted(rule))
    );
  }

  /**
   * Creates rule as authenticated user and asserts output
   */
  private String createRule(String rule) throws Exception {
    return mockMvc.perform(
      post("/api/v1/rules")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(rule)
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isCreated()
    ).andExpect(
      MockMvcResultMatchers.content().json(rule)
    ).andReturn()
    .getResponse()
    .getContentAsString();
  }

  /**
   * Delete category belonging to currently authenticated user (should be deleting output from {@link EndToEndTest#createCategory(String)}
   */
  private void deleteCategory(String category) throws Exception {
    mockMvc.perform(
      delete("/api/v1/categories/%d".formatted(getIdFromObjectString(category)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );
  }

  /**
   * Get category belonging to currently authenticated user and assert output (should be the same as output from {@link EndToEndTest#createCategory(String)}
   */
  private void getCategory(String category) throws Exception {
    mockMvc.perform(
      get("/api/v1/categories/%d".formatted(getIdFromObjectString(category)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json(category)
    );
  }

  /**
   * list categories belonging to currently authenticated user and assert output
   */
  private void listCategories(String category) throws Exception {
    mockMvc.perform(
      get("/api/v1/categories")
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("{items: [%s]}".formatted(category))
    );
  }

  /**
   * creates category as authenticated user and asserts output
   */
  private String createCategory(String category) throws Exception {
    return mockMvc.perform(
      post("/api/v1/categories")
        .with(user("user"))
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(category)
    ).andExpect(
      MockMvcResultMatchers.status().isCreated()
    ).andExpect(
      MockMvcResultMatchers.content().json(category)
    ).andReturn()
    .getResponse()
    .getContentAsString();
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
      MockMvcResultMatchers.content().json(transaction)
    ).andReturn()
    .getResponse()
    .getContentAsString();
  }

  /**
   * list transactions belonging to authenticated user
   */
  private void listTransactions(String expectedTransaction) throws Exception {
    mockMvc.perform(
      get("/api/v1/transactions")
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json("{items: [%s]}".formatted(expectedTransaction))
    );
  }

  /**
   * get transaction belonging to authenticated user (should contain the same resulting transaction as {@link EndToEndTest#createTransaction(String)})
   */
  private String getTransaction(String expectedTransaction) throws Exception {
    return mockMvc.perform(
      get("/api/v1/transactions/%s".formatted(getIdFromObjectString(expectedTransaction)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isOk()
    ).andExpect(
      MockMvcResultMatchers.content().json(expectedTransaction)
    ).andReturn()
    .getResponse()
    .getContentAsString();
  }

  /**
   * delete transaction belonging to authenticated user
   */
  private void deleteTransaction(String transaction) throws Exception {
    mockMvc.perform(
      delete("/api/v1/transactions/%d".formatted(getIdFromObjectString(transaction)))
        .with(user("user"))
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );
  }

  /**
   * Gets id as an integer from an input JSON blob
   * @param object string containing managed application data type
   * @return object id
   * @throws JsonProcessingException if input string contains invalid JSON
   */
  private Integer getIdFromObjectString(String object) throws JsonProcessingException {
    JsonNode jsonNode = objectMapper.readTree(object);
    return jsonNode.get("id").asInt();
  }

}
