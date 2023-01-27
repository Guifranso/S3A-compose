package br.com.arcom.s3a.di

import android.content.Context
import br.com.arcom.s3a.database.dao.CronogramaDao
import br.com.arcom.s3a.database.db.AppDatabase
import br.com.arcom.s3a.database.db.DatabaseTransactionRunner
import br.com.arcom.s3a.database.db.RoomTransactionRunner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideCronogramaDao(appDatabase: AppDatabase): CronogramaDao {
        return appDatabase.cronogramaDao()
    }
}


@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseModuleBinds {

    @Singleton
    @Binds
    abstract fun provideDatabaseTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner
}