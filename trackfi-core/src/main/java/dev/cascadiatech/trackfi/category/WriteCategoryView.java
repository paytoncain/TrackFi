package dev.cascadiatech.trackfi.category;

import jakarta.validation.constraints.NotBlank;

/**
 * View for writing categories
 * @param name category name
 */
record WriteCategoryView(@NotBlank String name) {

}
