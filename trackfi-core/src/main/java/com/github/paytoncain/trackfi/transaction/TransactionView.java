package com.github.paytoncain.trackfi.transaction;

import java.time.LocalDate;

/**
 * View for reading existing transactions
 * @param id transaction id
 * @param <ID> id Java type
 * @param categoryId transaction category id (nullable)
 * @param <CID> category id Java type
 * @param vendor transaction vendor name
 * @param amount transaction amount
 * @param date transaction date
 */
record TransactionView<ID, CID>(ID id, CID categoryId, String vendor, Float amount, LocalDate date) {

}
