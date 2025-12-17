package dev.cascadiatech.trackfi.transaction;

import java.time.LocalDate;

/**
 * View for reading existing transactions
 * @param id transaction id
 * @param vendor transaction vendor name
 * @param amount transaction amount
 * @param date transaction date
 */
record Transaction(Integer id, String vendor, Float amount, LocalDate date) {

}
