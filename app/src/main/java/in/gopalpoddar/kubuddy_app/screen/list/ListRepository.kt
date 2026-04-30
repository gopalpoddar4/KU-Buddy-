package `in`.gopalpoddar.kubuddy_app.screen.list

import android.util.Log
import `in`.gopalpoddar.kubuddy_app.data.local.LocalDataSource
import `in`.gopalpoddar.kubuddy_app.data.model.ParentModel
import `in`.gopalpoddar.kubuddy_app.data.remote.RemoteDataSource
import `in`.gopalpoddar.kubuddy_app.db.dao.StudyMaterialDao
import `in`.gopalpoddar.kubuddy_app.db.mapToDomain
import `in`.gopalpoddar.kubuddy_app.db.mapToEntity

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