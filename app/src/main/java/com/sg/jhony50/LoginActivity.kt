package com.sg.jhony50

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth= FirebaseAuth.getInstance()
    }

    fun loginLoginClicked(view: View) {
         /*val email=loginEmailTxt.text.toString()
         val password=loginPasswordTxt.text.toString()*/
        val email="shlomo10@gmail.com"
        val password="123456"
        Log.i(TAG,"inside loginActivity1 email=$email,password=$password")
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                Log.i(TAG,"inside loginActivity2 email=$email,password=$password")
                finish()
            }
            .addOnFailureListener { exception->
                Log.e(TAG,"Could not sign in usert:${exception.localizedMessage}")
            }
    }

    fun loginCreateUserClicked(view: View) {
        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
    }

}