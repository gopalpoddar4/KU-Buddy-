package `in`.gopalpoddar.kubuddy.db

import `in`.gopalpoddar.kubuddy.data.model.ChildModel
import `in`.gopalpoddar.kubuddy.data.model.ParentModel
import `in`.gopalpoddar.kubuddy.db.entity.StudyMaterialEntity

fun mapToEntity(
    data: List<ParentModel>,
    type: String,
    semester: String
): List<StudyMaterialEntity>{

    val list = mutableListOf<StudyMaterialEntity>()

    data.forEach { parentModel ->
        parentModel.child?.forEach { childModel ->
            list.add(StudyMaterialEntity(
                type = type,
                semester = semester,
                parentName = parentModel.name?:"",
                childName = childModel.name,
                pdfKey = childModel.pdfKey
            ))
        }
    }
    return list
}


fun mapToDomain(data: List<StudyMaterialEntity>): List<ParentModel>{

    return data
        .groupBy { it.parentName }
        .map { (parentName,items)->
            ParentModel(
                name = parentName,
                child = items.map {
                    ChildModel(it.childName,it.pdfKey)
                }
            )
        }
}