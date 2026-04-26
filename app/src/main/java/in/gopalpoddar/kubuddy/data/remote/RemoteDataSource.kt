package `in`.gopalpoddar.kubuddy.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import `in`.gopalpoddar.kubuddy.data.model.ChildModel
import `in`.gopalpoddar.kubuddy.data.model.ParentModel
import `in`.gopalpoddar.kubuddy.data.model.User
import kotlinx.coroutines.tasks.await

class RemoteDataSource(private val auth: FirebaseAuth, private val db: FirebaseDatabase) {
    suspend fun getUser(): User{
        val uid = auth.currentUser?.uid?:throw Exception("Not logged in")

        val snapshot = db.reference
            .child("users")
            .child(uid)
            .get()
            .await()

        return snapshot.getValue(User::class.java) ?: throw Exception("No data")
    }

    suspend fun saveUser(user: User) {
        val uid = auth.currentUser?.uid ?: throw Exception("No user")

        db.reference
            .child("users")
            .child(uid)
            .setValue(user)
            .await()
    }

    suspend fun deleteUserData(): Result<Unit>{
        val uid = auth.currentUser?.uid ?: throw Exception("No user")

        return try {

            db.reference
                .child("users")
                .child(uid)
                .removeValue()
                .await()

            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }

    }
    suspend fun getNotice(): String{
        val snapshot = db.reference
            .child("notice")
            .get()
            .await()

        return snapshot.getValue(String::class.java)?:"No notice"
    }

    suspend fun getStudyMaterial(materialType: String,semester: String): List<ParentModel>{

        val snapshot = db.reference
            .child(materialType)
            .child(semester)
            .get()
            .await()

        val parentList = mutableListOf<ParentModel>()

        for (parentSnapshot in snapshot.children){
            val parentName = parentSnapshot.key?:""
            val childList = mutableListOf<ChildModel>()

            for (childSnapshot in parentSnapshot.children){
                val childName = childSnapshot.key?:""

                val pdfUrl = childSnapshot
                    .child("pdfurl")
                    .getValue(String::class.java)?:""

                childList.add(ChildModel(childName,pdfUrl))
            }
            parentList.add(ParentModel(parentName,childList))
        }

        return parentList
    }
}