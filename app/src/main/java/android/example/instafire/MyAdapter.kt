package android.example.instafire

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout1.view.*

class MyAdapter(var context: Context,var posts:ArrayList<Post>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView=LayoutInflater.from(parent.context).inflate(R.layout.layout1,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tvUserName.text= posts[position].user?.username
        holder.itemView.tvUploadTime.text=DateUtils.getRelativeTimeSpanString(posts[position].currentTimeMs)
        Picasso.get().load(posts[position].imageUrl).into(holder.itemView.ivPost)
        holder.itemView.tvDesciption.text=posts[position].description
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}