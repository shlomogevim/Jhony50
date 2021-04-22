package com.sg.jhony50.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.jhony50.DATE_CREATED
import com.sg.jhony50.R
import com.sg.jhony50.USERNAME
import com.sg.jhony50.USERS_REF

class CreateUserActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        auth = FirebaseAuth.getInstance()
    }

    fun createCreateClicked(view: View) {
       /* val email = createEmailTxt.text.toString()
        val password = createPasswordText.text.toString()
        val username = createUsernameTxt.text.toString()*/
        val email = "sh3@dev.com"
        val password = "123456"
        val username = "shlomo3"
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val changeRequest=UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                result.user.updateProfile(changeRequest)
                    .addOnSuccessListener {

                    }.addOnFailureListener {
                        Log.i(com.sg.jhony50.TAG,"could not update display:${it.localizedMessage} ")
                    }
                val data=HashMap<String,Any>()
                data.put(USERNAME,username)
                data.put(DATE_CREATED,FieldValue.serverTimestamp())
                FirebaseFirestore.getInstance().collection(USERS_REF).document(result.user.uid)
                    .set(data)
                    .addOnSuccessListener {
                        finish()
                    }.addOnFailureListener {
                        Log.e(com.sg.jhony50.TAG,"cannot create user document :${it.localizedMessage}")
                    }

            }.addOnFailureListener {
                Log.i(com.sg.jhony50.TAG, "could not create user because:${it.localizedMessage}")
            }
    }

    fun createCancelClicked(view: View) {
        finish()
    }
}