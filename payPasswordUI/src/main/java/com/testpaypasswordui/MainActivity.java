package com.testpaypasswordui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private com.paybaseui.PayPasswordBoxView ppbv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ppbv = (com.paybaseui.PayPasswordBoxView) findViewById(R.id.ppbv);


//        动态设置
//        ppbv.setQuantity(3)
//                .setWh(250)
//                .setAboutSpacing(15)
//                .setSonBackground(R.drawable.login_button_style)
//                .setPanel(R.xml.numbers_keyboard)
//                .setPossword(true)
//                .setKeyboardViewId(R.id.keyboard_view)
//                .iniView(this);



        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, ppbv.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
