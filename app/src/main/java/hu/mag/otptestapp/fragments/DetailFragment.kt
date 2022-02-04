package hu.mag.otptestapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import hu.mag.otptestapp.R
import hu.mag.otptestapp.databinding.FragmentItemDetailBinding
import hu.mag.otptestapp.datas.Photo
import kotlinx.android.synthetic.main.fragment_item_detail.view.*


// Fotók adatainak részletezése

class DetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null

    private val binding get() = _binding!!
    private lateinit var photoData: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                photoData = Photo(it.getString("item_id")!!, it.getString("item_url")!!, it.getString("item_title")!!, it.getString("item_description")!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root
        // Megjegyzés szöveg kiíratása
        rootView.detail_description.text = photoData.description

        // Fotó betöltése és nagyítása
        Picasso.get()
            .load(photoData.url)
            .fit()
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(rootView.detail_image)
        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}