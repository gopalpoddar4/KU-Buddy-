package `in`.gopalpoddar.kubuddy.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.gopalpoddar.kubuddy.db.entity.StudyMaterialEntity

@Dao
interface StudyMaterialDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyMaterial(data: List<StudyMaterialEntity>)

    @Query("SELECT * FROM study_material WHERE type = :type AND semester = :semester")
    suspend fun getStudyMaterial(type: String, semester: String): List<StudyMaterialEntity>

    @Query("DELETE FROM study_material")
    suspend fun deleteStudyMaterial()
}