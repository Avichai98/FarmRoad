package com.avichai98.farmroad.Manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.avichai98.farmroad.Interfaces.CallBack_List;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.avichai98.farmroad.Adapters.RecordAdapter;
import com.avichai98.farmroad.R;

public class RecordsFragmentList extends Fragment {
    private RecyclerView frg_LST_records;

    private CallBack_List callBack_List;

    public RecordsFragmentList() {
    }

    public void setCallBack_List(CallBack_List callBack_List) {
        this.callBack_List = callBack_List;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        RecordAdapter recordAdapter = new RecordAdapter(RecordsManager.getInstance().getRecordsArrayList());
        recordAdapter.setCallBack_List(callBack_List);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        frg_LST_records.setLayoutManager(linearLayoutManager);
        frg_LST_records.setAdapter(recordAdapter);

    }

    private void findViews(View view) {
        frg_LST_records = view.findViewById(R.id.frg_LST_records);
    }
}