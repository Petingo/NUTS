package com.nuts.nuts;
/* Created by petingo on 2018/4/6. */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RegisterActivity extends Activity {
    private AnimateManager animateManager;
    private boolean backTag = false;
    private LinearLayout layoutChooseIdentity;
    private ConstraintLayout layoutLogin;
    private ConstraintLayout layoutGuestRegister;
    private ConstraintLayout layoutStudentRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        animateManager = new AnimateManager(this);
        layoutChooseIdentity = (LinearLayout) findViewById(R.id.layoutChooseIdentity);
        layoutLogin = (ConstraintLayout) findViewById(R.id.layoutLogin);
        layoutGuestRegister = (ConstraintLayout) findViewById(R.id.layoutGuestRegister);
        layoutStudentRegister = (ConstraintLayout) findViewById(R.id.layoutStudentRegister);
        layoutGuestRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccountInfo("guest","guest");
                finish();
            }
        });
        layoutStudentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateManager.leftSlide(layoutChooseIdentity, layoutLogin);
                backTag = true;
                login();
            }
        });
    }

    void login(){
        final EditText editEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editTextPassword);
        final Button confirm = (Button) findViewById(R.id.buttonRegisterConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                // TODO check if email available
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "請輸入信箱及密碼！", Toast.LENGTH_SHORT).show();
                } else {
                    updateAccountInfo("student",email);
                    finish();
                }
            }
        });
    }

    void updateAccountInfo(String identity, String account){
        SharedPreferences pref = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("identity", identity);
        editor.putString("account", account);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if (backTag) {
            animateManager.rightSlide(layoutLogin, layoutChooseIdentity);
            backTag = false;
        } else {
            super.onBackPressed();
        }
    }
}
