package dev.cascadiatech.trackfi.rule;

/**
 * View for reading existing rules
 * @param id rule id
 * @param categoryId rule category id
 * @param vendorRegex regular expression matching transaction vendors
 */
record Rule(Integer id, Integer categoryId, String vendorRegex) {

}
