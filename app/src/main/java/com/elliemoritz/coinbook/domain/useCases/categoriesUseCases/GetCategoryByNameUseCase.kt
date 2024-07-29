package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryByNameUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    operator fun invoke(name: String): Flow<Category?> {
        return categoriesRepository.getCategoryByName(name)
    }
}
