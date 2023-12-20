package com.linghe.fengling.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.linghe.fengling.comm.Goble;
import com.linghe.fengling.entity.Song;

import java.util.ArrayList;

public class ScanMusicUtils {
    public static ArrayList<Song> getMusicData(Context context) {
        ArrayList<Song> list = new ArrayList<>();//音乐列表
        // 访问手机里面的音乐
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC
        );
        if (cursor != null) {
            int index = 0;
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setIndex(index);
                song.setAlbumId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                song.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                song.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                song.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if (song.getSize() > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    //歌手-歌名.mp3
                    String name = song.getName();
                    if (name != null && name.contains("-")) {
                        String[] str = name.split("-");
                        //string.trim()为去除字符串首尾空格
                        song.setSinger(str[0].trim());
                        song.setName(str[1].trim());
                        //分离.mp3,.flac等后缀
                        String nameSplit=song.getName();
                        if(nameSplit!=null&&nameSplit.contains(".")) {
                            String[] strName = nameSplit.split("\\.");
                            song.setName(strName[0].trim());
                        }
                    }
                    list.add(song);
                    index++;
                }
            }
            // 释放资源
            cursor.close();
        }
        Goble.songs = list;
        return list;
    }

    // 定义一个方法用来格式化获取到的时间
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return (time / 1000 / 60) + ":0" + time / 1000 % 60;
        } else {
            return (time / 1000 / 60) + ":" + time / 1000 % 60;
        }
    }

}
