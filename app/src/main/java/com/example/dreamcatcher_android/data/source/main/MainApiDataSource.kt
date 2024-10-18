package com.example.dreamcatcher_android.data.source.main

import com.example.dreamcatcher_android.data.remote.MainApi
import javax.inject.Inject

class MainApiDataSource @Inject constructor(
    private val mainApi: MainApi
) {

    //    fun postSignUpInfo(
//        userDto: RequestBody,
//        profileImage: MultipartBody.Part
//    ): Flow<BaseResponse<UserResponse>> = flow {
//        try {
//            val result = mainApi.postSignUpInfo(userDto, profileImage)
//            emit(result)
//        } catch (e: HttpException) {
//            val errorResponse = e.response()?.let { it }
//            Log.e("Get Community MyScraps Failure", "HTTP Error: ${errorResponse?.errorBody()?.string()}")
//
//            emit(BaseResponse(
//                isSuccess = errorResponse!!.isSuccessful,
//                code = errorResponse.code().toString(),
//                message = errorResponse.message().toString(),
//                result =null)
//            )
//        }
//    }

}