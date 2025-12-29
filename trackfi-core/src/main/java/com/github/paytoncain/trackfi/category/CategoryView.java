package com.github.paytoncain.trackfi.category;

/**
 * View for reading existing categories
 * @param id category id
 * @param <ID> id Java type
 * @param name category name
 */
record CategoryView<ID>(ID id, String name) {

}
