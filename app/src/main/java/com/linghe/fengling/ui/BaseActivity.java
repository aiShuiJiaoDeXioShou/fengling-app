package com.linghe.fengling.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.linghe.fengling.R;
import com.linghe.fengling.entity.Setting;
import com.linghe.fengling.ui.theme.ThemeUtils;
import com.linghe.fengling.wrapper.LocalLangContextWrapper;
import org.litepal.LitePal;

public abstract class BaseActivity extends AppCompatActivity {
    protected SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 设置主题
        ThemeUtils.toggleTheme(this);
        // 初始化数据库
        LitePal.initialize(this);
        db = LitePal.getDatabase();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Setting setting = null;
        try {
            setting = LitePal.findFirst(Setting.class);
        } catch (RuntimeException e) {
            if (setting == null) {
                setting = new Setting();
                setting.setTheme(R.style.凤铃);
                setting.setLang("zh");
            }
        }

        super.attachBaseContext(LocalLangContextWrapper.wrap(newBase,setting.getLang()));
    }
}
