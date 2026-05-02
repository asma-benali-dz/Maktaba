package com.ElOuedUniv.maktaba.data.repository

import android.util.Log
import com.ElOuedUniv.maktaba.data.model.Category
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseCategoryRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> = flow {
        try {
            // تغيير الاسم من Categories إلى Category بناءً على تلميح الخطأ في Logcat
            val response = supabaseClient.postgrest.from("Category").select()
            val categories = response.decodeList<Category>()
            emit(categories)
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Categories Error: ${e.message}")
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getCategoryById(id: String): Category? = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = supabaseClient.postgrest.from("Category")
                .select {
                    filter {
                        eq("id", id)
                    }
                }
            response.decodeSingleOrNull<Category>()
        } catch (e: Exception) {
            Log.e("SupabaseRepo", "Get Category Error: ${e.message}")
            null
        }
    }
}