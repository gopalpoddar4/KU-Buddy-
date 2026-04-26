package `in`.gopalpoddar.kubuddy.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import `in`.gopalpoddar.kubuddy.data.local.PreferencesKeys.EMAIL
import `in`.gopalpoddar.kubuddy.data.local.PreferencesKeys.NAME
import `in`.gopalpoddar.kubuddy.data.local.PreferencesKeys.NOTICE
import `in`.gopalpoddar.kubuddy.data.local.PreferencesKeys.NOTICE_TIME
import `in`.gopalpoddar.kubuddy.data.local.PreferencesKeys.SEM
import `in`.gopalpoddar.kubuddy.data.local.PreferencesKeys.STUDY_MATERIAL_TIME
import `in`.gopalpoddar.kubuddy.data.local.PreferencesKeys.UNIVERSITY
import `in`.gopalpoddar.kubuddy.data.model.User
import kotlinx.coroutines.flow.first

class LocalDataSource (private val dataStore: DataStore<Preferences>){
    suspend fun saveUser(user: User){
        dataStore.edit {
            it[NAME] = user.name as String
            it[EMAIL] = user.email as String
            it[SEM] = user.semester as String
            it[UNIVERSITY] = user.university as String
        }
    }

    suspend fun getUser(): User?{
        val pref = dataStore.data.first()

        val name = pref[NAME]
        val email = pref[EMAIL]
        val semester = pref[SEM]
        val university = pref[UNIVERSITY]

        return if (name !=null && email!=null && semester!=null){
            User(name,email,semester,"",university)
        }
        else{
            null
        }
    }

    suspend fun saveNoticeInLocal(notice: String){
        dataStore.edit {
            it[NOTICE] = notice
        }
    }

    suspend fun addNoticeTime(){
        dataStore.edit {
            it[NOTICE_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun getNoticeTime(): Long{
        val pref = dataStore.data.first()
        val noticeTime = pref[NOTICE_TIME]
        return noticeTime?:5444
    }

    suspend fun addStudyMaterialTime(){
        dataStore.edit {
            it[STUDY_MATERIAL_TIME] = System.currentTimeMillis()
        }
    }

    suspend fun getStudyMaterialTime(): Long{
        val pref = dataStore.data.first()
        val studyMaterialTime = pref[STUDY_MATERIAL_TIME]
        return studyMaterialTime?:5444
    }
    suspend fun getNoticeFromLocal(): String{
        val pref = dataStore.data.first()
        val notice = pref[NOTICE]

        return if (notice != null  ){
            notice
        }else{
            "No notice"
        }
    }

    suspend fun clearNotice(){
        dataStore.edit {

        }
    }
    suspend fun clear(){
        dataStore.edit {
            it.clear()
        }
    }

}