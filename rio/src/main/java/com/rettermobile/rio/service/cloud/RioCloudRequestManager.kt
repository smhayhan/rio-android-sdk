package com.rettermobile.rio.service.cloud

import com.google.gson.Gson
import com.rettermobile.rio.RioLogger
import com.rettermobile.rio.cloud.RioCloudObject
import com.rettermobile.rio.cloud.RioCloudObjectParams
import com.rettermobile.rio.cloud.RioGetCloudObjectOptions
import com.rettermobile.rio.cloud.RioServiceParam
import com.rettermobile.rio.model.RioError
import com.rettermobile.rio.service.model.RioInstanceResponse
import com.rettermobile.rio.util.RioActions
import com.rettermobile.rio.util.TokenManager

/**
 * Created by semihozkoroglu on 10.12.2021.
 */
object RioCloudRequestManager {

    private val cloudObjects = arrayListOf<RioCloudObject>()

    suspend fun exec(
        action: RioActions,
        options: RioGetCloudObjectOptions
    ): RioCloudObject {
        TokenManager.checkToken()

        val foundedObj = cloudObjects.find { it.params.instanceId == options.instanceId }
        return foundedObj?.let {
            RioLogger.log("RBSCloudManager.exec cloudObjects returned from list")
            it
        } ?: kotlin.run {
            if (options.useLocal && !options.instanceId.isNullOrEmpty()) {
                RioLogger.log("RBSCloudManager.exec create cloud object in-memory")
                RioCloudObject(
                    RioCloudObjectParams(
                        options.classId!!,
                        options.instanceId!!,
                        useLocal = true
                    )
                )
            } else {
                val accessToken = TokenManager.accessToken

                RioLogger.log("RBSCloudManager.exec create cloud object service call executed")

                val res = RioCloudServiceImp.exec(
                    accessToken,
                    action,
                    RioServiceParam(options)
                )

                if (res.isSuccess) {
                    res.getOrNull()?.let {
                        it.body()?.string()?.let { result ->
                            RioLogger.log("RBSCloudManager.exec success")

                            RioLogger.log("RBSCloudManager.exec result: $result")

                            val instanceRes = Gson().fromJson(result, RioInstanceResponse::class.java)

                            cloudObjects.find { it.params.instanceId == instanceRes.instanceId }
                                ?: kotlin.run {
                                    RioCloudObject(
                                        RioCloudObjectParams(
                                            options.classId!!,
                                            instanceRes.instanceId
                                        )
                                    ).apply {
                                        cloudObjects.add(this)
                                    }
                                }
                        } ?: it.errorBody()?.string()?.let { error ->
                            val errorRes = Gson().fromJson(error, RioError::class.java)

                            RioLogger.log(
                                "RBSCloudManager.exec fail ${
                                    errorRes.error ?: res.exceptionOrNull()?.stackTraceToString()
                                }"
                            )

                            throw res.exceptionOrNull() ?: IllegalAccessError(
                                "RBSCloudManager.exec fail ${
                                    errorRes.error ?: res.exceptionOrNull()?.stackTraceToString()
                                }"
                            )
                        } ?: kotlin.run {
                            RioLogger.log(
                                "RBSCloudManager.exec fail ${
                                    res.exceptionOrNull()?.stackTraceToString()
                                }"
                            )

                            throw res.exceptionOrNull() ?: IllegalAccessError("RBSCloudManager.exec fail")
                        }
                    } ?: kotlin.run {
                        RioLogger.log(
                            "RBSCloudManager.exec fail ${
                                res.exceptionOrNull()?.stackTraceToString()
                            }"
                        )

                        throw res.exceptionOrNull() ?: IllegalAccessError("RBSCloudManager.exec fail")
                    }
                } else {
                    RioLogger.log(
                        "RBSCloudManager.exec fail ${
                            res.exceptionOrNull()?.stackTraceToString()
                        }"
                    )

                    throw res.exceptionOrNull() ?: IllegalAccessError("RBSCloudManager.exec fail")
                }
            }
        }
    }

    fun clear() {
        cloudObjects.forEach { it.unsubscribeStates() }
        cloudObjects.clear()
    }
}