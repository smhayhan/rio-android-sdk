package com.rbs.android.example

import android.app.Application
import com.readystatesoftware.chuck.ChuckInterceptor
import com.rettermobile.rio.Rio
import com.rettermobile.rio.service.RioNetworkConfig
import com.rettermobile.rio.util.RioRegion

/**
 * Created by semihozkoroglu on 7.08.2021.
 */
class App : Application() {

    lateinit var rio: Rio

    override fun onCreate() {
        super.onCreate()

        rio = Rio(
            applicationContext = applicationContext,
//            projectId = "69ec1ef0039b4332b3e102f082a98ec2",
            projectId = "11c5e84qtq",
            culture= "tr",
            config = RioNetworkConfig.build {
//                region = RioRegion.EU_WEST_1_BETA
                sslPinningEnabled = false
            }
        )
    }
}