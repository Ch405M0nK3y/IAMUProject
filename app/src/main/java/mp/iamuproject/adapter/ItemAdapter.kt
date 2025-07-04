package mp.iamuproject.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import mp.iamuproject.ITEM_POSITION
import mp.iamuproject.ItemPagerActivity
import mp.iamuproject.HN_PROVIDER_CONTENT_URI
import mp.iamuproject.R
import mp.iamuproject.framework.startActivity
import mp.iamuproject.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import mp.iamuproject.channel.sendNotification
import java.io.File

class ItemAdapter(
    private val context: Context,
    private val items: MutableList<Item>)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        fun bind(item: Item) {
            tvItem.text = item.title
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
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.setOnClickListener {
            context.startActivity<ItemPagerActivity>(ITEM_POSITION, position)
        }

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(context.getString(R.string.delete))
                setMessage(context.getString(R.string.sure_to_delete))
                setIcon(R.drawable.delete)
                setCancelable(true)
                setPositiveButton("OK") { _, _ -> deleteItem(position)}
                setNegativeButton(R.string.cancel, null)
                show()
            }
            true
        }
        holder.bind(item)
    }

    private fun deleteItem(position: Int) {
        val item = items[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(HN_PROVIDER_CONTENT_URI, item._id!!),
            null,
            null
        )
        items.removeAt(position)
        File(item.picturePath).delete()
        notifyItemRemoved(position)
        sendNotification(context, "Post was deleted", item.title)
    }
}