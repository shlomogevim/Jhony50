package com.sg.jhony50.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.sg.jhony50.*
import com.sg.jhony50.model.Thought
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var selectCategory: String
    lateinit var thoughtsAdapter: ThoughtsAdapter
    var thoughts = ArrayList<Thought>()
    val thoughtCollectionRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
    lateinit var thoughtsListener: ListenerRegistration
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectCategory = FUNNY
        fab.setOnClickListener {
            var intent = Intent(this, AddThoughtActivity::class.java)
            startActivity(intent)
        }
        thoughtsAdapter = ThoughtsAdapter(thoughts){thought->
            val intent=Intent(this,CommentsActivity::class.java)
            intent.putExtra(DOCUMENT_KEY,thought.documentId)
            startActivity(intent)

        }
        thoughtListView.adapter = thoughtsAdapter
        val layoutManger = LinearLayoutManager(this)
        thoughtListView.layoutManager = layoutManger
        auth = FirebaseAuth.getInstance()


    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.getItem(0)
        if (auth.currentUser == null) {
            menuItem.title = "Login"
        } else {
            menuItem.title = "Logout"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    fun updateUi() {
        if (auth.currentUser == null) {
            mainCrazyBtn.isEnabled = false
            mainFunnyBtn.isEnabled = false
            mainSeriousBtn.isEnabled = false
            mainPopularBtn.isEnabled = false
            fab.isEnabled = false
            thoughts.clear()
            thoughtsAdapter.notifyDataSetChanged()
        } else {
            mainCrazyBtn.isEnabled = true
            mainFunnyBtn.isEnabled = true
            mainSeriousBtn.isEnabled = true
            mainPopularBtn.isEnabled = true
            fab.isEnabled = true
            setListener()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_login) {
            if (auth.currentUser == null) {
                val inent = Intent(this, LoginActivity::class.java)
                startActivity(inent)
            } else {
                auth.signOut()
                updateUi()
            }
            return true
        }
        return false
    }

    fun setListener() {
        if (selectCategory == POPULAR) {
            thoughtsListener = thoughtCollectionRef
                .orderBy(NUM_LIKES, Query.Direction.DESCENDING)
                .addSnapshotListener(this) { snapshot, exception ->
                    if (exception != null) {
                        Log.i(TAG, "cant retrive data :${exception.localizedMessage}")
                    }
                    if (snapshot != null) {
                        parseData(snapshot)

                    }
                }

        } else {
            thoughtsListener = thoughtCollectionRef
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .whereEqualTo(CATEGORY, selectCategory)
                .addSnapshotListener(this) { snapshot, exception ->
                    if (exception != null) {
                        Log.i(TAG, "cant retrive data :${exception.localizedMessage}")
                    }
                    if (snapshot != null) {
                        parseData(snapshot)
                    }
                }
        }
    }

    fun parseData(snapshot: QuerySnapshot) {
        thoughts.clear()
        for (document in snapshot.documents) {
            val data = document.data
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
        }
    }

    fun mainFunnyOnClick(view: View) {
        mainFunnyBtn.isChecked = true
        mainSeriousBtn.isChecked = false
        mainCrazyBtn.isChecked = false
        mainPopularBtn.isChecked = false
        selectCategory = FUNNY
        thoughtsListener.remove()
        setListener()
    }

    fun mainSeriousOnclicked(view: View) {
        mainFunnyBtn.isChecked = false
        mainSeriousBtn.isChecked = true
        mainCrazyBtn.isChecked = false
        mainPopularBtn.isChecked = false
        selectCategory = SERIOUS
        thoughtsListener.remove()
        setListener()
    }

    fun mainCreazyOnclicked(view: View) {
        mainFunnyBtn.isChecked = false
        mainSeriousBtn.isChecked = false
        mainCrazyBtn.isChecked = true
        mainPopularBtn.isChecked = false
        selectCategory = CRAZY
        thoughtsListener.remove()
        setListener()
    }

    fun mainPopularOnclicked(view: View) {
        mainFunnyBtn.isChecked = false
        mainSeriousBtn.isChecked = false
        mainCrazyBtn.isChecked = false
        mainPopularBtn.isChecked = true
        selectCategory = POPULAR
        thoughtsListener.remove()
        setListener()
    }
}