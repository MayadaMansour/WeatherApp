package com.example.weather.ui.favorite.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.Model.Reposatory
import com.example.weather.data.weather.local.ConcreteLocalSource
import com.example.weather.data.weather.netwok.Client
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.location.DialogeFragmentDirections
import com.example.weather.models.City
import com.example.weather.models.MyResponce
import com.example.weather.ui.favorite.view.FavoriteViewModel
import com.example.weather.ui.favorite.OnClick
import com.example.weather.ui.home.view.ViewModelFactory

class FavoriteFragment : Fragment(), OnClick {
    lateinit var myViewModel: FavoriteViewModel
    lateinit var myViewModelFactory: ViewModelFactory
    lateinit var favoriteAdapter: FavoriteAdapter
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //Factory
        myViewModelFactory = ViewModelFactory(
            Reposatory.getInstance(
                Client.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )

        myViewModel =
            ViewModelProvider(
                requireActivity(),
                myViewModelFactory
            )[FavoriteViewModel::class.java]

        favoriteAdapter = FavoriteAdapter(listOf(),this)


        //Observe Home Data
        myViewModel.favoriteWeather.observe(viewLifecycleOwner) {
            favoriteAdapter.setList(it)

            binding.recFav.adapter = favoriteAdapter
            binding.recFav.layoutManager = LinearLayoutManager(context)

            binding.addFab.setOnClickListener {
                val action =
                    DialogeFragmentDirections.actionDialogeFragmentToNavigationHome()
                Navigation.findNavController(it).navigate(action)

            }

        }

        return root
    }

    override fun sendData(weather: MyResponce) {
    }

    override fun deleteWeathers(city: City) {
        myViewModel.deleteWeathers(city)
    }
}