package com.example.lisbeth_p2_ap2

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.lisbeth_p2_ap2.data.GastoApi
import com.example.lisbeth_p2_ap2.data.repository.GastoRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun providesRandomUserApi(moshi: Moshi): GastoApi {
        return Retrofit.Builder()
            .baseUrl("https://sag-api.azurewebsites.net/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GastoApi::class.java)
    }
}

