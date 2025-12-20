package dev.cascadiatech.trackfi.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import dev.cascadiatech.trackfi.core.CRDControllerTest;
import java.time.LocalDate;
import java.util.Map;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Tests security features and functionality of {@link TransactionController}
 */
@WebMvcTest(TransactionController.class)
final
class TransactionControllerTest extends CRDControllerTest<Integer, WriteTransactionView, TransactionView, TransactionSearchParameters> {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private Consumer<String> ruleApplicationService;

  @Override
  protected String endpoint() {
    return "transactions";
  }

  @Override
  protected WriteTransactionView createWriteView() {
    return new WriteTransactionView(2, "vendor", 10f, LocalDate.parse("2020-10-10"));
  }

  @Override
  protected TransactionView createOutputView() {
    return new TransactionView(1, 2, "vendor", 10f, LocalDate.parse("2020-10-10"));
  }

  @Test
  @WithMockUser
  void invalidTransaction() throws Exception {
    createBadObject(
      Map.of(
        "vendor", "",
        "date", LocalDate.now().plusYears(1)
      ),
      """
        {fieldErrors:  {vendor:  ['must not be blank'], amount:  ['must not be null'], date:  ['must be a past date']}}
      """
    );

    createBadObject(
      Map.of(
        "vendor", ""
      ),
      """
        {fieldErrors:  {vendor:  ['must not be blank'], amount:  ['must not be null'], date:  ['must not be null']}}
      """
    );
  }

  /**
   * Tests that user sub (from spring security context) is provided to rule application service
   */
  @Test
  @WithMockUser("test-user")
  void applyRules() throws Exception {
    mockMvc.perform(
      patch("/api/v1/transactions/apply-rules")
    ).andExpect(
      MockMvcResultMatchers.status().isNoContent()
    );

    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(ruleApplicationService).accept(argumentCaptor.capture());
    String userId = argumentCaptor.getValue();
    assertEquals("test-user", userId);
  }

  /**
   * Test that unauthenticated users cannot use /api/v1/transactions/apply-rules[PATCH]
   */
  @Test
  @WithAnonymousUser
  void applyRulesNoAuth() throws Exception {
    mockMvc.perform(
      patch("/api/v1/transactions/apply-rules")
    ).andExpect(
      MockMvcResultMatchers.status().isForbidden()
    );
  }
}
