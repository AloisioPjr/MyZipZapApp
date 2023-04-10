package com.example.myzipzap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRGenerator extends AppCompatActivity {

    ImageView qrImg;

    Button qrGenerate, scanner;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);

        qrImg = findViewById(R.id.qrImg);
        qrGenerate = findViewById(R.id.generate);
        scanner = findViewById(R.id.scanBtn);
        userId = currentUser.getUid();


        qrGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser == null) {
                    Toast.makeText(QRGenerator.this, "Could not access User ID", Toast.LENGTH_SHORT).show();
                }else{
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();

                    Point point = new Point();
                    display.getSize(point);

                    int width = point.x;
                    int height = point.y;

                    int dim = width < height ? width : height;
                    dim = dim * 3/4;

                    qrgEncoder = new QRGEncoder("ID "+ userId, null, QRGContents.Type.TEXT, dim);


                    try {
                        bitmap = qrgEncoder.getBitmap(0);
                        qrImg.setImageBitmap(bitmap);
                    }catch(Exception e) {
                        Toast.makeText(QRGenerator.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(QRGenerator.this, QRScanner.class)));
            }
        });
    }
}
