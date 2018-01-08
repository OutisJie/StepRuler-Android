package com.example.ready.stepruler.module.push.content;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.ready.stepruler.R;
import com.example.ready.stepruler.bean.push.PushArticleDataBean;
import com.example.ready.stepruler.utils.ImageLoadUtil;
import com.example.ready.stepruler.widget.listener.AppBarStateChangeListener;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by ready on 2017/12/26.
 */

public class ArticleContentFragment extends RxFragment implements IArticleContentView.view{
    private static final String TAG = "ArticleContentFragment";
    private static final String IMG = "img";
    //标题、副标题、段落
    private String articleTitle;
    private String articleSubTitle;
    private String articleContent;
    //控件
    private Toolbar toolbar;
    private WebView article_webView;
    private NestedScrollView article_scrollView;
    private SwipeRefreshLayout article_refreshLayout;
    private ContentLoadingProgressBar article_progress;
    private CollapsingToolbarLayout article_colLayout;
    private AppBarLayout article_barLayout;
    private ImageView article_imageView;
    //参数
    private Bundle bundle;
    private String imgUrl;
    private boolean isHasImage;
    private PushArticleDataBean articleDataBean;
    private IArticleContentView.presenter presenter;

    public static ArticleContentFragment newInstance(Parcelable dataBean, String imgUrl) {
        ArticleContentFragment instance = new ArticleContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(TAG, dataBean);
        args.putString(IMG, imgUrl);
        instance.setArguments(args);
        return instance;
    }
    @Override
    public <T1> LifecycleTransformer<T1> bindToLife() {
        return bindUntilEvent(FragmentEvent.DESTROY);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter((ArticleContentPresenter) presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(attachLayoutId(),container,false);
        initData();
        initView(view);
        return view;
    }

    protected int attachLayoutId() {
        imgUrl = getArguments().getString(IMG);
        isHasImage = !TextUtils.isEmpty(imgUrl);
        return isHasImage? R.layout.fragment_article_content_img: R.layout.fragment_article_content;
    }

    protected void initData() throws NullPointerException {
        bundle = getArguments();
        articleDataBean = bundle.getParcelable(TAG);
        articleTitle = articleDataBean.getArticleTitle();
        articleSubTitle = articleDataBean.getArticleSubTitle();
    }

    protected void initView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_article_option);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article_scrollView.smoothScrollTo(0, 0);
            }
        });
        article_webView = (WebView) view.findViewById(R.id.article_webView);
        initWebClient();
        article_scrollView = (NestedScrollView) view.findViewById(R.id.article_scrollView);

        article_progress = (ContentLoadingProgressBar) view.findViewById(R.id.article_progress);
        int color = getResources().getColor(R.color.Brown);
        article_progress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        article_progress.show();
        presenter.doLoadHtml(articleDataBean);
        article_progress.hide();
        article_refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.article_refreshLayout);
        article_refreshLayout.setColorSchemeColors(getResources().getColor(R.color.Grey));
        article_refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                article_refreshLayout.setRefreshing(false);
            }
        });

        if (isHasImage) {
            article_barLayout = (AppBarLayout) view.findViewById(R.id.article_barLayout);
            article_colLayout = (CollapsingToolbarLayout) view.findViewById(R.id.article_colLayout);
            article_imageView = (ImageView) view.findViewById(R.id.article_image);
        }
        if (isHasImage) {
            ImageLoadUtil.loadCenterCrop(getActivity(), bundle.getString(IMG), article_imageView, R.mipmap.error_image, R.mipmap.error_image);
            article_barLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                @Override
                public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
                    if (state == State.EXPANDED) {
                        // 展开状态
                        article_colLayout.setTitle(articleTitle);
                        toolbar.setBackgroundColor(Color.TRANSPARENT);
                        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(articleTitle);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        }
                    } else if (state == State.COLLAPSED) {
                        // 折叠状态
                    } else {
                        // 中间状态
                        article_colLayout.setTitle(articleTitle);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        }
                    }
                }
            });
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(articleTitle);
        }
        setHasOptionsMenu(true);
    }

    private void initWebClient(){
        WebSettings settings = article_webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启DOM storage API功能
        settings.setDomStorageEnabled(true);
        // 开启application Cache功能
        settings.setAppCacheEnabled(true);
        // 判断是否为无图模式
        settings.setBlockNetworkImage(!isHasImage);
        // 不调用第三方浏览器即可进行页面反应
        article_webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    view.loadUrl(url);
                }
                return true;
            }
        });

        article_webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) && article_webView.canGoBack()) {
                    article_webView.goBack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSetWebView(String url, boolean flag) {
        if(flag) {
            article_webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        }
    }

    @Override
    public void setPresenter(ArticleContentPresenter presenter) {
        if(presenter == null){
            this.presenter = new ArticleContentPresenter(this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_article_option,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_comment:
                //NewsCommentActivity.launch(bean.getGroup_id() + "", bean.getItem_id() + "");
                break;
            case R.id.action_share:
                //IntentAction.send(getActivity(), shareTitle + "\n" + shareUrl);
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
