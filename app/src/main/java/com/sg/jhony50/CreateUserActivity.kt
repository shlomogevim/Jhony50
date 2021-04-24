package com.sg.jhony50

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        auth= FirebaseAuth.getInstance()
    }

    fun createCreateClicked(view: View) {
        val email = createEmailTxt.text.toString()
        val password = cratePasswordText.text.toString()
        val username = createUsernameTxt.text.toString()
        Log.i(TAG,"email=$email ,password=$password ,username=$username")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                Log.e(TAG, "insid1: ${result}")
                val changeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                Log.e(TAG, "insid2: ${result}")
                result.user.updateProfile(changeRequest)
                    .addOnFailureListener { exception ->
                        Log.e( TAG,"could not update display name: ${exception.localizedMessage}"
                        )
                    }
                val data = HashMap<String, Any>()
                data[USERNAME] = username
                data[DATE_CREATED] = FieldValue.serverTimestamp()

                FirebaseFirestore.getInstance().collection(USERS_REF).document(result.user.uid)
                    .set(data)
                    .addOnSuccessListener {
                        finish()
                    }
                    .addOnFailureListener { exception ->
                       Log.i( TAG,"could not add user document: ${exception.localizedMessage}")
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("exception", "could not create user: ${exception.localizedMessage}")
            }
    }
    fun createCancelClicked(view: View) {
        finish()
    }
}