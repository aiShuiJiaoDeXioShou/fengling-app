package com.linghe.fengling.ui.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.linghe.fengling.R;
import com.linghe.fengling.comm.Goble;
import com.linghe.fengling.entity.Song;
import com.linghe.fengling.ui.BaseActivity;
import com.linghe.fengling.ui.adapter.SongAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends BaseActivity {
    private RecyclerView searchMusics;
    private SearchView searchMusicsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sreach);
        searchMusics = findViewById(R.id.searchMusics);

        // 获取搜索框，搜索框展开
        searchMusicsView = findViewById(R.id.searchMusicsView);
        searchMusicsView.setIconified(false);

        ArrayList<Song> songList = Goble.songs;
        Toast.makeText(this, "搜索到" + songList.size() + "首歌曲", Toast.LENGTH_SHORT).show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        SongAdapter songAdapter = new SongAdapter(songList, this);
        searchMusics.setAdapter(songAdapter);
        searchMusics.setLayoutManager(linearLayoutManager);

        searchMusicsView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Song> filteredList = songList.stream()
                        .filter(song -> song.getName().toLowerCase().contains(s.toLowerCase()))
                        .collect(Collectors.toList());
                searchMusics.setAdapter(new SongAdapter(filteredList, SearchActivity.this));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
