package com.sg.jhony50.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.jhony50.*
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {
    lateinit var thoughtDocumentID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentID=intent.getStringExtra(DOCUMENT_KEY)

    }

    fun addCommentClick(view: View) {
        val commentTxt=enterCommentText.text.toString()
        val thoughtRef=FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentID)

        FirebaseFirestore.getInstance().runTransaction { transaction->
            val thought=transaction.get(thoughtRef)
            val numCommentTxt = thought.getLong(NUM_COMMENTS)?.plus(1)
            transaction.update(thoughtRef, NUM_COMMENTS, numCommentTxt)

            val newCommentRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
                .document(thoughtDocumentID).collection(COMMENTS_REF).document()
            val data = HashMap<String, Any>()
            data.put(COMMENTS_TXT, commentTxt)
            data.put(TIMESTAMP, FieldValue.serverTimestamp())
            data.put(USERNAME, FirebaseAuth.getInstance().currentUser.displayName.toString())
            transaction.set(newCommentRef, data)
        }.addOnSuccessListener {
            enterCommentText.setText("")
            hideKeyboard()
        }
            .addOnFailureListener { exception ->
                Log.i("abc", "could not add comment:${exception.localizedMessage}")
            }
        }
    private fun hideKeyboard(){
        val inptManagar=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inptManagar.isAcceptingText){
            inptManagar.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
    }
