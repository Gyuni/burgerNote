package com.example.tablayouttest;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TextMemoListFragment extends Fragment implements View.OnClickListener {

    private ArrayList<TextMemoData> arrayList;
    private TextMemoAdapter textMemoAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    SQLiteDatabase db;
    TextMemoDBHelper helper;

    Button addBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.text_memo_list_fragment, container, false);

        addBtn = view.findViewById(R.id.text_memo_list_add_btn);
        addBtn.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.tab2_rv);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        // DB 가져오기 시작
        helper = new TextMemoDBHelper(getActivity());
        db = helper.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.rawQuery("select content, date from tb_text_memo order by _id desc", null);
        while(cursor.moveToNext()) {
            TextMemoData data = new TextMemoData(cursor.getString(0), cursor.getString(1));
            arrayList.add(data);
        }
        db.close();
        // DB 가져오기 끝

        textMemoAdapter = new TextMemoAdapter(arrayList);
        recyclerView.setAdapter(textMemoAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), TextMemoWriteActivity.class);
        startActivity(intent);
    }
}
