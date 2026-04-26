package `in`.gopalpoddar.kubuddy.data.auth

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String): Result<Unit>{
        return try {
            auth.createUserWithEmailAndPassword(email,password).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun login(email: String,password: String): Result<Unit>{
        return try {
            auth.signInWithEmailAndPassword(email,password).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }



    suspend fun logout(): Result<Unit>{
        return try {
            auth.signOut()
            Result.success(Unit)
        }catch (e: kotlin.Exception){
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(password: String, onSuccess:()-> Unit,onError:(String)-> Unit){
        val user = auth.currentUser

        if (user != null && user.email != null){
            val credential = EmailAuthProvider.getCredential(user.email!!,password)

            user.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful){
                    user.delete().addOnCompleteListener {
                        if (it.isSuccessful){
                            onSuccess()
                        }else{
                            onError("${it.exception?.message}")
                        }
                    }
                }else{
                    onError("Wrong password! Try again.")
                }
            }
        }else{
            onError("User not logged In")
        }
    }
}