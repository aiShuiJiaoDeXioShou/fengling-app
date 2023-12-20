package com.linghe.fengling.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.linghe.fengling.R;
import com.linghe.fengling.entity.User;
import com.linghe.fengling.ui.BaseActivity;
import org.litepal.LitePal;

import java.util.List;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String number;
    private String password;
    private User user;
    private void getEditText(){
        number=((EditText)findViewById(R.id.inputNumber)).getText().toString();
        password=((EditText)findViewById(R.id.inputPassword)).getText().toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
//        setSupportActionBar(toolbar);
        Button login_btn=findViewById(R.id.login_btn);
        TextView register=findViewById(R.id.sign_up);
        register.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //登录按钮
            case R.id.login_btn:
                getEditText();
                //判断两个输入框
                if(number==null||number.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password==null||password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //根据手机号查找用户
                List<User> findUser= LitePal.where("userNumber=?",number)
                        .find(User.class);
                //判断有无用户

                //用户不存在
                if(findUser.size()==0){
                    Toast.makeText(this,"无此用户，请重新输入手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                //用户存在
                else{
                    user=findUser.get(0);

                    //验证密码
                    if(user.getUserPassword().equals(password)){
                        Toast.makeText(this,"登陆成功，返回主页面",Toast.LENGTH_SHORT).show();
                        //将当前用户登录状态status设为1
                        user.setUserStatus("1");
                        user.save();
                        finish();
                    }else{
                        Toast.makeText(this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
            case R.id.sign_up:
                //转入注册地方
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
