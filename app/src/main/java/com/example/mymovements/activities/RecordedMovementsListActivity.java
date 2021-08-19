package com.example.mymovements.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.mymovements.R;
import com.example.mymovements.adapters.RecordedMovementListAdapter;
import com.example.mymovements.entities.RecordedMovement;
import com.example.mymovements.viewmodels.RecordedMovementsListViewModel;

import java.util.List;

public class RecordedMovementsListActivity extends AppCompatActivity implements RecordedMovementListAdapter.ItemClickListener
{
    private RecyclerView mRecyclerView;
    private RecordedMovementsListViewModel mViewModel;
    private RecordedMovementListAdapter mAdapter;
    private LiveData<List<RecordedMovement>> mAllRecordedMovements;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_movements_list);

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new RecordedMovementListAdapter(getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RecordedMovementsListViewModel.class);
        mAllRecordedMovements = mViewModel.getAllRecordedMovements();
        mAllRecordedMovements.observe(this, new Observer<List<RecordedMovement>>()
        {
            @Override
            public void onChanged(List<RecordedMovement> recordedMovements)
            {
                mAdapter.setRecordedMovements(recordedMovements);
            }
        });
    }

    @Override
    public void onItemClicked(int position)
    {
        int recMovId = mAdapter.getmRecordedMovements().get(position).getId();
        Intent intent = new Intent(this, SpecificRecordedMovement.class);
        intent.putExtra("recMovId", recMovId);
        startActivity(intent);
    }
}