package dev.cascadiatech.trackfi.api.transaction;

import java.time.LocalDate;

/**
 * View for reading existing transactions
 * @param id transaction id
 * @param userId transaction user id
 * @param vendor transaction vendor name
 * @param amount transaction amount
 * @param date transaction date
 */
record Transaction(Integer id, String userId, String vendor, Float amount, LocalDate date) implements BaseTransaction {

}
