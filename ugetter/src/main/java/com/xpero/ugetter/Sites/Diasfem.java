package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.Utils.Utils.getDomainFromURL;
import static com.xpero.ugetter.Utils.Utils.sortMe;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseListener;
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

import okhttp3.Response;

public class Diasfem {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        final String id = get_fEmbed_video_ID(url);
        final String domain = getDomainFromURL(url);

        if (id!=null) {
            AndroidNetworking.get(url)
                    .build()
                    .getAsOkHttpResponse(new OkHttpResponseListener() {
                        @Override
                        public void onResponse(Response response) {

                            AndroidNetworking.post(domain+"/api/source/"+id)
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response) {
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

                        }

                        @Override
                        public void onError(ANError anError) {
                            onComplete.onError();
                        }
                    });
        } else onComplete.onError();
    }

    private static ArrayList<XModel> parse(String response){
        ArrayList<XModel> jModels = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("data")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Utils.putModel(object.getString("file"),object.getString("label"),jModels);
                }
                return jModels;
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
