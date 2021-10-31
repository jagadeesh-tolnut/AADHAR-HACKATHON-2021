package com.example.docscan

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.docscan.databinding.ActivityMainBinding
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var bitmapImage: Bitmap? = null
    var bitmap: Bitmap? = null
    var exportImage:Bitmap? = null
    private lateinit var camImage:Bitmap
    private lateinit var galImage:Bitmap
    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //USER_PERMISSION
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_DENIED
        ) {
            val permission =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, 112)
        }

        //ACCESSING_CAMERA
        binding.cameraButton.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permission =
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 121)
            } else {
                openCamera()
            }
        }

        //ACCESSING_GALLERY
        binding.galleryButton.setOnClickListener {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
        }
        binding.btnPdf.setOnClickListener {
            createPdf()
        }

    }

    var image_uri: Uri? = null

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            binding.imageView.setImageURI(image_uri);
            bitmapImage = uriToBitmap(image_uri!!)
            exportImage = bitmapImage


        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            image_uri = data.data
            binding.imageView.setImageURI(image_uri);
            bitmap = uriToBitmap(image_uri!!)
            exportImage = bitmap


        }

    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    //CREATING_PDF
    private  fun createPdf(){
        binding.btnPdf.setOnClickListener{

            val myPdfDocument = PdfDocument()

            val myPaint = Paint()

            val pageInfo = PdfDocument.PageInfo.Builder(3000, 5000, 1).create()
            val myPage:PdfDocument.Page = myPdfDocument.startPage(pageInfo)
            var canVas  = Canvas()
            canVas = myPage.canvas
            val x = 0
            val y = 0
            val a = x.toFloat()
            val b = y.toFloat()

            exportImage?.let { it1 -> canVas.drawBitmap(it1, a,b, myPaint) }


            myPdfDocument.finishPage(myPage)
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "/TestPDF.pdf")


            val writedFile = FileOutputStream(file)
            myPdfDocument.writeTo(writedFile)
            Toast.makeText(this, "PDF created successfully check Download folder", Toast.LENGTH_LONG).show()
            myPdfDocument.close()





        }
    }

}