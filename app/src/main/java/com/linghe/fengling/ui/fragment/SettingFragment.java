package com.linghe.fengling.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.linghe.fengling.R;
import com.linghe.fengling.comm.Config;
import com.linghe.fengling.entity.Setting;
import com.linghe.fengling.ui.BaseFragment;
import org.litepal.LitePal;

import java.util.List;

public class SettingFragment extends BaseFragment {

    private List<String> languageValuesArray;
    private List<Integer> themeValuesArray;
    private Setting setting;
    private Button saveSetting;
    private Button cancelSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        // 使用LitePal获取系统设置
        setting = LitePal.findFirst(Setting.class);

        // 确定选项的值
        languageValuesArray = Config.languageValuesArray;
        themeValuesArray = Config.themeValuesArray;

        // 获取选项元素
        saveSetting = rootView.findViewById(R.id.setting_save);
        cancelSetting = rootView.findViewById(R.id.setting_cancel);

        // 创建对应的适配器，当选项改变的时候，里面的值发生改变
        Spinner languageSpinner = rootView.findViewById(R.id.languageSpinner);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.language_entries,
                android.R.layout.simple_spinner_item
        );
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        // 创建对应的适配器，当选项改变的时候，里面的值发生改变
        Spinner themeSpinner = rootView.findViewById(R.id.themeSpinner);
        ArrayAdapter<CharSequence> themeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.theme_entries,
                android.R.layout.simple_spinner_item
        );
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);

        // 监听语言的变化
        languageSpinner.setOnItemSelectedListener(createOnItemSelectedListener("Language"));
        themeSpinner.setOnItemSelectedListener(createOnItemSelectedListener("Theme"));
        saveSetting.setOnClickListener(e-> {
            boolean b = setting.saveOrUpdate();
            if (b) {
                Toast.makeText(requireContext(), "设置更改成功，请重新进入应用！", Toast.LENGTH_SHORT).show();
            }
        });

        // 回显从数据库中取得的默认选项
        languageSpinner.setSelection(languageValuesArray.indexOf(setting.getLang()));
        themeSpinner.setSelection(themeValuesArray.indexOf(setting.getTheme()));

        return rootView;
    }

    private AdapterView.OnItemSelectedListener createOnItemSelectedListener(final String itemType) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (itemType.equals("Language")) {
                    setting.setLang(languageValuesArray.get(position));
                } else if (itemType.equals("Theme")) {
                    setting.setTheme(themeValuesArray.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 什么也不做
            }
        };
    }
}
