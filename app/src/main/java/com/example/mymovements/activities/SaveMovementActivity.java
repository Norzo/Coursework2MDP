package com.example.mymovements.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovements.R;
import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.viewmodels.RecordMovementActivityViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveMovementActivity extends AppCompatActivity
{
    private String imageName; // string for image filepath

    private EditText recMovNameEditText; // variable for edit name text field
    private EditText recMovAnnotationEditText; // variable for edit note text field
    private int recordedMovementId; // reference to the id of the relevant recorded movement, needed for image filepath
    private boolean photoAdded = false; // flag to determine whether the user has added image or not

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_movement);

        recordedMovementId = getIntent().getIntExtra("recmovid", 1); // fetch the recorded movement id from intent extras
        imageName = "recorded_movement_photo_" + recordedMovementId; // instantiate the filepath wanted (e.g. "recorded_movement_photo_2")
        recMovNameEditText = findViewById(R.id.recmov_name_edittext); // instantiate edit name text field variable
        recMovAnnotationEditText = findViewById(R.id.recmov_annotation_edittext); // instantiate edit note text field variable
    }

    public void saveRecordedMovement(View view)
    {
        Intent intentAddRecMov = new Intent(); // create the intent which will contain the data retrieved from this activity
        String recMovName = recMovNameEditText.getText().toString(); // get the name, store it in a string
        String recMovAnnotation = recMovAnnotationEditText.getText().toString(); // get the notes, store it in a string
        if (recMovName.matches("")) // if nothing is in the recmovename, that means we can apply a default name
            recMovName = "A good lil' run";
        // add the data to intent extras
        intentAddRecMov.putExtra("recmovname", recMovName);
        intentAddRecMov.putExtra("recmovnote", recMovAnnotation);
        if (photoAdded) // if photoadded flag is true, we want to pass the filepath
            intentAddRecMov.putExtra("recmovphotopath", imageName);
        else // if not true, then we can simply pass an empty string to denote that there is no image attached
            intentAddRecMov.putExtra("recmovphotopath", "");
        if (recMovName.matches("")) // if name edit text field is empty, set result to be canceled, mostly for letting parent activity know how to react
            setResult(RESULT_CANCELED, intentAddRecMov);
        else
            setResult(RESULT_OK, intentAddRecMov);
        finish();
    }

    public void startCamera(View view)
    {
        // if statement to see if the application has permission to use the camera
        if (ContextCompat.checkSelfPermission(SaveMovementActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(SaveMovementActivity.this, new String[] { Manifest.permission.CAMERA }, 100);
        else // if the application has permission to use the camera, start camera activity
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 004);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 004) // request code is hardcoded for activity result from camera
        {
            switch (resultCode) // depending on the result code
            {
                case RESULT_OK:
                    File imageFile = getOutputMediaFile(); // fetch the file after proper creation
                    if (imageFile != null) // if file isnt null
                    {
                        Bitmap bitMap = (Bitmap) data.getExtras().get("data"); // fetch the bitmap that is saved from camera activity
                        try
                        {
                            FileOutputStream fos = new FileOutputStream(imageFile); // open file output stream to write to internal storage
                            bitMap.compress(Bitmap.CompressFormat.PNG, 90, fos); // compress the bitmap into a .PNG file
                            fos.close(); // close the file output stream object
                            photoAdded = true; // change the boolean of adding a photo to true
                            Toast.makeText(getApplicationContext(), "Photo added!", Toast.LENGTH_LONG).show(); // tell the user that the photo has been added
                        }
                        catch (FileNotFoundException e) { Log.d("file not found:", e.getMessage()); }
                        catch (IOException e) { Log.d("error accessing file:", e.getMessage()); }
                    }
            }
        }
    }

    private File getOutputMediaFile()
    {
        // create an object for media storage directory, effectively for creating directory in "/Android/data/com.example.mymovements.activities/Files"
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplication().getPackageName() + "/Files");

        if (!mediaStorageDir.exists()) // if the directory does not exist
        {
            if (!mediaStorageDir.mkdirs()) // if the media storage directory failed to be created
                return null; // return null
        }
        // if it did not fail
        File imageFile = new File(mediaStorageDir.getPath() + File.separator + imageName); // create the file object
        imageName = imageFile.getAbsolutePath(); // save the absolute path of the image file to image name
        Log.d("image file", imageFile.getAbsolutePath());
        return imageFile;
    }
}