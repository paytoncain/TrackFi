package dev.cascadiatech.trackfi.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

/**
 * View for writing transactions
 * @param categoryId transaction category id (nullable)
 * @param vendor transaction vendor name
 * @param amount transaction amount
 * @param date transaction date
 */
record WriteTransactionView(Integer categoryId, @NotBlank String vendor, @NotNull Float amount, @NotNull @Past LocalDate date) {

}
