package com.linghe.fengling.ui.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.linghe.fengling.R;
import com.linghe.fengling.comm.Goble;
import com.linghe.fengling.entity.Song;
import com.linghe.fengling.service.MusicService;
import com.linghe.fengling.ui.BaseFragment;
import com.linghe.fengling.ui.activity.MainActivity;
import com.linghe.fengling.ui.activity.PlayDetailActivity;
import com.linghe.fengling.ui.activity.SearchActivity;
import com.linghe.fengling.ui.adapter.SongAdapter;
import com.linghe.fengling.utils.ScanMusicUtils;

import java.util.ArrayList;

public class LocalMusicFragment extends BaseFragment {

    private ArrayList<Song> songList = new ArrayList<>();  // 歌曲列表
    private TextView info_buttom;  // 底部歌曲信息
    private ImageView album_bottom;  // 底部专辑图
    private LinearLayout nowSong;  // 当前播放歌曲
    private MusicReceiver mReceiver;  // 音乐播放广播接收器
    private LinearLayoutManager layoutManager;
    private View rootView;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            MainActivity mainActivity = (MainActivity) requireActivity();// 打开侧边栏
            mainActivity.drawerLayout.openDrawer(GravityCompat.START);
        } else if (itemId == R.id.search) {
            // 处理搜索按钮点击事件
            Intent intent = new Intent(requireContext(), SearchActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.reload) {
            Toast.makeText(requireContext(), "正在搜索本地音乐", Toast.LENGTH_SHORT).show();
            loadSongList();  // 重新加载本地歌曲列表
            Toast.makeText(requireContext(), "搜索到" + songList.size() + "首歌曲", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.local_music_fragment, container, false);

        // 初始化Toolbar
        toolbar = rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        mReceiver = new MusicReceiver(new Handler());
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction(MusicService.MAIN_UPDATE_UI);
        // 注册音乐播放广播接收器
        requireActivity().registerReceiver(mReceiver, itFilter);
        // 检查文件读写权限并加载歌曲列表
        checkWritePermission();

        return rootView;
    }


    private class MusicReceiver extends BroadcastReceiver {
        private final Handler handler;

        public MusicReceiver(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            // 加载音乐列表
            if (Goble.songs.isEmpty()) handler.post(LocalMusicFragment.this::loadSongList);
            loadCurrent();
        }
    }

    // 加载搜索到的歌
    public void loadSongList() {
        songList = ScanMusicUtils.getMusicData(requireContext()); // 初始化获取歌曲信息
        SongAdapter songAdapter = new SongAdapter(songList, (AppCompatActivity) requireActivity()); // 歌曲适配器
        RecyclerView recyclerView = rootView.findViewById(R.id.mRecyclerView);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(songAdapter); // 加载歌曲列表

        loadCurrent();

    }

    private void loadCurrent() {
        // 加载底部控制栏布局
        info_buttom = rootView.findViewById(R.id.CurrentTitle);
        //底部专辑图
        album_bottom = rootView.findViewById(R.id.album_bottom);
        if (MusicService.mlastPlayer != null) {

            String nowSongInfo = songList.get(MusicService.mPosition).getName() + " - " + songList.get(MusicService.mPosition).getSinger();
            info_buttom.setText(nowSongInfo);//更新歌手歌名
            loadingCover(songList.get(MusicService.mPosition).getPath());//获取专辑图
            nowSong = rootView.findViewById(R.id.nowSong);

            nowSong.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                int position = MusicService.mPosition;
                bundle.putInt("position", position);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(requireContext(), PlayDetailActivity.class);
                startActivity(intent);
            });

        } else {
            info_buttom.setText(R.string.no_songs_played);
        }
    }

    // 获取本地歌曲专辑图片
    private void loadingCover(String mediaUri) {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        Log.d("检查", "loadingCover: ");
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        if (picture != null) {//如果该歌曲有专辑图则显示
            bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            album_bottom.setImageBitmap(bitmap);
        } else {//没有专辑图则显示默认图片
            album_bottom.setImageResource(R.drawable.playing);
        }
    }

    // 检查文件读写权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongList();
            } else {
                Toast.makeText(requireContext(), "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }
    }

    // 检查文件读写权限
    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限则申请权限
            ActivityCompat.requestPermissions(requireActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            loadSongList();
        }
    }

    @Override
    public void onResume() {
        // 返回该fragment加载歌曲列表
        super.onResume();
        // 返回fragment时，定位到上一次点歌的位置
        layoutManager.scrollToPositionWithOffset(MusicService.mPosition, 0);
    }
}
