@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.*
import com.bumptech.glide.Glide.*
import com.example.myapplication.R.layout.display_image

class ImageDisplay : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var btnsubmit: Button
    private lateinit var imgView: ImageView
    private lateinit var tvComment: TextView
    private lateinit var comment : String
    private var title = ""
    private var link = ""
    private lateinit var emp: List<DataModelClass>
    private lateinit var databaseHandler: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(display_image)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back_black)// set drawable icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //calling the viewImageData method of DatabaseHandler class to read the records
        databaseHandler= DatabaseHandler(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            emp = databaseHandler.viewImageData()
        }

        //getting title and link from main activity
        title = intent.getStringExtra("title")
        link = intent.getStringExtra("link")

        //action bar with a title
        val actionBar = supportActionBar
        actionBar!!.title = title

        imgView = findViewById(R.id.iv_setimg)
        editText = findViewById(R.id.et_comment)
        btnsubmit = findViewById(R.id.bt_submit)
        tvComment = findViewById(R.id.tv_comment)

        //set image click from gridview
        with(this).load(link).asBitmap().placeholder(R.drawable.post).centerCrop().into(imgView)
        testlink()

        // submit comment listner
        btnsubmit.setOnClickListener{
            comment = editText.text.toString()
            saveRecord()
        }
    }

    //To set the comment below the image
    private fun testlink(){
        if(databaseHandler.checkComment(link))
            for (e in emp){
                if(e.link == link){
                    tvComment.visibility = View.VISIBLE
                    btnsubmit.visibility = GONE
                    editText.visibility = GONE
                    tvComment.text = e.comment
                }
            }
    }

    //method for saving records in database
    private fun saveRecord(){
        val databaseHandler = DatabaseHandler(this)
        if(comment.trim()!="" && title.trim()!="" && link.trim()!=""){
            val status = databaseHandler.addComment(DataModelClass(comment, title, link))
            if(status > -1){
                Toast.makeText(applicationContext,"Saved Your comment",Toast.LENGTH_LONG).show()
                tvComment.visibility = View.VISIBLE
                btnsubmit.visibility = GONE
                editText.visibility = GONE
                tvComment.text = comment
            }
        }else{
            Toast.makeText(applicationContext,"Please enter comment for image",Toast.LENGTH_LONG).show()
        }
    }

    // action bar with a back button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Here we can perform any action
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//    }
}

data class DataModelClass( var comment: String, var title: String, var link: String)


