package com.cookandroid.dietmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.Calendar;


public class CameraActivity extends AppCompatActivity {
    Button btnCamera2main, btnSelectPicture;
    TextView tvSaveTime, tvFoodName;
    ImageView ivFood;
    LinearLayout totalLayout, dataLayout;
    FrameView dataFrameView, totalFrameView;
    String saveTime, strFoodData="No Data", strTotal="No Data";
    float kcal = (float) 935, tans = (float) 56.21, danb = (float) 34.48, jiba = (float) 27.91,
            natt = (float) 1250.30, tKcal = (float) 2105, cKcal = (float) 1450;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnCamera2main = (Button) findViewById(R.id.btnCamera2main);
        btnSelectPicture = (Button) findViewById(R.id.btnSelectPicture);
        tvSaveTime = (TextView) findViewById(R.id.tvSaveTime);
        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        ivFood = (ImageView) findViewById(R.id.ivFood);
        dataLayout = (LinearLayout) findViewById(R.id.dataLayout);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);

        // 음식 분석 알고리즘 실행
        analyzeFood();

        // 데이터 String 생성
        loadData();

        // 가져온 음식 데이터
        dataFrameView = (FrameView) new FrameView(this,360,strFoodData);
        dataLayout.addView(dataFrameView);

        // 총 칼로리 섭취량
        totalFrameView = (FrameView) new FrameView(this,100,strTotal);
        totalLayout.addView(totalFrameView);

        // 날짜받아서 화면에 띄우기
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        saveTime = year + "년 " + month + "월 " + day + "일 ";
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour>=5 && hour<12){
            saveTime = saveTime + "아침식사";
        } else if(hour>=12 && hour<18){
            saveTime = saveTime + "점식식사";
        } else{
            saveTime = saveTime + "저녁식사";
        }
        tvSaveTime.setText(saveTime);

        // 뒤로가기 버튼 활성화
        btnCamera2main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 카메라 버튼 활성화 (갤러리, 카메라 선택)
        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.openGallery){
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 0);
                        } else{
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivity(intent);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    // 갤러리에 접근하여 사진 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    ivFood.setImageBitmap(bmp);
                } catch (Exception e) {
                }
            } else if (resultCode == 1) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 사진으로 음식 분석 알고리즘
    protected void analyzeFood(){
        ////////////////////////
        //     Processing     //
        ////////////////////////
    }

    // 데이터 가져와 String 생성하는 메소드
    protected void loadData(){

        ////////////////////////
        //   데이터 불러오기   //
        ////////////////////////

        strFoodData = "칼로리"     + "               " + kcal + "  kcal"  + "\n" +
                      "탄수화물"   + "           " +  tans + "  g"   + "\n" +
                      "단백질"   + "               " +  danb + "  g"   + "\n" +
                      "지방"   + "                   " +  jiba + "  g"   + "\n" +
                      "나트륨"   + "               " +  natt + "  mg";
        strTotal = "총 섭취량" + "               " + cKcal + " / " + tKcal + "  kcal";
    }
}