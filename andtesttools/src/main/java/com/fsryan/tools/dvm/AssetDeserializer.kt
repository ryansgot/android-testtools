package com.fsryan.tools.dvm

import android.content.Context
import androidx.test.InstrumentationRegistry.getContext
import androidx.test.InstrumentationRegistry.getTargetContext
import java.io.InputStream

object AssetDeserializer {
    fun <T> deserializeTargetAsset(assetFile: String, deserialize: (InputStream) -> T): T = deserialize(streamOf(assetFile, getTargetContext()))
    fun <T> deserializeAsset(assetFile: String, deserialize: (InputStream) -> T): T = deserialize(streamOf(assetFile, getContext()))
    private fun streamOf(assetFile: String, context: Context): InputStream = context.assets.open(assetFile)
}