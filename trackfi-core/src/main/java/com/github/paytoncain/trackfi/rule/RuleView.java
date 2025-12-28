package com.github.paytoncain.trackfi.rule;

/**
 * View for reading existing rules
 * @param id rule id
 * @param categoryId rule category id
 * @param vendor pattern matching transaction vendors
 */
record RuleView(Integer id, Integer categoryId, String vendor) {

}
