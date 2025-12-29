package com.github.paytoncain.trackfi.rule;

/**
 * View for reading existing rules
 * @param id rule id
 * @param <ID> id Java type
 * @param categoryId rule category id
 * @param <CID> category id Java type
 * @param vendor pattern matching transaction vendors
 */
record RuleView<ID, CID>(ID id, CID categoryId, String vendor) {

}
