package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.CategoriesDao
import com.elliemoritz.coinbook.data.mappers.CategoryMapper
import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val dao: CategoriesDao,
    private val mapper: CategoryMapper
) : CategoriesRepository {

    override fun getCategoriesList(): Flow<List<Category>> = flow {
        val dbModelsList = dao.getCategoriesList()
        val categoriesList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(categoriesList)
    }

    override fun getCategory(id: Int): Flow<Category> = flow {
        val dbModel = dao.getCategory(id)
        val category = mapper.mapDbModelToEntity(dbModel)
        emit(category)
    }

    override suspend fun addCategory(category: Category) {
        val dbModel = mapper.mapEntityToDbModel(category)
        dao.addCategory(dbModel)
    }

    override suspend fun editCategory(category: Category) {
        val dbModel = mapper.mapEntityToDbModel(category)
        dao.addCategory(dbModel)
    }

    override suspend fun removeCategory(category: Category) {
        dao.removeCategory(category.id)
    }
}