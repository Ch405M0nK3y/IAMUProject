package mp.iamuproject.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mp.iamuproject.HN_PROVIDER_CONTENT_URI
import mp.iamuproject.R
import mp.iamuproject.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(
    private val context: Context,
    private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvAuthor = itemView.findViewById<TextView>(R.id.tvAuthor)
        fun bind(item: Item) {
            tvItem.text = item.title
            tvDate.text = item.date
            tvDescription.text = item.description
            tvAuthor.text = item.author
            ivRead.setImageResource(
                if(item.read) R.drawable.green_flag else R.drawable.red_flag
            )
            Picasso.get()
                .load(File(item.picturePath))
                .error(R.drawable.hacker)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pager, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivRead.setOnClickListener {
            updateItem(position)
        }
        holder.bind(items[position])
    }

    private fun updateItem(position: Int) {
        val item = items[position]
        item.read = !item.read
        context.contentResolver.update(
            ContentUris.withAppendedId(HN_PROVIDER_CONTENT_URI, item._id!!),
            ContentValues().apply {
                put(Item::read.name, item.read)
            },
            null,
            null
        )
        notifyItemChanged(position)
    }
}