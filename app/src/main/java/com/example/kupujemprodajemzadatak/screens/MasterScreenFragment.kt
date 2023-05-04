package com.example.kupujemprodajemzadatak.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kupujemprodajemzadatak.R
import com.example.kupujemprodajemzadatak.utils.setViewInvisible
import com.example.kupujemprodajemzadatak.utils.setViewVisible
import java.lang.Exception

open class MasterScreenFragment: Fragment() {

    companion object {
        private const val LOG_TAG = "MasterScreenFragment"
    }

     fun setBackBtnVisible() {
        val backBtn = activity?.findViewById<ImageView>(R.id.btn_back)
        backBtn.setViewVisible()
    }

    fun hideBackBtn() {
        val backBtn = activity?.findViewById<ImageView>(R.id.btn_back)
        backBtn.setViewInvisible()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       setOnBackClickListener()
    }

    private fun setOnBackClickListener() {
        activity?.findViewById<ImageView>(R.id.btn_back)?.setOnClickListener {

            try {
                findNavController().popBackStack()
            } catch (ex: Exception) {
                Log.e(LOG_TAG, ex.message, ex)
            }

        }
    }



}