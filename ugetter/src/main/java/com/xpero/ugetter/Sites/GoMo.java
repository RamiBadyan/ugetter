package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.LowCostVideo.agent;
import static com.xpero.ugetter.Utils.Utils.putModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import com.xpero.ugetter.Utils.JSUnpacker;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoMo {

    private static ArrayList<XModel> videoModels;

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.get(url)
                .setUserAgent(agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String src = getUrl(response);
                            videoModels = new ArrayList<>();
                            putModel(src,"Normal", videoModels);
                            if (videoModels !=null) {
                                if(videoModels.size() == 0){
                                    onComplete.onError();
                                } else onComplete.onTaskCompleted(videoModels, videoModels.size() > 1);
                            }else onComplete.onError();
                        } catch (Exception A){
                            onComplete.onError();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        onComplete.onError();
                    }
                });

    }

    private static String getUrl(String html){
        Pattern pattern = Pattern.compile("eval\\(function\\(p,a,c,k,e,d\\)(.*?)split");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            String jsPacked = "eval(function(p,a,c,k,e,d)"+matcher.group(1)+"split('|')))";
             JSUnpacker jsUnpacker = new JSUnpacker(jsPacked);
                String Unpacked = jsUnpacker.unpack();
                pattern = Pattern.compile("file:\"(.*?)\"");
                matcher = pattern.matcher(Unpacked);
                while (matcher.find()) {
                    if(matcher.group(1).contains("http")){
                        return matcher.group(1);
                    } else {
                        return "https:"+matcher.group(1);
                    }

                }
        }
        return null;
    }
}
