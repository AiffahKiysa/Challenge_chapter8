@file:Suppress("RedundantNullableReturnType")

package com.example.challenge_chapter6_fix.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.challenge_chapter6_fix.R
import com.example.challenge_chapter6_fix.data.dao.FavoriteData
import com.example.challenge_chapter6_fix.databinding.FragmentDetailBinding
import com.example.challenge_chapter6_fix.viewModel.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : FavoriteViewModel
    private var isClicked = false
    private lateinit var data : FavoriteData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         viewModel = ViewModelProvider(
                    this)[FavoriteViewModel::class.java]
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = arguments?.getInt("id")
        if (id != null) {
            viewModel.isFav(id)
            viewModel.favorit.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it) {
                        isClicked = true
                        binding.btnFavorit.setImageResource(R.drawable.ic_baseline_favorite_24_red)
                    } else {
                        isClicked = false
                        binding.btnFavorit.setImageResource(R.drawable.ic_baseline_favorite_24)
                    }
                }
            }
            getData()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        }
        if (id != null) {
            viewModel.getMovie(id)
            viewModel.movieDetail.observe(viewLifecycleOwner) {
                binding.apply {
                    if (it != null) {
                        data = FavoriteData(it.id.toString().toInt(), it.title.toString(),
                            it.originalTitle.toString(), it.overview.toString(),
                            it.posterPath.toString()
                        )
                    }
                }
            }
        }
        binding.btnFavorit.setOnClickListener {
            if (!isClicked) {
                isClicked = true
                addFavorite(data)
                binding.btnFavorit.setImageResource(
                    R.drawable.ic_baseline_favorite_24_red)

            } else {
                isClicked = false
                deleteFavorite(data)
                binding.btnFavorit.setImageResource(
                    R.drawable.ic_baseline_favorite_24)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        val getoriginalTitle = arguments?.getString("originalTitle")
        val gettitle = "Title : " + arguments?.getString("title")
        val getposterPath = arguments?.getString("posterPath")
        val getrelease = "Release : " + arguments?.getString("releaseDate")
        val getpopularity = "Popularity : " + arguments?.getDouble("popularity")
        val getlanguage = "Language : " + arguments?.getString("language")
        val getoverview = arguments?.getString("overview")

        binding.ivPoster.load("https://www.themoviedb.org/t/p/w220_and_h330_face/$getposterPath") {
            crossfade(true)
            placeholder(R.drawable.ic_baseline_menu_24)
        }

        binding.originalTitle.text = getoriginalTitle
        binding.title.text = gettitle
        binding.releaseDate.text = getrelease
        binding.popularity.text = getpopularity
        binding.language.text = getlanguage
        binding.overview.text = getoverview
    }

    private fun addFavorite(favMovie: FavoriteData) {
        viewModel.addFavorit(favMovie)
        viewModel.favoritMovie.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(binding.root, "Film ditambahkan ke Favorit", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimary
                    ))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    .show()
            }
        }
    }

    private fun deleteFavorite(favMovie: FavoriteData) {
        viewModel.removeFavorit(favMovie)
        viewModel.deleteFavorit.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(binding.root, "Film dihapus dari Favorit", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimary
                    ))
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}