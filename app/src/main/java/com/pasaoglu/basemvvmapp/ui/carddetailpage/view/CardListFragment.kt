package com.pasaoglu.basemvvmapp.ui.carddetailpage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import com.pasaoglu.basemvvmapp.R
import com.pasaoglu.basemvvmapp.data.model.CardListResponseModel
import com.pasaoglu.basemvvmapp.databinding.CardListFragmentBinding
import com.pasaoglu.basemvvmapp.ui.adapter.CardListAdapter
import com.pasaoglu.basemvvmapp.ui.carddetailpage.viewmodel.CardListViewModel
import com.pasaoglu.basemvvmapp.util.Status
import com.pasaoglu.basemvvmapp.util.isNetworkAvailable
import com.pasaoglu.basemvvmapp.util.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardListFragment : Fragment() {

    companion object {
        fun newInstance() = CardListFragment()
    }

    private var _binding: CardListFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: CardListViewModel by viewModels()
    private lateinit var cardListAdapter: CardListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CardListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        cardListAdapter = CardListAdapter(arrayListOf()) { cardItem, imageView->
            val action = CardListFragmentDirections.actionCardListFragmentToCardDetailFragment(
                cardId = cardItem.id,
                cardTitle = cardItem.name,
                posterPath = cardItem.getPosterImagePath())
            if(cardItem.getPosterImagePath() != null){
                val extras = FragmentNavigatorExtras(
                    imageView to cardItem.getPosterImagePath()!!
                )
                view?.findNavController()?.navigate(action, extras)
            }else{
                view?.findNavController()?.navigate(action)
            }
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                3
            )
            adapter = cardListAdapter
              postponeEnterTransition()
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
           /*TODO if there will be a load more list addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                        viewModel.getCardList()

                    }
                }
            })*/
        }
       /*TODO if there will be search
        binding.queryEditText.doOnTextChanged { inputText, _, _, _ ->
       //     viewModel.getSearchedPokesListWithQuery(query = inputText.toString(), isEditing = true)
        }*/
    }

    private fun setupObservers() {

        viewModel.cards.observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerView.visible(true)
                        binding.progressBar.visible(false)
                        resource.data?.cards?.let {
                            cards -> retrieveList(cards)
                        }

                    }
                    Status.ERROR -> {
                        if (!requireActivity().isNetworkAvailable()) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.no_internet_connection_offline_mode_cannot_work),
                                Toast.LENGTH_LONG
                            ).show()
                        } 
                        binding.recyclerView.visible(true)
                        binding.progressBar.visible(false)
                    }
                    Status.LOADING -> {
                        binding.progressBar.visible(true)
                        // binding.recyclerView.visible(false)
                    }
                    Status.EMPTY -> {
                        binding.progressBar.visible(false)
                        retrieveList(mutableListOf())
                    }
                }
            }
        })
            viewModel.getCardList()
    }

    private fun retrieveList(cardListResponse: List<CardListResponseModel>) {
        cardListAdapter.apply {
            addCards(cardListResponse)
            notifyDataSetChanged()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}