package com.example.kilogram2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class bmi extends AppCompatActivity {
    ImageView imgView;
    EditText heightText, weightText;
    TextView resultText;
    Button btnOk;
    String height, weight, BMI, result2;
    double result1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_activity);


        heightText = findViewById(R.id.heightText);
        weightText = findViewById(R.id.weightText);
        resultText = findViewById(R.id.resultText);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                height = heightText.getText().toString();
                weight = weightText.getText().toString();

                result1 = Double.parseDouble(weight) / ((Double.parseDouble(height) / 100) * (Double.parseDouble(height) / 100));

                if(result1 < 20) result2 = "저체중";
                else if(result1 <= 24 && result1 > 20) result2 = "정상제충";
                else if(result1 <= 30 && result1 >= 24) result2 = "과제충";
                else result2 = "비만";

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("weight", weight);
                intent.putExtras(bundle);
                startActivity(intent);

                String BMI = String.format("%.2f", result1);
                resultText.setText("결과 : " + BMI + ", " + result2);

            }
        });

    }
}
