package com.kwmax.onemore.http

import com.kwmax.onemore.base.BaseResponse
import com.kwmax.onemore.beans.UserEntity
import retrofit2.http.GET


/**
 * created by kwmax on 2020/1/9
 * desc:
 */
interface ApiService {

    @GET("/login")
    suspend fun login() : BaseResponse<UserEntity>
}