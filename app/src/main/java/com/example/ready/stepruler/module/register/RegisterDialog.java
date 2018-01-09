package com.example.ready.stepruler.module.register;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.module.login.User;
import com.example.ready.stepruler.utils.RandomUtil;


public class RegisterDialog extends Dialog implements View.OnClickListener,RegisterView {
    Activity context;
    private EditText idText;
    private EditText passwordText, passwordText2;
    private EditText codeText;
    private Button registerButton;
    private Button cancleButton;
    private ImageView imageView;
    private String mName;
    private String mPwd;
    private String mPwd2;
    private String mCode;
    private String result = "default";
    //产生的验证码
    private String realCode;

    private RegisterPresenter registerPresenter;

    public RegisterDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.register);
        initView();
        setListener();
        this.setCancelable(true);

        registerPresenter = new RegisterPresenter(this);
    }

    public void initView() {
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        passwordText2 = (EditText) findViewById(R.id.passwordText2);
        codeText = (EditText) findViewById(R.id.codeText);
        registerButton = (Button) findViewById(R.id.button);
        cancleButton = (Button) findViewById(R.id.button2);
        imageView = (ImageView) findViewById(R.id.imageView);
        //将验证码用图片的形式显示出来
        imageView.setImageBitmap(RandomUtil.getInstance().createBitmap());
        realCode = RandomUtil.getInstance().getCode().toLowerCase();
    }

    public void setListener() {
        idText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mName = s.toString();
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
        passwordText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPwd2 = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        codeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCode = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        registerButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (mName == null || mName.equals("")) {
                    Toast.makeText(context, "请输入账号！", Toast.LENGTH_SHORT).show();
                    break;
                } else if (mName.length() < 6 || mName.length() > 16) {
                    Toast.makeText(context, "账号长度不正确！", Toast.LENGTH_SHORT).show();
                } else if (mPwd == null || mPwd.equals("")) {
                    Toast.makeText(context, "请输入密码！", Toast.LENGTH_SHORT).show();
                    break;
                } else if (mPwd.length() < 6 || mPwd.length() > 22) {
                    Toast.makeText(context, "密码长度不正确！", Toast.LENGTH_SHORT).show();
                } else if (mPwd2 == null || !mPwd2.equals(mPwd)) {
                    Toast.makeText(context, "两次输入的密码不同！", Toast.LENGTH_SHORT).show();
                    break;
                } else if (mCode == null || !mCode.equals(realCode)) {
                    Toast.makeText(context, "验证码错误！", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    User user = new User(mName, mPwd);
                    registerPresenter.doRegister(user);
                    break;
                }
            case R.id.imageView:
                imageView.setImageBitmap(RandomUtil.getInstance().createBitmap());
                realCode = RandomUtil.getInstance().getCode().toLowerCase();
                break;
            case R.id.button2:
                hide();
                break;
            default:
                break;
        }
    }

    @Override
    public String getUsername() {
        return mName;
    }

    @Override
    public String getPassword() {
        return mPwd;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
