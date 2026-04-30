package `in`.gopalpoddar.kubuddy_app.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_material")
data class StudyMaterialEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val type: String,
    val semester: String,
    val parentName: String,
    val childName: String,
    val pdfKey: String
)
