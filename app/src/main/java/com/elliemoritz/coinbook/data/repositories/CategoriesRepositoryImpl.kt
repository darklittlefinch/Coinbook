package com.elliemoritz.coinbook.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.elliemoritz.coinbook.data.dao.CategoriesDao
import com.elliemoritz.coinbook.data.mappers.CategoryMapper
import com.elliemoritz.coinbook.domain.entities.Category
import com.elliemoritz.coinbook.domain.repositories.CategoriesRepository
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val dao: CategoriesDao,
    private val mapper: CategoryMapper
) : CategoriesRepository {

    override fun getCategoriesList(): LiveData<List<Category>> {
        val categoriesLiveData = dao.getCategoriesList()
        return categoriesLiveData.map {
            mapper.mapListDbModelToListEntities(it)
        }
    }

    override suspend fun getCategory(id: Int): Category {
        val dbModel = dao.getCategory(id)
        return mapper.mapDbModelToEntity(dbModel)
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