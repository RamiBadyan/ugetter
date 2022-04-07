package com.xpero.ugetter.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import com.xpero.ugetter.Utils.JSUnpacker;
import com.xpero.ugetter.Utils.Utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Upstream {
    private static String UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4136.7 Safari/537.36";

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(url)
                .setUserAgent(UserAgent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> jModels = parseVideo(response);
                        if (jModels==null){
                            onTaskCompleted.onError();
                        }else onTaskCompleted.onTaskCompleted(jModels, false);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static ArrayList<XModel> parseVideo(String response){
        JSUnpacker jsUnpacker = new JSUnpacker(getEvalCode(response));
        if(jsUnpacker.detect()) {
            String src = getSrc(jsUnpacker.unpack());
            if (src!=null && src.length()>0){
                ArrayList<XModel> jModels = new ArrayList<>();
                Utils.putModel(src,"Normal",jModels);
                if (!jModels.isEmpty()){
                    return jModels;
                }
            }
        }
        return null;
    }

    private static String getSrc(String code){
        final String regex = "file:\"(.*?)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String getEvalCode(String html){
        Pattern pattern = Pattern.compile("eval\\(function(.*?)split");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String jsPacked = "eval(function"+matcher.group(1)+"split('|')))";
            return jsPacked;
        }
        return null;
    }
}
