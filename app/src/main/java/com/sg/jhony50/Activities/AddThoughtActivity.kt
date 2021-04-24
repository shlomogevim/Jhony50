package com.sg.jhony50.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.jhony50.*
import kotlinx.android.synthetic.main.activity_add_thought.*

class AddThoughtActivity : AppCompatActivity() {
    var selectedCategory= FUNNY
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_thought)
    }
    fun post_Onclicked(view: View) {
            val data=HashMap<String,Any>()
        data.put(CATEGORY,selectedCategory)
        data.put(NUM_COMMENTS,0)
        data.put(NUM_LIKES,0)
        data.put(THOUGHT_TXT,addThoughtText.text.toString())
        data.put(USERNAME,FirebaseAuth.getInstance().currentUser.displayName.toString())
        data.put(TIMESTAMP,FieldValue.serverTimestamp())
      FirebaseFirestore.getInstance().collection(THOUGHTS_REF).add(data)
          .addOnSuccessListener {
              finish()
          }.addOnFailureListener { e->
              Log.e(TAG,"Can't connect to fire base because: ${e.localizedMessage}")

          }

    }

    fun addFunnyOnclicked(view: View) {
        addFunnyBtn.isChecked=true
        addSeriousBtn.isChecked=false
        addCrazyBtn.isChecked=false
        selectedCategory= FUNNY
    }
    fun addSeriousOnClick(view: View) {
        addFunnyBtn.isChecked=false
        addSeriousBtn.isChecked=true
        addCrazyBtn.isChecked=false
        selectedCategory= SERIOUS
    }

    fun addCrazyOnClicked(view: View) {
        addFunnyBtn.isChecked=false
        addSeriousBtn.isChecked=false
        addCrazyBtn.isChecked=true
        selectedCategory= CRAZY
    }





}