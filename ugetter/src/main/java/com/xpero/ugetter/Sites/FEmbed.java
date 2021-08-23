package com.xpero.ugetter.Sites;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import com.xpero.ugetter.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xpero.ugetter.Utils.Utils.sortMe;

/*
This is direct link getter for Fembed
    By
Khun Htetz Naing
 */

public class FEmbed {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
       // String id = get_fEmbed_video_ID(url);
        Log.d("libraryGet",url);
        String newUrl = getUrl(url);
        Log.d("libraryGet",newUrl);
        if (newUrl!=null){
            AndroidNetworking.post(newUrl)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("libraryGet",response);
                            ArrayList<XModel> xModels = parse(response);
                            if (xModels!=null){
                                onComplete.onTaskCompleted(sortMe(xModels),true);
                            }else onComplete.onError();
                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        }else onComplete.onError();
    }

    private static String getUrl(String url) {
        return url.replace("fembed","");
    }

    private static ArrayList<XModel> parse(String response){
        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Utils.putModel(object.getString("file"),object.getString("label"),xModels);
                }
                return xModels;
            }else return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String get_fEmbed_video_ID(String string){
        final String regex = "(v|f)(\\/|=)(.+)(\\/|&)?";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(3).replaceAll("&|/","");
        }
        return null;
    }
}
