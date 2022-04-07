package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.LowCostVideo.agent;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Aparat {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){

        String fixurl = "https://www.aparat.com/api/fa/v1/video/video/show/videohash/"+url.substring(url.lastIndexOf('/') + 1);
        AndroidNetworking.get(fixurl)
                .setUserAgent(agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final String regex = "profile.?:.?\"(.*?)\"(?:.).*?urls.?:.*?\"(.*?)\"";
                            final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
                            final Matcher matcher = pattern.matcher(response);

                            ArrayList<XModel> xModel = new ArrayList<>();

                            while(matcher.find()){

                                XModel jModel = new XModel();
                                jModel.setUrl(matcher.group(2).replace("\\/", "/"));
                                jModel.setQuality(matcher.group(1));
                                xModel.add(jModel);
                            }

                            if(xModel.size() > 1){
                                onComplete.onTaskCompleted(xModel, true);
                            } else {
                                onComplete.onTaskCompleted(xModel, false);
                            }
                        } catch (Exception A){
                            onComplete.onError();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }
}
