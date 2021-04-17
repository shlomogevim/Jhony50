package com.sg.jhony50

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sg.jhony50.AddThoughtActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var selectCategory: String
    lateinit var thoughtsAdapter: ThoughtsAdapter
    var thoughts = ArrayList<Thought>()
    val thoughtCollectionRef=FirebaseFirestore.getInstance().collection(THOUGHTS_REF)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectCategory = FUNNY
        fab.setOnClickListener {
            var intent = Intent(this, AddThoughtActivity::class.java)
            startActivity(intent)
        }
        thoughtsAdapter = ThoughtsAdapter(thoughts)
        thoughtListView.adapter = thoughtsAdapter
        val layoutManger = LinearLayoutManager(this)
        thoughtListView.layoutManager = layoutManger

        thoughtCollectionRef.get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val data =document.data
                    Log.i(TAG,"${data}")
                    if (data != null) {
                        val name = data[USERNAME] as String
                        val timestamp = data[TIMESTAMP] as Timestamp
                        var thoghtTxt = "ff"
                        if (data[THOUGHT_TXT] != null) {
                            thoghtTxt = data[THOUGHT_TXT] as String
                        }
                        val numLikes = data[NUM_LIKES] as Long
                        Log.i("message", "numLikes=$numLikes")
                        val numComments = data[NUM_COMMENTS] as Long
                        val documentId = document.id
                        val newThought = Thought(
                            name, timestamp, thoghtTxt, numLikes.toInt(),
                            numComments.toInt(), documentId
                        )
                        thoughts.add(newThought)
                    }
 thoughtsAdapter.notifyDataSetChanged()
                    Log.i(TAG,"end")
                }

            }.addOnFailureListener { e->

                Log.i(TAG,"Canot retrive data because :${e.localizedMessage}")

            }

    }

    fun mainFunnyOnClick(view: View) {
        mainFunnyBtn.isChecked = true
        mainSeriousBtn.isChecked = false
        mainCrazyBtn.isChecked = false
        mainPopularBtn.isChecked = false
        selectCategory = FUNNY
    }

    fun mainSeriousOnclicked(view: View) {
        mainFunnyBtn.isChecked = false
        mainSeriousBtn.isChecked = true
        mainCrazyBtn.isChecked = false
        mainPopularBtn.isChecked = false
        selectCategory = SERIOUS
    }

    fun mainCreazyOnclicked(view: View) {
        mainFunnyBtn.isChecked = false
        mainSeriousBtn.isChecked = false
        mainCrazyBtn.isChecked = true
        mainPopularBtn.isChecked = false
        selectCategory = CRAZY
    }

    fun mainPopularOnclicked(view: View) {
        mainFunnyBtn.isChecked = false
        mainSeriousBtn.isChecked = false
        mainCrazyBtn.isChecked = false
        mainPopularBtn.isChecked = true
        selectCategory = POPULAR
    }
}