package com.linghe.fengling.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.linghe.fengling.R;
import com.linghe.fengling.comm.Goble;
import com.linghe.fengling.entity.Collection;
import com.linghe.fengling.entity.Song;
import com.linghe.fengling.ui.activity.PlayDetailActivity;
import com.linghe.fengling.ui.fragment.CollectionFragment;
import org.litepal.LitePal;

import java.util.List;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private AppCompatActivity activity;
    private List<Song> mSong;

    private CollectionFragment fragment = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View songView;
        ImageView songImage;
        TextView songName;
        TextView singer;
        TextView songDuration;
        TextView songAlbum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songView = itemView;
            songImage = itemView.findViewById(R.id.ivSongImage);
            songName = itemView.findViewById(R.id.tvSongName);
            singer = itemView.findViewById(R.id.tvSonger);
            songDuration = itemView.findViewById(R.id.tvSongTime);
            songAlbum = itemView.findViewById(R.id.album);
        }
    }

    public SongAdapter(List<Song> songList, AppCompatActivity activity) {
        mSong = songList;
        this.activity = activity;
    }

    public SongAdapter(List<Song> songList, AppCompatActivity activity, CollectionFragment fragment) {
        mSong = songList;
        this.activity = activity;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_component, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 获取音乐选项
        Song song = mSong.get(position);
        // 对view进行设置
        holder.singer.setText(song.getSinger());
        holder.songName.setText(song.getName());
        holder.songAlbum.setText(song.getAlbum());
        holder.songDuration.setText(changDuration(song.getDuration()));
        holder.songImage.setImageResource(R.drawable.icon_music);

        // 处理选项点击事件
        holder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("position", song.getIndex());
            bundle.putString("musicName", song.getName());
            Intent intent = new Intent();
            intent.putExtras(bundle);
            // 跳转到播放详情页面
            intent.setClass(activity, PlayDetailActivity.class);
            activity.startActivity(intent);
        });

        // 为 itemView 设置长按监听器
        holder.itemView.setOnLongClickListener(v -> {
            // 显示弹出菜单
            showPopupMenu(holder.itemView, song);
            return true;
        });
    }

    // 显示弹出菜单的方法
    private void showPopupMenu(View view, Song song) {
        PopupMenu popupMenu = new PopupMenu(activity.getApplicationContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        // 设置弹出菜单的点击监听器
        popupMenu.setOnMenuItemClickListener(item -> {
//            Goble.isLogin(activity);
            int itemId = item.getItemId();

            if (itemId == R.id.collection) {
                Collection collection = new Collection();
                collection.setSongId(song.getId());
                collection.setSongIndex(song.getIndex());
//                collection.setUserId(Goble.getNowUser().getId());
                collection.setUserId(-1);
                collection.save();
                Toast.makeText(activity.getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT);
                if (this.fragment != null) {
                    fragment.update();
                }
                return true;
            } else if (itemId == R.id.unfavorite) {
                LitePal.deleteAll(Collection.class, "songId=?", song.getId() + "");
                if (this.fragment != null) {
                    fragment.update();
                }
                return true;
            }
            return false;
        });
        // 显示弹出菜单
        popupMenu.show();
    }


    public String changDuration(int duration) {
        String timeNow = "";
        Integer minute = Integer.valueOf(duration / 60000);
        Integer seconds = Integer.valueOf(duration % 60000);
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            timeNow += "0";
        }
        timeNow += minute + ":";

        if (second < 10) {
            timeNow += "0";
        }
        timeNow += second;
        return timeNow;
    }

    @Override
    public int getItemCount() {
        return mSong.size();
    }

}
