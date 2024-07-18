package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import javax.inject.Inject

class EditCategoryUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend fun editCategory(category: Category) {
        categoriesRepository.editCategory(category)
    }
}
