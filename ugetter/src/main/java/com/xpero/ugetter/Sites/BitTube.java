package com.xpero.ugetter.Sites;

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

public class BitTube {

    private static String getBitTubeID(String string){
        final String regex = "(embed|watch)/(.+)";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(2).replaceAll("&|/","");
        }
        return null;
    }

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        String id = getBitTubeID(url);
        if (id!=null) {
            AndroidNetworking.get("https://bittube.video/api/v1/videos/" + id)
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
                            onTaskCompleted.onError();
                        }
                    });
        }else onTaskCompleted.onError();
    }

    private static ArrayList<XModel> parseVideo(String html){

        ArrayList<XModel> xModels = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(html).getJSONArray("files");
            for (int i=0;i<array.length();i++){
                String label = array.getJSONObject(i).getJSONObject("resolution").getString("label");
                String src = array.getJSONObject(i).getString("fileDownloadUrl");
                if (label.length()>1) {
                    XModel jModel = new XModel();
                    jModel.setQuality(label);
                    jModel.setUrl(src);
                    xModels.add(jModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return xModels;
    }
}
