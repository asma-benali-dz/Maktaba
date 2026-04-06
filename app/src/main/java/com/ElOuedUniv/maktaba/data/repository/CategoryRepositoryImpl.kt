package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor() : CategoryRepository {

    private val _categoriesList = listOf(
        Category(
            id = "1",
            name = "Programming",
            description = "Books about software development and coding"
        ),
        Category(
            id = "2",
            name = "Algorithms",
            description = "Books about algorithms and data structures"
        ),
        Category(
            id = "3",
            name = "Databases",
            description = "Books about database design and management"
        ),
        Category(
            id="4",
            name="Machine Learning",
            description="Books about machine learning, AI, and data science"
        ),
        Category(
            id="5",
            name="Web Development",
            description="Books about building websites and web applications"
        ),
        Category(
            id="6",
            name="Cybersecurity",
            description="Books about network security, ethical hacking, and cyber defense"
        )

    )

    private val categoriesFlow = MutableSharedFlow<List<Category>>(replay = 1).apply {
        tryEmit(_categoriesList)
    }
    
    override fun getAllCategories(): Flow<List<Category>> = flow {
        delay(2000) // Simulate delay
        emitAll(categoriesFlow)
    }

    override fun getCategoryById(id: String): Category? {
        return _categoriesList.find { it.id == id }
    }
}
