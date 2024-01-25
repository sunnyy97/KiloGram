package com.example.kilogram2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class dietActivity extends AppCompatActivity {
    EditText editText;
    Button btnAdd, btnRest;
    RecyclerView recyclerView;

    List<MainData> dataList = new ArrayList<>();
    private double calorie;
    int tan, dan, gi;
    RoomDB database;
    MainAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
      
            calorie = bundle.getDouble("calorie", 0.0);  // 0.0은 기본값, 필요에 따라 변경 가능
            tan = bundle.getInt("tan", 0);  // 0은 기본값, 필요에 따라 변경 가능
            dan = bundle.getInt("dan", 0);  // 0은 기본값, 필요에 따라 변경 가능
            gi = bundle.getInt("gi", 0);  // 0은 기본값, 필요에 따라 변경 가능

            Log.d("dietActivity", "calorie: " + calorie);
            Log.d("dietActivity", "tan: " + tan);
            Log.d("dietActivity", "dan: " + dan);
            Log.d("dietActivity", "gi: " + gi);

            // 추출된 데이터를 사용하여 원하는 작업 수행
            // 예: UI 업데이트 또는 다른 로직 수행


        setContentView(R.layout.activity_diet);

        editText = findViewById(R.id.editText);
        btnAdd = findViewById(R.id.btnAdd);
        btnRest = findViewById(R.id.btnRest);
        recyclerView = findViewById(R.id.recyclerView);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MainAdapter(dietActivity.this, dataList, calorie, tan, dan, gi);

        recyclerView.setAdapter(adapter);





        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String sText = editText.getText().toString().trim();
                if (!sText.equals(""))
                {
                    MainData data = new MainData();
                    data.setText(sText);
                    database.mainDao().insert(data);

                    editText.setText("");

                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        btnRest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                database.mainDao().reset(dataList);

                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });

    }
}
