package com.elliemoritz.coinbook.data.repositories

import com.elliemoritz.coinbook.data.dao.CategoriesDao
import com.elliemoritz.coinbook.data.mappers.CategoryMapper
import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val dao: CategoriesDao,
    private val mapper: CategoryMapper
) : CategoriesRepository {

    private val refreshListEvents = MutableSharedFlow<Unit>()
    private val refreshCategoryEvents = MutableSharedFlow<Unit>()

    override fun getCategoriesList(): Flow<List<Category>> = flow {
        val list = dao.getCategoriesList()
        val result = mapper.mapListDbModelToListEntities(list)
        emit(result)
        refreshListEvents.collect {
            val updatedList = dao.getCategoriesList()
            val updatedResult = mapper.mapListDbModelToListEntities(updatedList)
            emit(updatedResult)
        }
    }

    override fun getCategory(id: Int): Flow<Category> = flow {
        val dbModel = dao.getCategory(id)
        val result = mapper.mapDbModelToEntity(dbModel)
        emit(result)
        refreshCategoryEvents.collect {
            val updatedDbModel = dao.getCategory(id)
            val updatedResult = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedResult)
        }
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

    override suspend fun refreshCategoriesData() {
        refreshListEvents.emit(Unit)
        refreshCategoryEvents.emit(Unit)
    }
}