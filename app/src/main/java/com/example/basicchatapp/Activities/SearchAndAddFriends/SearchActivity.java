package com.example.basicchatapp.Activities.SearchAndAddFriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.example.basicchatapp.Utils.HelperMethods;
import com.example.basicchatapp.databinding.ActivitySearchBinding;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private List<SearchModel> searchModelList;
    private SearchAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

    }

    private void init(){
        searchModelList = new ArrayList<>();
        context = SearchActivity.this;

        // set toolbar
        setSupportActionBar(binding.toolbarSearchPeople);
        // set back feature
        binding.toolbarSearchPeople.setNavigationOnClickListener(view -> finish());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerViewSearchActivity.setLayoutManager(layoutManager);

        binding.search.setOnClickListener(view -> {
            if(binding.edittextSearchPeople.getText() != null){
                getData(binding.edittextSearchPeople.getText().toString());
            }
        });
    }

    private void getData(String username){

        SearchViewModel viewModel = new ViewModelProvider(this,
                new SearchViewModelFactory(username)).get(SearchViewModel.class);

        viewModel.getLiveDataSearchList(username).observe(this, searchModels -> {
            if (!searchModels.isEmpty()) {
                searchModelList.clear();
                searchModelList.addAll(searchModels);
                adapter = new SearchAdapter(searchModelList, SearchActivity.this);
                binding.recyclerViewSearchActivity.setAdapter(adapter);
                setVisibilities();
                HelperMethods.showShortToast(context, "not empty");
             } else {
                binding.recyclerViewSearchActivity.setVisibility(View.GONE);
                binding.linearSearchActivityNoSearchResult.setVisibility(View.VISIBLE);
                HelperMethods.showShortToast(context, "empty");
             }
        });

    }

    private void setVisibilities(){  // when there is a search result
        binding.recyclerViewSearchActivity.setVisibility(View.VISIBLE);
        binding.linearSearchActivityNoSearchResult.setVisibility(View.GONE);
    }
}