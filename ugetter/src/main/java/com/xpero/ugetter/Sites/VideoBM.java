package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.Utils.Utils.getDomainFromURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoBM {

    @SuppressLint("JavascriptInterface")
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        url = fixURL(url);
        if (url!=null) {
            AndroidNetworking.get(url)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            ArrayList<XModel> jModels = parse(response);
                            if (jModels!=null) {
                                onTaskCompleted.onTaskCompleted(jModels, false);
                            }else onTaskCompleted.onError();
                        }

                        @Override
                        public void onError(ANError anError) {
                            onTaskCompleted.onError();
                        }
                    });
        }else onTaskCompleted.onError();
    }

    private static String fixURL(String url){
        if (!url.contains("embed-")) {
            final String regex = "org/([^']*)";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                String id = matcher.group(1);
                if (id.contains("/")) {
                    id = id.substring(0, id.lastIndexOf("/"));
                }
                url = getDomainFromURL(url)+"/embed-" + id;
            } else {
                return null;
            }
        }
        return url;
    }

    private static ArrayList<XModel> parse(String response){

        final Pattern pattern = Pattern.compile("sources.*file.*?\"(.*?)\",", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            XModel jModel = new XModel();
            jModel.setUrl(matcher.group(1));
            jModel.setQuality("Normal");
            ArrayList<XModel> jModels = new ArrayList<>();
            jModels.add(jModel);
            return jModels;
        }

        return null;
    }

}
