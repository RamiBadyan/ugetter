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

public class HDVid {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        AndroidNetworking.get(url)
                .setUserAgent(agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace(" ", "").replace("\n", "");
                        Pattern pattern = Pattern.compile("file:\"(.*?)\",");
                        Matcher matcher = pattern.matcher(response);
                        if(matcher.find()) {
                            XModel xModel = new XModel();
                            xModel.setUrl(matcher.group(1));
                            pattern = Pattern.compile("label:\"(.*?)\"");
                            matcher = pattern.matcher(response);
                            if(matcher.find()){
                                xModel.setQuality(matcher.group(1)+"p");
                            } else {
                                xModel.setQuality("Normal");
                            }

                            ArrayList<XModel> xModels = new ArrayList<>();
                            xModels.add(xModel);
                            onComplete.onTaskCompleted(xModels,false);
                        } else {
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
