package com.sg.jhony50.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.sg.jhony50.R

class LoginActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= FirebaseAuth.getInstance()
    }

    fun loginLoginClicked(view: View) {
       /* val email=loginEmailTxt.text.toString()
        val password=loginPasswordTxt.text.toString()*/
        val email = "sh3@dev.com"
        val password = "123456"
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                finish()
            }.addOnFailureListener {
                Log.e(com.sg.jhony50.TAG,"Cannot get user: ${it.localizedMessage}")
            }
    }
    fun loginCreateUserClicked(view: View) {
        val intent=Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
    }
}