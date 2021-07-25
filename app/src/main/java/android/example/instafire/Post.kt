package android.example.instafire

data class Post(
    var description:String="",
    var currentTimeMs:Long=0,
    var imageUrl:String="",
    var user: User?=null
)
