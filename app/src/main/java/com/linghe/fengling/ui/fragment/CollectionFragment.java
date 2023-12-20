package com.linghe.fengling.ui.fragment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.linghe.fengling.R;
import com.linghe.fengling.comm.Goble;
import com.linghe.fengling.entity.Collection;
import com.linghe.fengling.entity.Song;
import com.linghe.fengling.ui.BaseFragment;
import com.linghe.fengling.ui.adapter.SongAdapter;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends BaseFragment {
    private RecyclerView collectionList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_collection, container, false);
        collectionList = inflate.findViewById(R.id.collectionList);
        update();
        return inflate;
    }

    public void update() {
        // 获取数据库列表收藏
        List<Collection> all = LitePal.findAll(Collection.class);
        ArrayList<Song> songs = Goble.songs;
        ArrayList<Song> res = new ArrayList<>();
        for(Collection collection: all) {
            res.add(songs.get(collection.getSongIndex()));
        }
        collectionList.setAdapter(new SongAdapter(res, (AppCompatActivity) requireActivity(), this));
        collectionList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}
