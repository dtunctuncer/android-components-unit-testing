package com.dtunctuncer.testing

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @VisibleForTesting
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveBtn.isEnabled = viewModel.saveButtonEnabled
        saveBtn.setOnClickListener {
            viewModel.saveButtonClicked()
        }
    }
}