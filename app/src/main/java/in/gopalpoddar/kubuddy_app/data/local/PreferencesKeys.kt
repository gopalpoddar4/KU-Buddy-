package `in`.gopalpoddar.kubuddy_app.data.local

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val NAME = stringPreferencesKey("name")
    val EMAIL = stringPreferencesKey("email")
    val SEM = stringPreferencesKey("semester")
    val UNIVERSITY = stringPreferencesKey("university")
    val NOTICE = stringPreferencesKey("notice")
    val NOTICE_TIME = longPreferencesKey("notice_time")
    val STUDY_MATERIAL_TIME = longPreferencesKey("study_material_time")
}