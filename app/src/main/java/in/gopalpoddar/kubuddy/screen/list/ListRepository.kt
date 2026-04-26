package `in`.gopalpoddar.kubuddy.screen.list

import android.util.Log
import `in`.gopalpoddar.kubuddy.data.local.LocalDataSource
import `in`.gopalpoddar.kubuddy.data.model.ParentModel
import `in`.gopalpoddar.kubuddy.data.remote.RemoteDataSource
import `in`.gopalpoddar.kubuddy.db.dao.StudyMaterialDao
import `in`.gopalpoddar.kubuddy.db.mapToDomain
import `in`.gopalpoddar.kubuddy.db.mapToEntity

class ListRepository (
    private val remoteDataSource: RemoteDataSource,
    private val materialType: String,
    private val semester: String,
    private val dao: StudyMaterialDao,
    private val localDataSource: LocalDataSource
){
    suspend fun getStudyMaterial(): List<ParentModel>{
        val localData = dao.getStudyMaterial(type = materialType, semester = semester)

        val saveTime = localDataSource.getStudyMaterialTime()
        val currentTime = System.currentTimeMillis()
        val diffTime = currentTime-saveTime

        if (localData.isEmpty() || diffTime>=4*60*60*1000){
            val remoteData = remoteDataSource.getStudyMaterial(materialType,semester)

            val entityList = mapToEntity(remoteData,materialType,semester)

            dao.insertStudyMaterial(entityList)
            localDataSource.addStudyMaterialTime()
            Log.d("STUDYMATERIAL","FROM REMOTE")
            return remoteData
        }
        Log.d("l","FROM LOCAL")
        return mapToDomain(localData)

    }
}