package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository

class AddCategoryUseCase(
    private val categoriesRepository: CategoriesRepository
) {
    fun addCategory(category: Category) {
        categoriesRepository.addCategory(category)
    }
}
