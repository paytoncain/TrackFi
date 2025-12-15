package dev.cascadiatech.trackfi.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests application workflow and integration of application features
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EndToEndTest {

  @Autowired
  private TestRestTemplate restTemplate;

  /**
   * Tests complete application workflow via REST API
   */
  @Test
  public void test() {
    createTransaction();
  }

  private void createTransaction() {
    ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(
      "/api/v1/transactions",
      Map.of("userId", 1, "vendor", "vendor", "amount", 10, "date", LocalDate.now().minusYears(1)),
      String.class
    );
    System.out.println(stringResponseEntity.getBody());
    assertEquals(HttpStatus.CREATED.value(), stringResponseEntity.getStatusCode().value());
  }

}
