package com.thanhduc91tpk.twetterclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.thanhduc91tpk.twetterclone.R
import com.thanhduc91tpk.twetterclone.util.DATA_USERS
import com.thanhduc91tpk.twetterclone.util.DATA_USERS_EMAIL
import com.thanhduc91tpk.twetterclone.util.DATA_USERS_USERNAME
import com.thanhduc91tpk.twetterclone.util.User
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if(userId == null){
            finish()
        }
        profileProgressLayout.setOnTouchListener { view, motionEvent -> true }

        populateInfo()
    }

    fun populateInfo(){
        profileProgressLayout.visibility = View.VISIBLE
        firebaseDB.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener{documentSnapshot ->
                    val user = documentSnapshot.toObject(User::class.java)
//                    Toast.makeText(this,"this is ${user?.username} ",Toast.LENGTH_LONG).show()
                    usernameET.setText(user?.username,TextView.BufferType.EDITABLE)
                    emailET.setText(user?.email,TextView.BufferType.EDITABLE)
                    profileProgressLayout.visibility = View.GONE
            }
            .addOnFailureListener {e ->
                e.printStackTrace()
                finish()
            }
    }

    fun onApply(view: View) {
        profileProgressLayout.visibility = View.VISIBLE
        val username = usernameET.text.toString()
        val email = emailET.text.toString()
        val map = HashMap<String , Any>()
        map[DATA_USERS_USERNAME] = username
        map[DATA_USERS_EMAIL] = email

        firebaseDB.collection(DATA_USERS).document(userId!!).update(map)
            .addOnSuccessListener {
                Toast.makeText(this,"Update successful",Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
                Toast.makeText(this,"Update failed. Please try again.",Toast.LENGTH_SHORT).show()

                profileProgressLayout.visibility = View.GONE
            }
    }

    override fun onStart() {
        super.onStart()

    }

    fun onSignout(view: View) {
        firebaseAuth.signOut()
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ProfileActivity::class.java)
    }
}
