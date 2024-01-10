package com.esrefnifteliyev.permission2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.esrefnifteliyev.permission2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var actionResultLauncher: ActivityResultLauncher<Intent>
    private var image : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerPermission()

        binding.imageView.setOnClickListener {
            check(it)
        }


    }

    fun registerAction(){
       actionResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
          if (result.resultCode == RESULT_OK){
              val d = result.data
              d?.let { intentResult ->
                  intentResult.data?.let {
                      image = it
                      Glide.with(this)
                          .load(image)
                          .into(binding.imageView)
                  }
              }
          }

        }
    }

    fun check(view: View){
        if (
           ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                   permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }

            else{
                 permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        else{
           val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            actionResultLauncher.launch(intent)
        }
    }


      fun registerPermission(){
          registerAction()
          permissionLauncher =
              registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
                  if (result){
                       val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                       actionResultLauncher.launch(intent)
                  }

                  else{
                      Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show()
                  }

              }
      }


}