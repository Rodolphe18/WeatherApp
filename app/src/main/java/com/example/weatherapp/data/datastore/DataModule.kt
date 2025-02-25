package com.example.weatherapp.data.datastore


import com.example.weatherapp.domain.DefaultUserDataRepository
import com.example.weatherapp.domain.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsUserDataRepository(defaultUserDataRepository: DefaultUserDataRepository): UserDataRepository

}


