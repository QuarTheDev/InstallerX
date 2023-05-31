package com.rosan.installer.data.settings.util

import android.content.Context
import com.rosan.installer.data.settings.model.room.entity.AppEntity
import com.rosan.installer.data.settings.model.room.entity.ConfigEntity
import com.rosan.installer.data.settings.model.room.entity.converter.AuthorizerConverter
import com.rosan.installer.data.settings.repo.AppRepo
import com.rosan.installer.data.settings.repo.ConfigRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject

class ConfigUtil {
    companion object : KoinComponent {
        private val context by inject<Context>()

        private val sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

        suspend fun getByPackageName(packageName: String? = null): ConfigEntity {
            val entity = getByPackageNameInner(packageName)
            return if (entity.authorizer != ConfigEntity.Authorizer.Global)
                entity.copy()
            else entity.copy(
                authorizer = AuthorizerConverter.revert(sharedPreferences.getString("authorizer", null)),
                customizeAuthorizer =
                sharedPreferences.getString("customize_authorizer", null) ?: ""
            )
        }

        private suspend fun getByPackageNameInner(packageName: String? = null): ConfigEntity =
            withContext(Dispatchers.IO) {
                val repo = get<ConfigRepo>()
                val app = getAppByPackageName(packageName)
                var config: ConfigEntity? = null
                if (app != null) config = repo.find(app.configId)
                if (config != null) return@withContext config
                config = repo.all().firstOrNull()
                if (config != null) return@withContext config
                return@withContext ConfigEntity.default
            }

        private fun getAppByPackageName(packageName: String? = null): AppEntity? {
            val repo = get<AppRepo>()
            var app: AppEntity? = repo.findByPackageName(packageName)
            if (app != null) return app
            if (packageName != null) app = repo.findByPackageName(null)
            if (app != null) return app
            return null
        }
    }
}