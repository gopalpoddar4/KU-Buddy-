package `in`.gopalpoddar.kubuddy.data.user

import android.util.Log
import `in`.gopalpoddar.kubuddy.data.local.LocalDataSource
import `in`.gopalpoddar.kubuddy.data.model.User
import `in`.gopalpoddar.kubuddy.data.remote.RemoteDataSource
import `in`.gopalpoddar.kubuddy.db.dao.StudyMaterialDao

class UserRepository(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource,
    private val dao: StudyMaterialDao
) {

    suspend fun saveUserData(user: User): Result<Unit>{
        return try {
            remote.saveUser(user)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun deleteUserData(): Result<Unit>{
        val result = remote.deleteUserData()
        return result
    }

    suspend fun getUserData(): User{
        val localUser = local.getUser()

        if (localUser != null){
            return localUser
        }

        val remoteUser = remote.getUser()

        local.saveUser(remoteUser)

        return remoteUser
    }

    suspend fun getNotice(): String{
        val localNotice = local.getNoticeFromLocal()
        val noticeTime = local.getNoticeTime()
        val currentTime = System.currentTimeMillis()
        val diffTime = currentTime - noticeTime

        if (localNotice != null && diffTime <= 48*60*60*1000){
            return localNotice
        }

        local.addNoticeTime()
        val remoteNotice = remote.getNotice()
        local.saveNoticeInLocal(remoteNotice)
        Log.d("NOTICE",remoteNotice)
        return remoteNotice
    }

    suspend fun clearLocalData(){
        local.clear()
        dao.deleteStudyMaterial()
    }

}