package com.yawpie.rotidigitalent.data.module

import android.app.Application
import android.content.Context
import com.yawpie.rotidigitalent.data.database.BreadDatabaseHelper
import com.yawpie.rotidigitalent.data.database.UserDatabaseHelper
import com.yawpie.rotidigitalent.data.preferences.SessionManager
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