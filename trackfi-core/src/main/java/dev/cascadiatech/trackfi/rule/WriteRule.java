package dev.cascadiatech.trackfi.rule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * View for writing rules
 * @param categoryId rule category id
 * @param vendorRegex regular expression matching transaction vendors
 */
record WriteRule(@NotNull Integer categoryId, @NotBlank String vendorRegex) {

}
