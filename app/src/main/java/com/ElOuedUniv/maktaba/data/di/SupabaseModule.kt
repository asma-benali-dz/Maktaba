package com.ElOuedUniv.maktaba.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://ppollwbvlgcedfsaeosd.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBwb2xsd2J2bGdjZWRmc2Flb3NkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzcyMTE5MTksImV4cCI6MjA5Mjc4NzkxOX0.iKesf-QYt8_AnwSHPU_KfiB5bBybHJqChAhrwtFcoYA"
        ) {
            install(Postgrest)
            install(Storage)
        }
    }
}