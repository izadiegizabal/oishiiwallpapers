package xyz.izadi.oishiiwallpapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import xyz.izadi.oishiiwallpapers.data.api.UnsplashPhoto
import xyz.izadi.oishiiwallpapers.databinding.FragmentItemListDialogListDialogBinding

const val ARG_PHOTO = "photo"

class ItemListDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentItemListDialogListDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_item_list_dialog_list_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photo = arguments?.getParcelable<UnsplashPhoto>(ARG_PHOTO)
        binding.photo = photo
    }

    companion object {
        fun newInstance(photo: UnsplashPhoto): ItemListDialogFragment =
            ItemListDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PHOTO, photo)
                }
            }

    }
}