package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    operator fun invoke(id: Long): Flow<Category> {
        return categoriesRepository.getCategory(id)
    }
}
