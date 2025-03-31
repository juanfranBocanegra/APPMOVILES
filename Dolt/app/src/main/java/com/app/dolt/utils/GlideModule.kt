package com.app.dolt.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        val diskCacheSize = 100 * 1024 * 1024 // 100 MB
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSize.toLong()))

        // Configuración de memoria caché
        builder.setMemoryCache(LruResourceCache((MemorySizeCalculator.Builder(context)
            .setMemoryCacheScreens(2F).build()).memoryCacheSize.toLong()))
    }
}
