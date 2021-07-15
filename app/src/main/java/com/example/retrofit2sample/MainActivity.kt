package com.example.retrofit2sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer

    private lateinit var viewModel: MyViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = MyViewModelFactory().create(MyViewModel::class.java)

        viewModel.fireDatum.observe(this, Observer<MyViewModel.User> { newDatum ->
            frm_name.setText(
                viewModel.getFireDatumByText(newDatum)
            )
        })

        viewModel.fireData.observe(this, Observer{ newData ->
            text1.text = viewModel.getFireDatumByText(newData)
        })

        viewModel.getUsersByRetrofit()

        button1.setOnClickListener {
            val id_str = frm_id.text.toString()
            if (id_str != "") {
                viewModel.getUsersByRetrofit(id_str.toInt())
            }
        }


    }
}