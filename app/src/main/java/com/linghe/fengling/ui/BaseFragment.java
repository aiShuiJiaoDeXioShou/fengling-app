package com.linghe.fengling.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.linghe.fengling.ui.theme.ThemeUtils;
import org.litepal.LitePal;

public abstract class BaseFragment extends Fragment {
    protected SQLiteDatabase db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // 设置主题
        ThemeUtils.toggleTheme(requireActivity());
        // 初始化数据库
        LitePal.initialize(requireContext());
        db = LitePal.getDatabase();
        super.onCreate(savedInstanceState);
    }

}
