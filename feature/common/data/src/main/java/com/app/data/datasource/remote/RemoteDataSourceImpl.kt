package com.app.data.datasource.remote

import com.app.data.datasource.remote.model.banner.BannerResponse
import com.app.data.datasource.remote.model.category.CategoryResponse
import com.app.data.datasource.remote.model.detail.CardDetails
import com.app.data.datasource.remote.model.list.CardsResponse
import com.app.data.network.ApiService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    RemoteDataSource {
    override suspend fun getTrendingCards(): CardsResponse {
        return apiService.getTrendingCards()
    }

    override suspend fun getCardDetails(cardId: String): CardDetails {
        return apiService.getCardDetails(cardId)
    }

    override suspend fun getBanners(): BannerResponse {
        return apiService.getBannerCards()
    }

    override suspend fun getCategories(): CategoryResponse {
        return apiService.getCategories()
    }

}

