package mp.iamuproject.model

data class Item(
    var _id: Long?,
    val title: String,
    val description: String,
    val picturePath: String,
    val date: String,
    val author: String,
    var read: Boolean,
    var guid: String,
    var url: String
)
