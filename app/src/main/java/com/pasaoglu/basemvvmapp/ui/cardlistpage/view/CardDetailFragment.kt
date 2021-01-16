package com.pasaoglu.basemvvmapp.ui.cardlistpage.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pasaoglu.basemvvmapp.R
import com.pasaoglu.basemvvmapp.databinding.CardDetailFragmentBinding
import com.pasaoglu.basemvvmapp.ui.cardlistpage.viewmodel.CardDetailViewModel
import com.pasaoglu.basemvvmapp.util.Status
import com.pasaoglu.basemvvmapp.util.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardDetailFragment : Fragment() {

    companion object {
        fun newInstance() = CardDetailFragment()
    }
    private var _binding: CardDetailFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: CardDetailViewModel by viewModels()
    private val args: CardDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CardDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setupObservers()
        val posterPath = args.posterPath
        binding.pokeCardImageView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                transitionName = posterPath
            }
            Glide.with(this)
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(this)
        }

        binding.pokeBackdropImageView.apply {
       /*     Glide.with(this)
                .load(args.backdropPath)
                .transform(CenterCrop())
                .into(this)*/
        }

        postponeEnterTransition()
        requireView().doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun setupObservers() {
        viewModel.getCardDetailById(cardId = args.cardId).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.pokeOverviewTextView.text =  resource.data?.card?.overview?.getOrNull(0) ?: ""
                        val tempGenresText = StringBuilder()
                        resource.data?.card?.attacks?.forEach {
                            genresModel ->
                            tempGenresText.appendLine( genresModel.name)
                        }
                        binding.pokeAttacksTextView.text = tempGenresText
                        binding.pokeHpTextView.text = resource.data?.card?.hp ?: ""
                    }
                    Status.ERROR -> {
                        if (!requireActivity().isNetworkAvailable()) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.no_internet_connection_offline_mode_cannot_work),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}