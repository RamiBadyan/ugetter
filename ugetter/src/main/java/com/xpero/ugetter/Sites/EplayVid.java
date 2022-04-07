package com.xpero.ugetter.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EplayVid {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete) {
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "<source.*\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response.toString());
                        if (matcher.find()) {
                            XModel xModel = new XModel();
                            xModel.setUrl(matcher.group(1));
                            xModel.setQuality("Normal");
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
