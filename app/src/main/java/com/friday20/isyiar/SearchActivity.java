package com.friday20.isyiar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.friday20.isyiar.adapter.SearchAdapter;
import com.friday20.isyiar.helpers.SoftKeyboardHelper;
import com.friday20.isyiar.helpers.Space;
import com.friday20.isyiar.model.Syiar;
import com.friday20.isyiar.vm.AppViewModel;

import java.util.List;


public class SearchActivity extends AppCompatActivity implements SearchAdapter.AdapterListener {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private AppViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchViewQuery);
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.onWindowFocusChanged(true);
        searchView.clearFocus();
        SoftKeyboardHelper.show(this);
        searchView.requestFocus();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new Space(20, 0));
        adapter = new SearchAdapter(this, SearchActivity.this);

        viewModel.getAllSyiars().observe(this, new Observer<List<Syiar>>() {
            @Override
            public void onChanged(@Nullable List<Syiar> entities) {
                adapter.setDataList(entities);
            }
        });
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public void onContactSelected(Syiar syiar, int position) {

    }
}