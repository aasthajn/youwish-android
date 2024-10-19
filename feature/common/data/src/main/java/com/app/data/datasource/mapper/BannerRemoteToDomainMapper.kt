package com.app.data.datasource.mapper

import com.app.core.utils.Mapper
import com.app.data.datasource.remote.model.banner.Banner
import com.app.data.datasource.remote.model.detail.Data
import com.app.domain.model.CardData
import javax.inject.Inject

class BannerRemoteToDomainMapper @Inject constructor() :
    Mapper<Banner, com.app.domain.model.Banner> {

    override suspend fun map(from: Banner): com.app.domain.model.Banner {
        return com.app.domain.model.Banner(
            url = from.url,
            position = from.position
        )
    }
}
