package com.xpero.ugetter.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xpero.ugetter.LowCostVideo.agent;
import static com.xpero.ugetter.Utils.FacebookUtils.getFbLink;
import static com.xpero.ugetter.Utils.Utils.putModel;

public class MFire {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.get(fixURL(url))
                .addHeaders("User-agent", agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "aria-label=\"Download file\"\\n.+href=\"(.*)\"";
                        final Pattern pattern = Pattern.compile(regex);
                        final Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            ArrayList<XModel> xModels = new ArrayList<>();
                            putModel(matcher.group(1),"Normal",xModels);
                            onTaskCompleted.onTaskCompleted(xModels,false);
                        }else onTaskCompleted.onError();
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }

    private static String fixURL(String url){
        if (!url.startsWith("https")){
            url = url.replace("http","https");
        }
        return url;
    }
}
