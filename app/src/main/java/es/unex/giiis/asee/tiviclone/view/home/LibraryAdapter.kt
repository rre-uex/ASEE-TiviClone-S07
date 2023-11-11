package es.unex.giiis.asee.tiviclone.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.unex.giiis.asee.tiviclone.R
import es.unex.giiis.asee.tiviclone.databinding.LibraryItemListBinding
import es.unex.giiis.asee.tiviclone.data.model.Show
class LibraryAdapter(
    private var shows: List<Show>,
    private val onClick: (show: Show) -> Unit,
    private val onLongClick: (title: Show) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<LibraryAdapter.ShowViewHolder>() {

    class ShowViewHolder(
        private val binding: LibraryItemListBinding,
        private val onClick: (show: Show) -> Unit,
        private val onLongClick: (title: Show) -> Unit,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(show: Show, totalItems: Int) {
            with(binding) {
                tvTitle.text = show.title

                context?.let {
                    Glide.with(context)
                        .load(show.imagePath)
                        .placeholder(R.drawable.placeholder)
                        .into(itemImg)
                }

                clItem.setOnClickListener {
                    onClick(show)
                }
                clItem.setOnLongClickListener {
                    onLongClick(show)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding =
           LibraryItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding, onClick, onLongClick, context)
    }

    override fun getItemCount() = shows.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(shows[position], shows.size)
    }

    fun updateData(newShows: List<Show>) {
        shows = newShows
        notifyDataSetChanged()
    }

}