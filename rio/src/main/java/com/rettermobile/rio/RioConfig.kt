package com.rettermobile.rio

import android.content.Context
import com.rettermobile.rio.service.RioNetworkConfig

/**
 * Created by semihozkoroglu on 10.12.2021.
 */
object RioConfig {
    lateinit var applicationContext: Context
    lateinit var projectId: String
    lateinit var culture: String

    var config = RioNetworkConfig()
}