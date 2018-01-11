package com.example.ready.stepruler.module.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.adapter.MyAdapter;
import com.example.ready.stepruler.adapter.MyArticleAdapter;
import com.example.ready.stepruler.api.ArticleApi;
import com.example.ready.stepruler.api.UserApi;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.utils.AppManager;
import com.example.ready.stepruler.utils.RetrofitFactory;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by single dog on 2017/12/28.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    //控件
    private Spinner spinner;
    private SearchView searchView;
    private Button searchButton;
    private ListView listView;

    //查询数据
    private String searchSelect;
    private String searchData;
    //服务器返回数据
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<PushArticleDataBean> articles = new ArrayList<>();


    private UserApi userApi = RetrofitFactory.getRetrofit().create(UserApi.class);
    private ArticleApi articleApi = RetrofitFactory.getRetrofit().create(ArticleApi.class);

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.search);
        AppManager.getAppManager().addActivity(this);
        initView();
        setListener();
    }

    private void initView() {
        spinner = (Spinner) findViewById(R.id.search_spinner);
        searchView = (SearchView) findViewById(R.id.search_searchView);
        searchButton = (Button) findViewById(R.id.search_searchButton);
        listView = (ListView) findViewById(R.id.search_list);
    }

    private void setListener() {
        searchButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchSelect = getResources().getStringArray(R.array.search_list)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchData = newText;
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_searchButton:
                if (searchSelect.equals("用户") && !TextUtils.isEmpty(searchData)) {
                    Call<ArrayList<String>> call = userApi.search(searchData);
                    call.enqueue(new Callback<ArrayList<String>>() {
                        @Override
                        public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                            names.clear();
                            names = response.body();
                            ArrayList<HashMap<String, Object>> data = new ArrayList<>();
                            for (int i = 0; i < names.size(); i++) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("username", names.get(i));
                                data.add(hashMap);
                            }
                            MyAdapter myAdapter = new MyAdapter(SearchActivity.this, data);
                            listView.setAdapter(myAdapter);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                } else if (searchSelect.equals("文章") && !TextUtils.isEmpty(searchData)) {
                    Call<ArrayList<PushArticleDataBean>> call = articleApi.search("黄河");
                    call.enqueue(new Callback<ArrayList<PushArticleDataBean>>() {
                        @Override
                        public void onResponse(Call<ArrayList<PushArticleDataBean>> call, Response<ArrayList<PushArticleDataBean>> response) {
                            articles.clear();
                            articles = response.body();
                            ArrayList<HashMap<String, Object>> data = new ArrayList<>();
                            for (int i = 0; i < articles.size(); i++) {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("article_title", articles.get(i).getArticleTitle());
                                hashMap.put("article_subtitle", articles.get(i).getArticleSubTitle());
                                hashMap.put("article_content", articles.get(i).getArticleBodyhtml());
                                hashMap.put("article_img", articles.get(i).getArticleImg());
                                hashMap.put("article_imgs", articles.get(i).getArticleImgs());
                                hashMap.put("article_text", articles.get(i).getArticleText());
                                data.add(hashMap);
                            }
                            MyArticleAdapter myArticleAdapter = new MyArticleAdapter(SearchActivity.this, data);
                            listView.setAdapter(myArticleAdapter);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<PushArticleDataBean>> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().finishActivity(this);
    }
}
