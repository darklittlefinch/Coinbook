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

    private val refreshEvents = MutableSharedFlow<Unit>()

    private suspend fun refreshData() {
        refreshEvents.emit(Unit)
    }

    override fun getCategoriesList(): Flow<List<Category>> = flow {
        val dbModelsList = dao.getCategoriesList()
        val categoriesList = mapper.mapListDbModelToListEntities(dbModelsList)
        emit(categoriesList)

        refreshEvents.collect {
            val updatedDbModelsList = dao.getCategoriesList()
            val updatedCategoriesList = mapper.mapListDbModelToListEntities(updatedDbModelsList)
            emit(updatedCategoriesList)
        }
    }

    override fun getCategory(id: Long): Flow<Category> = flow {
        val dbModel = dao.getCategory(id)
        val category = mapper.mapDbModelToEntity(dbModel)
        emit(category)

        refreshEvents.collect {
            val updatedDbModel = dao.getCategory(id)
            val updatedCategory = mapper.mapDbModelToEntity(updatedDbModel)
            emit(updatedCategory)
        }
    }

    override fun getCategoryByName(name: String): Flow<Category?> = flow {
        val dbModel = dao.getCategoryByName(name)
        val category = if (dbModel != null) {
            mapper.mapDbModelToEntity(dbModel)
        } else {
            null
        }
        emit(category)

        refreshEvents.collect {
            val updatedDbModel = dao.getCategoryByName(name)
            val updatedCategory = if (updatedDbModel != null) {
                mapper.mapDbModelToEntity(updatedDbModel)
            } else {
                null
            }
            emit(updatedCategory)
        }
    }

    override suspend fun addCategory(category: Category): Long {
        val dbModel = mapper.mapEntityToDbModel(category)
        val id = dao.addCategory(dbModel)
        return id
    }

    override suspend fun editCategory(category: Category) {
        val dbModel = mapper.mapEntityToDbModel(category)
        dao.addCategory(dbModel)
    }

    override suspend fun removeCategory(category: Category) {
        dao.removeCategory(category.id)
        refreshData()
    }
}