package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import javax.inject.Inject

class RemoveCategoryUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend fun removeCategory(category: Category) {
        categoriesRepository.removeCategory(category)
    }
}
