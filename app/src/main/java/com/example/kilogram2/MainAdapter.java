package com.example.kilogram2;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
{
    private List<MainData> dataList;
    private Activity context;
    private double calorie;
    private int tan, dan, gi;
    private RoomDB database;
    private List<MainData> selectedDataList;  // 선택된 데이터를 저장할 리스트 추가

    public MainAdapter(Activity context, List<MainData> dataList, double calorie, int tan, int dan, int gi)
    {
        this.context = context;
        this.dataList = dataList;
        this.calorie = calorie;
        this.tan = tan;
        this.dan = dan;
        this.gi = gi;
        //this.selectedDataList = new ArrayList<>();  // 초기화 추가
        notifyDataSetChanged();
    }

    // 다른 메소드들과 함께 선택된 데이터를 반환하는 메소드 추가
    public List<MainData> getSelectedDataList() {
        return selectedDataList;
    }



    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        return new ViewHolder(view);
    }

    // ItemClickListener 인터페이스 정의
    public interface ItemClickListener {
        void onItemClick(List<MainData> selectedDataList);
    }

    private ItemClickListener itemClickListener;

    // ItemClickListener를 설정하는 메서드 추가
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.ViewHolder holder, int position)
    {
        final MainData data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.textView.setText(data.getText());

        //holder.textView.setText(data.getTextWithoutNumber());


        String text2 = data.getText2();
        // text2를 사용하여 필요한 작업 수행
        // 여기에서 text2를 활용하여 추가적인 작업을 수행할 수 있습니다.



        holder.btEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainData mainData = dataList.get(holder.getAdapterPosition());

                final int sID = mainData.getId();
                String sText = mainData.getText();

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);

                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                dialog.getWindow().setLayout(width, height);

                dialog.show();

                final EditText editText = dialog.findViewById(R.id.dialog_edit_text);
                Button bt_update = dialog.findViewById(R.id.bt_update);
                //Button bt_registered = dialog.findViewById(R.id.bt_registered);

                editText.setText(sText);

                bt_update.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                        String uText = editText.getText().toString().trim();

                        database.mainDao().update(sID, uText);

                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();
                    }
                });
                /*
                bt_registered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        String uText = editText.getText().toString().trim();

                        database.mainDao().update(sID, uText);

                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();

                        // 선택된 데이터를 담을 리스트 생성
                        List<MainData> selectedDataList = new ArrayList<>();
                        // 여러 MainData 객체를 리스트에 추가 (예: dataList에서 선택된 데이터를 추가)
                        selectedDataList.addAll(getSelectedDataFromDataList());

                        Bundle bundle = new Bundle();
                        // 선택된 데이터 리스트를 put
                        bundle.putSerializable("selectedDataList", (Serializable) selectedDataList);
                        bundle.putString("calorie", String.valueOf(calorie));
                        bundle.putInt("tan", tan);
                        bundle.putInt("dan", dan);
                        bundle.putInt("gi", gi);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                        /* 수정중 3:39분
                        MainData lastSelectedData = selectedDataList.get(selectedDataList.size() - 1);
                        Bundle bundle = new Bundle();
                        bundle.putString("clickedText1", lastSelectedData.getText());
                        bundle.putString("clickedText2", lastSelectedData.getText2());
                        bundle.putString("calorie", String.valueOf(calorie));
                        bundle.putInt("tan", tan);
                        bundle.putInt("dan", dan);
                        bundle.putInt("gi", gi);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);


                    }
                });

                 */
            }
        });


        /* 삭제 클릭 */
        holder.btDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainData mainData = dataList.get(holder.getAdapterPosition());

                database.mainDao().delete(mainData);

                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });

        holder.btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainData mainData = dataList.get(holder.getAdapterPosition());
                selectedDataList.clear();
                selectedDataList.addAll(database.mainDao().getAll());
                notifyDataSetChanged();
                // 선택 여부 업데이트
                holder.isSelected = !holder.isSelected;
                // 선택된 항목의 데이터를 저장
                if (holder.isSelected) {
                    selectedDataList.add(mainData);
                } else {
                    selectedDataList.remove(mainData);
                }

                /*
                // 선택된 데이터가 있다면 리스너 호출
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(selectedDataList);
                }

                 */

                // 선택된 데이터가 있다면 MainActivity로 전달
                if (!selectedDataList.isEmpty()) {
                    MainData lastSelectedData = selectedDataList.get(selectedDataList.size() - 1);

                    Bundle bundle = new Bundle();
                    bundle.putString("clickedText1", lastSelectedData.getText());
                    bundle.putString("clickedText2", lastSelectedData.getText2());
                    bundle.putString("calorie", String.valueOf(calorie));
                    bundle.putInt("tan", tan);
                    bundle.putInt("dan", dan);
                    bundle.putInt("gi", gi);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);


                }



            }
        });

    }

    // 선택된 데이터 리스트를 얻는 메소드
    private List<MainData> getSelectedDataFromDataList() {
        List<MainData> selectedDataList = new ArrayList<>();
        // 여기에 dataList에서 선택된 데이터를 추출하여 selectedDataList에 추가하는 로직을 작성
        return selectedDataList;
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView btEdit, btDelete, btUpload;
        boolean isSelected;  // 선택 여부를 나타내는 변수

        public ViewHolder(@NonNull View view)
        {
            super(view);
            textView = view.findViewById(R.id.text_view);
            btEdit = view.findViewById(R.id.bt_edit);
            btDelete = view.findViewById(R.id.bt_delete);
            btUpload = view.findViewById(R.id.bt_upload);

            isSelected = false;  // 초기값은 선택되지 않은 상태로 설정
        }
    }
}