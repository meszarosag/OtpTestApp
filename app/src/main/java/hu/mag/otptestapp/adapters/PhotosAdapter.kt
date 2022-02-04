package hu.mag.otptestapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hu.mag.otptestapp.R
import hu.mag.otptestapp.datas.Photo
import kotlinx.android.synthetic.main.photo.view.imageView

/*
    Fotó lista elemit megjelenítő osztály
 */

class PhotosAdapter(val photos: MutableList<Photo> = mutableListOf(), var pageView: View? = null) : RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        return PhotosViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.photo,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val item = photos[position]
        holder.bind(photos[position])

        with(holder.itemView) {
            tag = item

            // Kattintásra adatok átadása a részletek felületre

            setOnClickListener { itemView ->
                //val item = itemView.tag as PlaceholderContent.PlaceholderItem
                val bundle = Bundle()
                bundle.putString( "item_id", item.id )
                bundle.putString( "item_url", item.url )
                bundle.putString( "item_title", item.title )
                bundle.putString( "item_description", item.description )
                // Átnavigálás a részletek felületre
                if (pageView != null) {
                    pageView!!.findNavController()
                        .navigate(R.id.fragment_item_detail, bundle)
                } else {
                    itemView.findNavController().navigate(R.id.show_item_detail, bundle)
                }
            }
        }
    }

    inner class PhotosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photo) {
            //Fotó elemek betöltése a listának az url változójából - teljes kitöltés - középre helyezés és a nem látható részek vágásával
            Picasso.get().
            load(photo.url)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(itemView.imageView)
        }
    }
}

