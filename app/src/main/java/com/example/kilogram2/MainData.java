package com.example.kilogram2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name")
public class MainData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "text")
    private String text;
    private String text2;

    @ColumnInfo(name = "number1")
    private int number1;

    @ColumnInfo(name = "number2")
    private int number2;

    @ColumnInfo(name = "number3")
    private int number3;

    @ColumnInfo(name = "number4")
    private int number4;

    @ColumnInfo(name = "some_other_text")
    private String someOtherText;

    @ColumnInfo(name = "updated_text")
    private String updatedText;

    public MainData() {
        // 기본 생성자
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }

    public int getNumber3() {
        return number3;
    }

    public int getNumber4() {
        return number4;
    }

    public String getText2() {
        return text2;
    }

    public String getSomeOtherText() {
        return someOtherText;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public void setNumber3(int number3) {
        this.number3 = number3;
    }

    public void setNumber4(int number4) {
        this.number4 = number4;
    }

    public void setSomeOtherText(String someOtherText) {
        this.someOtherText = someOtherText;
    }

    public String getTextWithoutNumber() {
        // ':' 이전의 텍스트 반환
        int colonIndex = text.indexOf(':');
        return colonIndex != -1 ? text.substring(0, colonIndex).trim() : text;
    }

    public int getNumber() {
        // ':' 이후의 숫자 반환
        int colonIndex = text.indexOf(':');
        if (colonIndex != -1) {
            String numberString = text.substring(colonIndex + 1).trim();
            try {
                return Integer.parseInt(numberString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0; // 기본값 설정
    }

    public String getUpdatedText() {
        return updatedText;
    }

    public void setUpdatedText(String updatedText) {
        this.updatedText = updatedText;
    }

    public MainData(String text, String someOtherText) {
        this.text = text;
        this.someOtherText = someOtherText;
    }
}
