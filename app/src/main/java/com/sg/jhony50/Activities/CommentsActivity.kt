package com.sg.jhony50.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sg.jhony50.*
import com.sg.jhony50.Adapters.CommentsAdapter
import com.sg.jhony50.Model.Comment
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {
    lateinit var thoughtDocumentId:String
    val comments = arrayListOf<Comment>()
    lateinit var commentsAdapter:CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        thoughtDocumentId=intent.getStringExtra(DOCUMENT_KEY)

        commentsAdapter= CommentsAdapter(comments)
        commentsListview.adapter=commentsAdapter
        val layoutManager=LinearLayoutManager(this)
        commentsListview.layoutManager=layoutManager

    }

    fun addCommentClick(view: View) {
        val commentText = enterCommentText.text.toString()

        val thoughtRef =
            FirebaseFirestore.getInstance().collection(THOUGHTS_REF).document(thoughtDocumentId)

        FirebaseFirestore.getInstance().runTransaction { transaction ->
            val thought = transaction.get(thoughtRef)
           val numComments = thought.getLong(NUM_COMMENTS)?.plus(1)
            transaction.update(thoughtRef, NUM_COMMENTS, numComments)

            val newCommentRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
                .document(thoughtDocumentId).collection(COMMENTS_REF).document()
            val data = HashMap<String, Any>()
            data.put(COMMENTS_TXT, commentText)
            data.put(TIMESTAMP, FieldValue.serverTimestamp())
            data.put(USERNAME, FirebaseAuth.getInstance().currentUser.displayName.toString())
            transaction.set(newCommentRef, data)
        }.addOnSuccessListener {
            enterCommentText.setText("")
            hideKeyboard()
        }
            .addOnFailureListener { exception ->
                Log.i(TAG, "could not add comment:${exception.localizedMessage}")
            }
    }
    private fun hideKeyboard(){
        val inptManagar=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inptManagar.isAcceptingText){
            inptManagar.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }
}