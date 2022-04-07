package com.xpero.ugetter.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.Utils.DailyMotionUtils;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import static com.xpero.ugetter.Utils.Utils.putModel;
import static com.xpero.ugetter.Utils.Utils.sortMe;

import org.json.JSONArray;
import org.json.JSONObject;

public class DailyMotion {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){

        String meta_url = "https://www.dailymotion.com/player/metadata/video/"+getMediaID(url);

        AndroidNetworking.get(meta_url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("qualities");
                            JSONArray contacts = obj.getJSONArray("auto");
                            ArrayList<XModel> models = new ArrayList<>();
                            for (int i = 0; i < contacts.length(); i++) {
                                String url = contacts.getJSONObject(i).getString("url");
                                putModel(url, "Default", models);
                            }

                            if(models.size() > 1){
                                onTaskCompleted.onTaskCompleted(sortMe(models), true);
                            } else {
                                onTaskCompleted.onTaskCompleted(models, false);
                            }

                        } catch (Exception Error){
                            Error.printStackTrace();
                            onTaskCompleted.onError();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static String getMediaID(String url){
        url = url.substring(url.lastIndexOf("/") + 1);
        return url;
    }}
