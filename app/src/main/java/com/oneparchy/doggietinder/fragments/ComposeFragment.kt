package com.oneparchy.doggietinder.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.oneparchy.doggietinder.R
import com.oneparchy.doggietinder.models.Post
import com.parse.ParseFile
import com.parse.ParseGeoPoint
import com.parse.ParseUser
import java.io.File

class ComposeFragment : Fragment() {

    companion object {
        private const val TAG = "ComposeFragment"
    }

    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1039
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    lateinit var ivPreview: ImageView
    lateinit var etDogName: EditText
    lateinit var etAge: EditText
    lateinit var etSex: EditText
    lateinit var etBreed: EditText
    lateinit var etDescription:EditText
    var currentLat: Double = 0.0
    var currentLong: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set listeners etc. here
        ivPreview = view.findViewById(R.id.ivPicture)
        etDogName = view.findViewById(R.id.etDogName)
        etAge = view.findViewById(R.id.etAge)
        etSex = view.findViewById(R.id.etSex)
        etBreed = view.findViewById(R.id.etBreed)
        etDescription = view.findViewById(R.id.etDescription)
        currentLat = requireArguments().getDouble("CurrentLat")
        currentLong = requireArguments().getDouble("CurrentLong")

        Log.i(TAG, arguments?.getString("key").toString())

        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            //Send post to server
            //Grab the post lost dog's info from the edit text
            val dogName = etDogName.text.toString()
            val age = etAge.text.toString()
            val sex = etSex.text.toString()
            val breed = etBreed.text.toString()
            val description = etDescription.text.toString()
            val user = ParseUser.getCurrentUser()
            val currLocation = ParseGeoPoint(currentLat, currentLong)


            if (photoFile != null) {
                submitPost(dogName, age, sex, breed, description, user, photoFile!!, currLocation)
            } else {
                Log.i(TAG, "No image taken for post")
                Toast.makeText(requireContext(), "Take a picture first!", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.btnTakePicture).setOnClickListener {
            onLaunchCamera()
        }
    }

    //Create the post and submit it to Parse
    private fun submitPost(
        dogName:String, age: String, sex: String, breed:String, description: String,
        user: ParseUser, file: File, location: ParseGeoPoint
    ) {
        val post = Post()
        post.setDogName(dogName)
        post.setAge(age)
        post.setSex(sex)
        post.setBreed(breed)
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.setLocation(location)
        post.saveInBackground { e ->
            if (e != null) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error making post", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Successfully made post!")
                Toast.makeText(requireContext(), "Successfully posted!", Toast.LENGTH_SHORT).show()
                etDescription.setText("")
                ivPreview.setImageBitmap(null)
//                val FeedFragment = FeedFragment()
//                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//                transaction.replace(R.id.flContainer, FeedFragment)
//                transaction.commit()
            }
        }
    }

    //Launch the camera app
    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}