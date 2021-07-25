package android.example.instafire

import android.content.Intent
import android.content.QuickViewConstants
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_posts.*

class PostsActivity : AppCompatActivity() {
    var dbRef=FirebaseFirestore.getInstance()
    var posts=ArrayList<Post>()
    lateinit var adapter:MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        adapter=MyAdapter(this,posts)

        var postRef=dbRef.collection("posts")
            .orderBy("currentTimeMs",Query.Direction.DESCENDING)
        postRef.addSnapshotListener { snapshot, exception ->
            if(exception!=null || snapshot==null) {
                Toast.makeText(this@PostsActivity,"Error while retrieving post data",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            var postList= snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()

        }
        var userRef=dbRef.collection("users")
        userRef.addSnapshotListener { snapshot, exception ->
            if(exception!=null || snapshot==null) {
                Toast.makeText(this@PostsActivity,"Error while retrieving user data",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            val userList=snapshot.toObjects(User::class.java)
        }
        rvPosts.layoutManager=LinearLayoutManager(this)
        rvPosts.adapter=adapter

        fbAdd.setOnClickListener {
            val intent=Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.navSignOut) {
            Toast.makeText(this@PostsActivity,"Signed Out",Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            val i= Intent(this@PostsActivity,MainActivity::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }
}