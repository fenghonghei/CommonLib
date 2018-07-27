package com.honghei.feng.commonlib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.honghei.feng.commonlib.R;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void testPermission(View view) {
    startActivity(new Intent(this, PermissionActivity.class));
  }

  public void testHttp(View view) {
    startActivity(new Intent(this, HttpActivity.class));
  }
}
