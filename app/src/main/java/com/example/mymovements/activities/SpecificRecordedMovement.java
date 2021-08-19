package com.example.mymovements.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymovements.R;
import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.viewmodels.SpecificRecordedMovementViewModel;

import java.io.File;

public class SpecificRecordedMovement extends AppCompatActivity
{
    private SpecificRecordedMovementViewModel mViewModel;
    private TextView textViewDate, textViewDistance, textViewSpeed;
    private EditText editTextName, editTextNote;
    ImageView imageView;
    private RecordedMovement mRecordedMovement;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_recorded_movement);

        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(SpecificRecordedMovementViewModel.class);
        textViewDate = findViewById(R.id.text_recorded_movement_date);
        textViewDistance = findViewById(R.id.text_recorded_movement_distance);
        textViewSpeed = findViewById(R.id.text_recorded_movement_speed);
        editTextName = findViewById(R.id.text_recorded_movement_name);
        editTextNote = findViewById(R.id.text_recorded_movement_note);
        imageView = findViewById(R.id.imageview);

        mViewModel.getRecordedMovementById(getIntent().getIntExtra("recMovId", 1)).observe(this, new Observer<RecordedMovement>()
        {
            @Override
            public void onChanged(RecordedMovement recordedMovement)
            {
                if (recordedMovement != null)
                {
                    mRecordedMovement = recordedMovement;
                    textViewDate.setText(recordedMovement.getDate());
                    String tempString = recordedMovement.getDistance() + " m";
                    textViewDistance.setText(tempString);
                    tempString = recordedMovement.getSpeed() + " m/s";
                    textViewSpeed.setText(tempString);
                    editTextName.setText(recordedMovement.getName());
                    editTextNote.setText(recordedMovement.getNote());

                    File imgFile = new File(recordedMovement.getImage());
                    if (imgFile.exists())
                    {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        });
    }

    public void updateRecMov(View view)
    {
        String newName = editTextName.getText().toString();
        String newNote = editTextNote.getText().toString();
        if (newName != mRecordedMovement.getName())
            mRecordedMovement.setName(newName);
        if (newNote != mRecordedMovement.getNote())
            mRecordedMovement.setNote(newNote);
        mViewModel.updateRecordedMovement(mRecordedMovement);
    }

    public void showMapRoute(View view)
    {
        Intent intent = new Intent(this, MapsRouteActivity.class);
        intent.putExtra("recmovid", mRecordedMovement.getId());
        startActivity(intent);
    }

    public void closeRecMov(View view) { finish(); }
}