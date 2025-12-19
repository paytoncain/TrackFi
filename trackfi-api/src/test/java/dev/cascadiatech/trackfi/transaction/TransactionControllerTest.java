package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.CRDControllerTest;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Tests security features and functionality of {@link TransactionController}
 */
@WebMvcTest(TransactionController.class)
final
class TransactionControllerTest extends CRDControllerTest<Integer, WriteTransactionView, TransactionView> {

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
}
