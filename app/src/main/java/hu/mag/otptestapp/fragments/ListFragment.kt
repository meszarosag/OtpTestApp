package hu.mag.otptestapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import hu.mag.otptestapp.MainActivity
import hu.mag.otptestapp.R
import hu.mag.otptestapp.adapters.PhotosAdapter
import hu.mag.otptestapp.databinding.FragmentItemListBinding
import hu.mag.otptestapp.model.PhotosViewModel
import kotlinx.android.synthetic.main.fragment_item_list.*


class ListFragment : Fragment() {


    private val photosViewModel: PhotosViewModel by viewModels()
    private val photosAdapter = PhotosAdapter()

    private var _binding: FragmentItemListBinding? = null
    private var sharedPref : SharedPreferences? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var pageNumber = 1
    private var searchValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        loading.visibility = View.GONE;

        // Kereső mező enter lenyomás figyelése, majd a tartalomnak megfelelő adatlekérés

        searchText.setOnKeyListener(View.OnKeyListener { _ , keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                searchValue = searchText.text.toString()
                pageNumber = 1
                photosViewModel.loadData(searchValue, pageNumber )
                // bevitt kereső szöveg mentése a helyi tárolóba
                sharedPref?.edit()?.putString(getString(R.string.sPref_score_key), searchValue)?.apply()
                val imm = (activity as MainActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                return@OnKeyListener true
            }
            false
        })


        // Fotó lista létrehozása
        recyclerView.adapter = photosAdapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.setNestedScrollingEnabled(false);

        // A lista vége scroll figyelése, új adatok betölése
        nsv.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    addPhotoData();
                    loading.visibility = View.VISIBLE;
            }
        })

        // Kereső szöveg betöltése a helyi tárolóból
        searchValue = sharedPref?.getString(resources.getString(R.string.sPref_score_key), "dogs")!!

        // Adatok betöltése lista frissítése
        photosViewModel.loadData(searchValue, pageNumber)
        photosViewModel.photosLiveData.observe(viewLifecycleOwner,
            Observer { list ->
                with(photosAdapter) {
                    if(pageNumber == 1) photos.clear()
                    photos.addAll(list)
                    pageView = itemDetailFragmentContainer
                    notifyDataSetChanged()
                    loading.visibility = View.GONE;
                }
            })
    }

    private fun addPhotoData() {
        if (loading.visibility == View.GONE ){
            pageNumber++
            photosViewModel.loadData(searchValue, pageNumber)
        }
    }

    override fun onStart() {
        pageNumber = 1
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}