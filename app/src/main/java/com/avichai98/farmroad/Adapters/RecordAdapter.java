package com.avichai98.farmroad.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.avichai98.farmroad.Interfaces.CallBack_List;
import com.avichai98.farmroad.R;
import com.avichai98.farmroad.Manager.Record;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private final ArrayList<Record> records;
    private CallBack_List callBack_list;

    public RecordAdapter(ArrayList<Record> records) {
        this.records = records;
    }

    public void setCallBack_List(CallBack_List callBack_list) {
        this.callBack_list = callBack_list;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_record_item, parent, false);

        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = getItem(position);
        holder.record_LBL_rank.setText(String.valueOf(position + 1));
        holder.record_LBL_date.setText(record.getFormattedDate());
        holder.record_LBL_time.setText(record.getFormattedTime());
        holder.record_LBL_score.setText(String.valueOf(record.getScore()));
    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    public Record getItem(int position) {
        return records.get(position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView record_LBL_rank;
        private final MaterialTextView record_LBL_date;
        private final MaterialTextView record_LBL_time;
        private final MaterialTextView record_LBL_score;
        private final LinearLayout record_LL_data;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record_LBL_rank = itemView.findViewById(R.id.record_LBL_rank);
            record_LBL_date = itemView.findViewById(R.id.record_LBL_date);
            record_LBL_time = itemView.findViewById(R.id.record_LBL_time);
            record_LBL_score = itemView.findViewById(R.id.record_LBL_score);
            record_LL_data = itemView.findViewById(R.id.record_LL_data);

            record_LL_data.setOnClickListener(v -> {
                if (callBack_list != null) {
                    callBack_list.showLocationInMap(getItem(getAdapterPosition()));
                }
            });
        }
    }
}
