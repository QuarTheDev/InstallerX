package com.rosan.installer.data.settings.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.koin.core.component.KoinComponent

@Entity(
    tableName = "config",
    indices = [
    ]
)
data class ConfigEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "authorizer") var authorizer: Authorizer,
    @ColumnInfo(name = "customize_authorizer") var customizeAuthorizer: String,
    @ColumnInfo(name = "install_mode") var installMode: InstallMode,
    @ColumnInfo(name = "analyser") var analyser: Analyser,
    @ColumnInfo(name = "compat_mode") var compatMode: Boolean,
    @ColumnInfo(name = "installer") var installer: String?,
    @ColumnInfo(name = "for_all_user") var forAllUser: Boolean,
    @ColumnInfo(name = "allow_test_only") var allowTestOnly: Boolean,
    @ColumnInfo(name = "allow_downgrade") var allowDowngrade: Boolean,
    @ColumnInfo(name = "auto_delete") var autoDelete: Boolean,
    @ColumnInfo(name = "created_at") var createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "modified_at") var modifiedAt: Long = System.currentTimeMillis(),
) {
    companion object : KoinComponent {
        var default = ConfigEntity(
            name = "",
            description = "",
            authorizer = Authorizer.Global,
            customizeAuthorizer = "",
            installMode = InstallMode.Dialog,
            analyser = Analyser.R0s,
            compatMode = false,
            installer = null,
            forAllUser = false,
            allowTestOnly = false,
            allowDowngrade = false,
            autoDelete = false
        )
    }

    val isCustomizeAuthorizer: Boolean
        get() = authorizer == Authorizer.Customize

    enum class Authorizer(val value: String) {
        Global("global"),
        None("none"),
        Root("root"),
        Shizuku("shizuku"),
        Dhizuku("dhizuku"),
        Customize("customize");
    }

    enum class InstallMode(val value: String) {
        Dialog("dialog"),
        AutoDialog("auto_dialog"),
        Notification("notification"),
        AutoNotification("auto_notification"),
        Ignore("ignore");
    }

    enum class Analyser(val value: String) {
        R0s("r0s"),
        System("system");
    }
}
