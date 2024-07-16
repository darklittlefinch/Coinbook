package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class GetCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    suspend fun getCategory(id: Int): Category {
        return categoriesRepository.getCategory(id)
    }
}
