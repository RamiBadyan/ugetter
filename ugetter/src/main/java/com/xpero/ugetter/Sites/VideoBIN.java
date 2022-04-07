package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.Utils.Utils.getDomainFromURL;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import com.xpero.ugetter.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoBIN {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(url)
                .addHeaders("User-Agent", LowCostVideo.agent)
                .addHeaders("content-type", "application/json")
                .addHeaders("Connection","close")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = parseVideo(response);
                        if (xModels.isEmpty()) {
                            onTaskCompleted.onError();
                        } else onTaskCompleted.onTaskCompleted(Utils.sortMe(xModels), true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        onTaskCompleted.onError();
                    }
                });
    }

    private static String quality(int size,int index){
        List<String> quality = new ArrayList<>();
        switch (size){
            case 1:quality.add("480p");break;
            case 2:quality.add("720p");quality.add("480p");break;
            case 3:quality.add("1080p");quality.add("720p");quality.add("480p");break;
            case 4:quality.add("Higher");quality.add("1080p");quality.add("720p");quality.add("480p");break;
        }
        return quality.get(index);
    }

    private static ArrayList<XModel> parseVideo(String html){
        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            String regex = "sources:(.*),";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()){
                String source = matcher.group(1).trim();
                JSONArray array = new JSONArray(source);
                List<String> list = new ArrayList<>();
                for (int i=0;i<array.length();i++){
                    String src = array.getString(i);
                    if (!src.endsWith(".m3u8")){
                        list.add(src);
                    }
                }

                for (int i=0;i<list.size();i++){
                    String label = quality(list.size(),i);
                    XModel jModel = new XModel();
                    jModel.setQuality(label);
                    jModel.setUrl(list.get(i));
                    xModels.add(jModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return xModels;
    }

    private static String fixURL(String url){
        if (!url.contains("embed-")) {
            final String regex = "co/([^']*)";
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

}
