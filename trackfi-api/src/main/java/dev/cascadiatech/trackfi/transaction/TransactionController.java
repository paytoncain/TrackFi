package dev.cascadiatech.trackfi.transaction;

import dev.cascadiatech.trackfi.core.CRDController;
import dev.cascadiatech.trackfi.core.Datastore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for managing transactions
 */
@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController extends CRDController<Integer, WriteTransaction, Transaction> {

    /**
     * Creates a {@link TransactionController}
     * @param datastore {@link Datastore} for managing transactions
     */
  protected TransactionController(Datastore<Integer, WriteTransaction, Transaction> datastore) {
    super(datastore);
  }

}
