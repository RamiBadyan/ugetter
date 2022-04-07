package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.Utils.Utils.putModel;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YodBox {

    private static ArrayList<XModel> videoModels;

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.get(url)
                .setUserAgent(LowCostVideo.agent)
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
        Pattern pattern = Pattern.compile("<source.*src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
