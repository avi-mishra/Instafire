package android.example.instafire

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_upload.*
import kotlin.contracts.contract

class UploadActivity : AppCompatActivity() {
    var dbRef=FirebaseFirestore.getInstance()
    var storageRef=FirebaseStorage.getInstance().reference
    var imageUrl:Uri?=null
    val firebaseUser= FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        val signedInUser= firebaseUser?.displayName?.let { User(it,20) }

        val result=registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback { uri->
            ivNewImage.setImageURI(uri)
            imageUrl=uri
            Log.d("imageUrl","$imageUrl")
        })
        Log.d("imageUri","$imageUrl")

        btnGallery.setOnClickListener {
            result.launch("image/*")
    }
        btnUpload.setOnClickListener {
            if(imageUrl!=null){
                val photoRef=storageRef.child("images/${System.currentTimeMillis()}-photo.jpg")
                photoRef.putFile(imageUrl!!).continueWithTask { photoUploadTask->
                    photoRef.downloadUrl
                }.continueWithTask { downloadUrlTask->
                    val post=Post(
                        etDescription.text.toString(),
                        System.currentTimeMillis(),
                        downloadUrlTask.result.toString(),
                        signedInUser
                    )
                    dbRef.collection("posts").add(post)
                }.addOnCompleteListener { postCreationTask->
                    if(!postCreationTask.isSuccessful) {
                        Toast.makeText(this@UploadActivity,"${postCreationTask.exception}",Toast.LENGTH_SHORT).show()
                    }
                    val i=Intent(this@UploadActivity,PostsActivity::class.java)
                    startActivity(i)
                }
            }
            else {
                Toast.makeText(this@UploadActivity,"Image cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
}