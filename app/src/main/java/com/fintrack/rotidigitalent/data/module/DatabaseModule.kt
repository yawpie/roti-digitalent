package com.fintrack.rotidigitalent.data.module

import android.app.Application
import android.content.Context
import com.fintrack.rotidigitalent.data.database.BreadDatabaseHelper
import com.fintrack.rotidigitalent.data.database.UserDatabaseHelper
import com.fintrack.rotidigitalent.data.preferences.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBreadDatabaseHelper(
        @ApplicationContext context: Context
    ): BreadDatabaseHelper {
        return BreadDatabaseHelper(context)
    }

    @Provides
    @Singleton
    fun provideUserDatabaseHelper(
        @ApplicationContext context: Context
    ): UserDatabaseHelper {
        return UserDatabaseHelper(context)
    }

    @Provides
    @Singleton
    fun provideSessionManager(application: Application): SessionManager {
        return SessionManager(application.applicationContext)
    }
}