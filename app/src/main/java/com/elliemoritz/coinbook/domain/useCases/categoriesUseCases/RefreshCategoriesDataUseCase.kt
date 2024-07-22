package com.elliemoritz.coinbook.domain.useCases.categoriesUseCases

import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import javax.inject.Inject

class RefreshCategoriesDataUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke() {
        categoriesRepository.refreshCategoriesData()
    }
}
