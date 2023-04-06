package com.example.weather.location

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.weather.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DialogeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var selectedDialogIndex: Int = 0
    private val list = arrayOf("GPS", "Map")
    lateinit var se: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showAlertDialog()
        se = activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)!!
        return inflater.inflate(R.layout.fragment_dialoge, container, false)

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DialogeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }

    fun showAlertDialog() {

        var selectedLocation = list[selectedDialogIndex]
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Select Location")
            .setSingleChoiceItems(list, selectedDialogIndex) { dialog_, which ->
                selectedDialogIndex = which
                selectedLocation = list[which]
            }

            .setPositiveButton("Ok") { dialog, which ->
                if (selectedLocation.equals("GPS")) {
                    activity?.getSharedPreferences("My Shared", Context.MODE_PRIVATE)?.edit()
                        ?.apply {
                            putString("loc", "GPS")
                        }
                    val action =
                        DialogeFragmentDirections.actionDialogeFragmentToNavigationHome()
                    findNavController().navigate(action)
                } else {
                    val action =
                        DialogeFragmentDirections.actionDialogeFragmentToMapsFragment("Home")
                    findNavController().navigate(action)


                }
                Toast.makeText(requireActivity(), "$selectedLocation Selected", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
}

