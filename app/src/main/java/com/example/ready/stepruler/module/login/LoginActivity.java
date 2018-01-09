package com.example.ready.stepruler.module.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.activity.MainActivity;
import com.example.ready.stepruler.module.register.RegisterDialog;


public class LoginActivity extends Activity implements View.OnClickListener,LoginView {
    private EditText idText;
    private EditText passwordText;
    private Button loginButton;
    private Button registerButton;
    private String mId;
    private String mPwd;
    public int user_id;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
        setListener();

        loginPresenter=new LoginPresenter(this);
    }

    public void initView() {
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
    }

    public void setListener() {
        idText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mId = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPwd = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                if (mId == null || mId.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入账号！", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    if (mPwd == null || mPwd.equals("")) {
                        Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        loginPresenter.doLogin(mId, mPwd);
                        break;
                    }
                }
            case R.id.registerButton:
                RegisterDialog registerDialog=new RegisterDialog(this);
                registerDialog.setCanceledOnTouchOutside(false);
                registerDialog.show();
            default:
                break;
        }
    }

    @Override
    public String getUsername(){return mId;}

    @Override
    public String getUserpwd(){return mPwd;}

    @Override
    public void showToast(String msg){Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();}

    @Override
    public void changeView(int id) {
        user_id = id;
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("user_name", mId);
        bundle.putInt("user_id", user_id);
        intent.putExtras(bundle);
        this.startActivity(intent);
        this.finish();
    }
}
