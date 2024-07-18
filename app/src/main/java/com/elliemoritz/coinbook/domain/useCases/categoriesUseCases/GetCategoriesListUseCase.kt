package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import androidx.lifecycle.LiveData
import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import javax.inject.Inject

class GetCategoriesListUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    operator fun invoke(): LiveData<List<Category>> {
        return categoriesRepository.getCategoriesList()
    }
}
