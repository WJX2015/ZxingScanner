package com.example.lenovo_g50_70.zxingscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private Button btnCreate;
    private Button btnScanner;
    private ImageView imageCode;
    private TextView txtResult;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        editText = (EditText) findViewById(R.id.editText);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnScanner = (Button) findViewById(R.id.ScanCode);
        imageCode = (ImageView) findViewById(R.id.imageCode);
        txtResult = (TextView) findViewById(R.id.txtResult);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str =editText.getText().toString().trim();
                try {
                    str =new String(str.getBytes("UTF-8"),"ISO-8859-1");//解决中文乱码
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap=encodeAsBitmap(str);
                imageCode.setImageBitmap(bitmap);
            }
        });
        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new IntentIntegrator(MainActivity.this)
                       .setOrientationLocked(false)
                       .setCaptureActivity(CustomScanActivity.class)
                       .initiateScan();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"扫描成功",Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                txtResult.setText(ScanResult);
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private Bitmap encodeAsBitmap(String str){
        Bitmap bitmap =null;
        BitMatrix result =null;
        MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
        try{
            result =multiFormatWriter.encode(str, BarcodeFormat.QR_CODE,200,200);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
