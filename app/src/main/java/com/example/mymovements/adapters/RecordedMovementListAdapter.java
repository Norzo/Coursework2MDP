package com.example.mymovements.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovements.R;
import com.example.mymovements.entities.RecordedMovement;

import java.util.ArrayList;
import java.util.List;

public class RecordedMovementListAdapter extends RecyclerView.Adapter<RecordedMovementListAdapter.RecordedMovementsViewHolder>
{
    class RecordedMovementsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final TextView recordedMovementName, recordedMovementDistance, recordedMovementTime;
        ItemClickListener itemClickListener;

        public RecordedMovementsViewHolder(@NonNull View itemView, ItemClickListener itemClickListener)
        {
            super(itemView);
            recordedMovementName = itemView.findViewById(R.id.textViewDate);
            recordedMovementDistance = itemView.findViewById(R.id.textViewDistance);
            recordedMovementTime = itemView.findViewById(R.id.textViewTime);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        void bind(final RecordedMovement recordedMovement)
        {
            if (recordedMovement != null)
            {
                if (!recordedMovement.getName().matches("A good lil' run"))
                    recordedMovementName.setText(recordedMovement.getName());
                else
                    recordedMovementName.setText(recordedMovement.getDate());
                String tempString = recordedMovement.getDistance() + " m";
                recordedMovementDistance.setText(tempString);
                tempString = recordedMovement.getTime() + " ms";
                recordedMovementTime.setText(tempString);
            }
        }

        @Override
        public void onClick(View v)
        {
            itemClickListener.onItemClicked(getAdapterPosition());
        }
    }

    private final LayoutInflater mInflater;
    private List<RecordedMovement> mRecordedMovements;
    private ItemClickListener itemClickListener;

    public RecordedMovementListAdapter(Context context, ItemClickListener itemClickListener)
    {
        mInflater = LayoutInflater.from(context);
        this.mRecordedMovements = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecordedMovementsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RecordedMovementsViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(RecordedMovementsViewHolder holder, int position)
    {
        holder.bind(mRecordedMovements.get(position));
    }

    public List<RecordedMovement> getmRecordedMovements() { return mRecordedMovements; }

    @Override
    public int getItemCount() { return mRecordedMovements.size(); }

    public void setRecordedMovements(List<RecordedMovement> recordedMovements)
    {
        if (mRecordedMovements != null)
        {
            mRecordedMovements.clear();
            mRecordedMovements.addAll(recordedMovements);
            notifyDataSetChanged();
        }
        else
            mRecordedMovements = recordedMovements;
    }

    public interface ItemClickListener { void onItemClicked(int position); }
}
