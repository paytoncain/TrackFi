package com.github.paytoncain.trackfi.rule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * View for writing rules
 * @param categoryId rule category id
 * @param <CID> category id Java type
 * @param vendor pattern matching transaction vendors
 */
record WriteRuleView<CID>(@NotNull CID categoryId, @NotBlank String vendor) {

}
