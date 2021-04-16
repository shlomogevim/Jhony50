package com.sg.jhony50

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sg.jhony50.AddThoughtActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var selectCategory:String
    lateinit var thoughtsAdapter: ThoughtsAdapter
     var thoughts=ArrayList<Thought>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        selectCategory= FUNNY
        fab.setOnClickListener {
            var intent = Intent(this, AddThoughtActivity::class.java)
            startActivity(intent)
        }
        thoughtsAdapter=ThoughtsAdapter(thoughts)
        thoughtListView.adapter=thoughtsAdapter
        val layoutManger=LinearLayoutManager(this)
        thoughtListView.layoutManager=layoutManger

    }

    fun mainFunnyOnClick(view: View) {
        mainFunnyBtn.isChecked=true
        mainSeriousBtn.isChecked=false
        mainCrazyBtn.isChecked=false
        mainPopularBtn.isChecked=false
        selectCategory= FUNNY
    }
    fun mainSeriousOnclicked(view: View) {
        mainFunnyBtn.isChecked=false
        mainSeriousBtn.isChecked=true
        mainCrazyBtn.isChecked=false
        mainPopularBtn.isChecked=false
        selectCategory= SERIOUS
    }
    fun mainCreazyOnclicked(view: View) {
        mainFunnyBtn.isChecked=false
        mainSeriousBtn.isChecked=false
        mainCrazyBtn.isChecked=true
        mainPopularBtn.isChecked=false
        selectCategory= CRAZY
    }
    fun mainPopularOnclicked(view: View) {
        mainFunnyBtn.isChecked=false
        mainSeriousBtn.isChecked=false
        mainCrazyBtn.isChecked=false
        mainPopularBtn.isChecked=true
        selectCategory= POPULAR
    }
}