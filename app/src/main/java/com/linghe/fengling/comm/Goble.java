package com.linghe.fengling.comm;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.linghe.fengling.entity.Song;
import com.linghe.fengling.entity.User;
import com.linghe.fengling.ui.activity.LoginActivity;
import com.linghe.fengling.ui.activity.MainActivity;
import org.litepal.LitePal;

import java.util.ArrayList;

public class Goble {
    public static ArrayList<Song> songs = new ArrayList<>();
    private static User nowUser;

    public static User getNowUser() {
        if (nowUser == null) {
//            User findUser = LitePal.where("isLogin = ?", "1").findFirst(User.class);
            User findUser = LitePal.where("isLogin = ?", "1").findFirst(User.class);
            nowUser = findUser;
        }
        return nowUser;
    }

    public static boolean isLogin(AppCompatActivity activity) {
//        User findUser = LitePal.where("isLogin = ?", "1").findFirst(User.class);
//        if (findUser == null) {
//            Intent intent = new Intent(activity, LoginActivity.class);
//            activity.startActivity(intent);
//            return false;
//        }
//        if (nowUser == null) {
//            nowUser = findUser;
//        }
        return true;
    }
}
