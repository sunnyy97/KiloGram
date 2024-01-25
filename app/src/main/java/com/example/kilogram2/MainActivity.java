package com.example.kilogram2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    TextView textView, tv1, tv2, tv3, tvTan, tvDan, tvGi, diet_tv, textView3;
    Button calculateBtn;
    ImageView addbtn;

    double calorie;
    double resultCal;
    int  tan, dan, gi;
    String weightValue;
    ProgressBar progressBar1, progressBar2, progressBar3;
    RecyclerView recyclerView;
    List<MainData> dataList;
    MainAdapter adapter;


   // double calorie = 0.0; // 초기값 설정
   // int tan = 0, dan = 0, gi = 0; // 초기값 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // dataList 초기화
        dataList = new ArrayList<>();

        calculateBtn = findViewById(R.id.calculateBtn);

        textView = findViewById(R.id.textView);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);


        tvTan = findViewById(R.id.tvTan);
        tvDan = findViewById(R.id.tvDan);
        tvGi = findViewById(R.id.tvGi);

        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar1.setProgress(80);

        addbtn = findViewById(R.id.addbtn);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // dataList를 어댑터에 전달
        adapter = new MainAdapter(MainActivity.this, dataList, calorie, tan, dan, gi);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new MainAdapter.ItemClickListener() {
            @Override
            public void onItemClick(List<MainData> selectedDataList) {
                // 여기에서 선택된 데이터(selectedDataList)를 사용하여 처리
                // 예: 선택된 데이터를 리스트에 추가하거나 다른 작업 수행
                // 이 예제에서는 선택된 데이터의 첫 번째 항목을 가져옴
                if (!selectedDataList.isEmpty()) {
                    MainData selectedData = selectedDataList.get(0);
                    String clickedText1 = selectedData.getText();
                    String clickedText2 = selectedData.getText2();

                    // 이제 선택된 데이터를 사용하여 필요한 작업 수행
                    // 여기에서는 예시로 토스트 메시지 출력
                    Toast.makeText(MainActivity.this, "Clicked: " + clickedText1 + ", " + clickedText2, Toast.LENGTH_SHORT).show();
                }
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.d("MainActivity", "Received intent with bundle: " + bundle);

        if (bundle != null) {

            String intentValue1 = bundle.getString("clickedText1");
            String intentValue2 = bundle.getString("clickedText2");
            List<MainData> selectedDataList = (List<MainData>) bundle.getSerializable("selectedDataList");
            weightValue = bundle.getString("weight");
            // diet_tv.setText(intentValue1);
            // textView3.setText(intentValue2);

            // 데이터를 RecyclerView에 추가
            updateRecyclerView(intentValue1);
            updateRecyclerView(intentValue2);

            // 추가된 부분: calorie 값을 가져와서 설정
            String storedCalorie = bundle.getString("calorie");
            if (storedCalorie != null && !storedCalorie.isEmpty()) {
                calorie = Double.parseDouble(storedCalorie);
                textView.setText("하루 권장 섭취 칼로리 : " + calorie);
            }

            tan = bundle.getInt("tan");
            tvTan.setText("0 / " + tan + "g");
            dan = bundle.getInt("dan");
            tvDan.setText("0 / " + dan + "g");
            gi = bundle.getInt("gi");
            tvGi.setText("0 / " + gi + "g");



            // 여기서 intentValue1, intentValue2, intentValue를 사용할 수 있습니다.
        } else {
            // bundle이 null인 경우 처리
            // 예: 기본값 설정 또는 오류 처리
        }


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권장하루 섭취량 공식
                calorie = Double.parseDouble(weightValue) * 24 * 1.5;
                textView.setText("하루 권장 섭취 칼로리 : " + calorie);

                // 탄단지 비율 계산 및 설정
                tan = (int) (calorie * 0.4 / 4);
                dan = (int) (calorie * 0.4 / 4);
                gi = (int) (calorie * 0.2 / 9);
                // tvTan, tvDan, tvGi 값을 설정하는 부분 추가
                tvTan.setText("0 / " + tan + "g");
                tvDan.setText("0 / " + dan + "g");
                tvGi.setText("0 / " + gi + "g");
            }
        });


        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataList.isEmpty()) {
                    int totalFirstNumber = 0;
                    int totalSecondNumber = 0;
                    int totalThirdNumber = 0;
                    int totalFourthNumber = 0;
                    int itemCount = 0;

                    for (MainData mainData : dataList) {
                        String itemText = mainData.getText();
                        List<Integer> extractedNumbers = extractNumbers(itemText);

                        if (extractedNumbers.size() >= 4) {
                            // 200, 54, 8, 15
                            int firstNumber = extractedNumbers.get(0);
                            int secondNumber = extractedNumbers.get(1);
                            int thirdNumber = extractedNumbers.get(2);
                            int fourthNumber = extractedNumbers.get(3);

                            totalFirstNumber += firstNumber;
                            totalSecondNumber += secondNumber;
                            totalThirdNumber += thirdNumber;
                            totalFourthNumber += fourthNumber;

                            itemCount++;
                        }
                    }

                    if (itemCount > 0) {
                        // 각각의 숫자의 평균 계산
                        int averageFirstNumber = totalFirstNumber;
                        int averageSecondNumber = totalSecondNumber;
                        int averageThirdNumber = totalThirdNumber;
                        int averageFourthNumber = totalFourthNumber;

                        // 결과를 사용하거나 표시하는 등의 로직 수행
                        textView.setText(String.valueOf("하루 권장 섭취 칼로리 : " + (int) (calorie - averageFirstNumber)));
                        textView.setTextColor(Color.RED);
                        tvTan.setText(" " + averageSecondNumber + "/" + tan + "g");
                        tvDan.setText(" " + averageThirdNumber + "/" + dan + "g");
                        tvGi.setText(" " + averageFourthNumber + "/" + gi + "g");

                    }
                }
            }
        });




        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), dietActivity.class);
                Bundle bundle = new Bundle();

                bundle.putDouble("calorie", calorie);
                bundle.putInt("tan", tan);
                bundle.putInt("dan", dan);
                bundle.putInt("gi", gi);
                intent.putExtras(bundle);

                startActivity(intent);

                Log.d("MainActivity", "calorie: " + calorie);
                Log.d("MainActivity", "tan: " + tan);
                Log.d("MainActivity", "dan: " + dan);
                Log.d("MainActivity", "gi: " + gi);
            }
        });
    }

    // RecyclerView 갱신 메소드
    public void updateRecyclerView(String newData) {
        Log.e("MainActivity", "updateRecyclerView: " + newData);
        MainData data = new MainData();
        data.setText(newData);

        // 데이터 추가
        dataList.add(data);
        // 어댑터 갱신
        adapter.notifyDataSetChanged();
    }


    // 문자열에서 숫자 추출하는 메서드
    private List<Integer> extractNumbers(String text) {
        List<Integer> numbers = new ArrayList<>();
        // 숫자를 추출할 정규표현식
        String regex = "\\d+";

        // 정규표현식을 컴파일한 후 매처를 얻음
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // 매칭된 숫자를 리스트에 추가
        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group());
            numbers.add(number);
        }

        return numbers;
    }


}