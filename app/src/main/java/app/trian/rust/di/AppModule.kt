package app.trian.rust.di

import android.content.Context
import android.content.SharedPreferences
import app.trian.rust.data.dataSource.local.MainDatabase
import app.trian.rust.model.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideLocalDatabase(
        @ApplicationContext appContext: Context,
    ): MainDatabase = MainDatabase.getInstance(appContext)

    @Provides
    fun provideSharedPref(
        @ApplicationContext appContext: Context,
    ): SharedPreferences = appContext.getSharedPreferences("fcda-4adb", Context.MODE_PRIVATE)

    @Provides
    fun provideSession(
        sharedPreferences: SharedPreferences,
    ): Session = Session.from(sharedPreferences)
}