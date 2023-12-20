package com.linghe.fengling.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.linghe.fengling.R;
import com.linghe.fengling.entity.User;
import com.linghe.fengling.service.MusicService;
import com.linghe.fengling.ui.BaseActivity;
import com.linghe.fengling.ui.fragment.CollectionFragment;
import com.linghe.fengling.ui.fragment.LocalMusicFragment;
import com.linghe.fengling.ui.fragment.SettingFragment;
import org.litepal.LitePal;

public class MainActivity extends BaseActivity {

    public DrawerLayout drawerLayout;
    private TextView username;
    private NavigationView navigationView;  // 导航视图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取页面(XML)中的元素
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        username = navigationView.getHeaderView(0).findViewById(R.id.name);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(new LocalMusicFragment());
                    return true;
                case R.id.navigation_dashboard:
                    switchFragment(new CollectionFragment());
                    return true;
                case R.id.navigation_notifications:
                    switchFragment(new SettingFragment());
                    return true;

                default:
                    return false;
            }
        });
        switchFragment(new LocalMusicFragment());
        userLogin();
        checkWritePermission();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.themeBg) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);  // 启动设置界面
            } else if (item.getItemId() == R.id.gd) {

            }
            return true;
        });
    }

    private void userLogin() {
        // 查询是否有登录记录
        User findUser = LitePal.where("userStatus = ?","1").findFirst(User.class);

        // 显示用户名
        if (findUser == null) {
            username.setText(R.string.login);
        } else {
            username.setText(findUser.getUserName());
        }

        // 设置登录点击事件
        username.setOnClickListener(e-> {
            if (findUser == null) {
                // 进入登录页面
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                // 进入个人信息页面
                username.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("number", findUser.getUserNumber());
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }
        });


    }
    //检查文件读写权限
    private void checkWritePermission(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //没有权限则申请权限
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        userLogin();
    }
}
