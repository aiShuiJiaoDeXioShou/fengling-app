package com.linghe.fengling.ui.theme;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import com.linghe.fengling.R;
import com.linghe.fengling.entity.Setting;
import com.linghe.fengling.entity.Theme;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public final class ThemeUtils {


    public static void toggleTheme(ComponentActivity activity, int theme) {
        activity.setTheme(theme);
    }

    public static void toggleTheme(ComponentActivity activity) {
        // 判断数据库是否初始化
        if (LitePal.getDatabase() == null) LitePal.initialize(activity.getApplicationContext());
        // 获取数据库当中的theme字段的值
        Setting theme;
        try {
            theme = LitePal.findFirst(Setting.class);
            toggleTheme(activity, theme.getTheme());
        } catch (RuntimeException runtimeException) {
            Setting t = new Setting(1,R.style.凤铃, "zh", 0);
            t.save();
        }
    }
}
