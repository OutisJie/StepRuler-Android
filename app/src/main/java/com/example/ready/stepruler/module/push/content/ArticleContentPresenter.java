package com.example.ready.stepruler.module.push.content;

import android.text.TextUtils;

import com.example.ready.stepruler.bean.push.PushArticleDataBean;

/**
 * Created by ready on 2017/12/26.
 */

public class ArticleContentPresenter implements IArticleContentView.presenter {

    private IArticleContentView.view view;

    ArticleContentPresenter(IArticleContentView.view view){
        this.view = view;
    }

    public void doRefresh() {}

    @Override
    public void doLoadHtml(PushArticleDataBean dataBean) {
        String title = dataBean.getArticleTitle();
        String subtitle = dataBean.getArticleSubTitle();
        String html = "<!DOCTYPE html>";
        String body = "";

        String[] content = dataBean.getArticleText().split("[$]");
        for(int i = 0; i < content.length; i ++){
            content[i] = "<P>" + content[i] + "</P>\n";
        }
        if(dataBean.getArticleImgs() != null) {
            String[] imgUrls = dataBean.getArticleImgs().split("[$]");
            for(int i = 0; i < imgUrls.length; i ++){
                imgUrls[i] = " <div><img style=\"display:        ;max-width:100%;\" src=\" " +  imgUrls[i] + "\"></div>";
            }
            if(content.length >= imgUrls.length){
                String temp = content[0] ;
                for(int i = 0; i < imgUrls.length - 1; i ++){
                    temp = temp + imgUrls[i] + content[i + 1] ;
                }
                temp = temp + imgUrls[imgUrls.length - 1];
                for(int i = imgUrls.length; i < content.length; i ++){
                    temp = temp + content[i];
                }

                body = temp;
            }else {
                String temp = content[0] ;
                for(int i = 0; i < content.length - 1; i ++){
                    temp = temp + imgUrls[i] + content[i + 1];
                }
                for(int i = content.length + 1; i < imgUrls.length; i ++){
                    temp = temp + imgUrls[i + 1];
                }
                body = temp;
            }
        }else {
            String temp = content[0] ;
            for(int i = 0; i < content.length - 1; i ++){
                temp = temp + content[i + 1];
            }
            body = temp;
        }

        if(body != null && (!TextUtils.isEmpty(subtitle) || subtitle != null) ){
            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/toutiao_light.css\" type=\"text/css\">";

            html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">" +
                    css +
                    "<body>\n" +
                    "<article class=\"article-container\">\n" +
                    "    <div class=\"article__content article-content\">" +
                    "<h1 class=\"article-title\">" +
                    title +
                    "</h1>" +
                    "<h1 class=\"article-title\">" +
                    subtitle +
                    "</h1>" +
                    body +
                    "    </div>\n" +
                    "</article>\n" +
                    "</body>\n" +
                    "</html>";
            view.onSetWebView(html, true);
        }else if(body != null && TextUtils.isEmpty(subtitle)){
            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/toutiao_light.css\" type=\"text/css\">";

            html = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">" +
                    css +
                    "<body>\n" +
                    "<article class=\"article-container\">\n" +
                    "    <div class=\"article__content article-content\">" +
                    "<h1 class=\"article-title\">" +
                    title +
                    "</h1>" +
                    body +
                    "    </div>\n" +
                    "</article>\n" +
                    "</body>\n" +
                    "</html>";
            view.onSetWebView(html, true);
        }else {
            view.onSetWebView(html, false);
        }

    }
}
