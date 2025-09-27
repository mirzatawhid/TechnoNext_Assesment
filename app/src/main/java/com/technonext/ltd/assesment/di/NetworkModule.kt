package com.technonext.ltd.assesment.di

import android.app.Application
import androidx.room.Room
import com.technonext.ltd.assesment.data.local.post.PostDao
import com.technonext.ltd.assesment.data.local.post.PostDatabase
import com.technonext.ltd.assesment.data.remote.PostApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApi(): PostApi = Retrofit.Builder()
        .baseUrl("https://my-json-server.typicode.com/mirzatawhid/assessment_posts_db/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PostApi::class.java)

    @Provides
    @Singleton
    fun provideDb(app: Application): PostDatabase =
        Room.databaseBuilder(app, PostDatabase::class.java, "posts.db").fallbackToDestructiveMigration()   // 🚨 This will clear DB on version change
            .build()

    @Provides
    fun provideDao(db: PostDatabase): PostDao = db.postDao()

}
