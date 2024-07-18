package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend fun getCategory(id: Int): Category {
        return categoriesRepository.getCategory(id)
    }
}
