package com.xpero.ugetter.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import com.xpero.ugetter.Utils.Utils;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VidMoly {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete) {

        url = fixURL(url);
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "sources.*file.*\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            AndroidNetworking.get(matcher.group(1))
                                    .addHeaders("Referer", "https://vidmoly.to/")
                                    .build()
                                    .getAsString(new StringRequestListener() {
                                        @Override
                                        public void onResponse(String response1) {
                                            //RESOLUTION.*x(.*?),.*[\s\S]http(.*?)[\s\S]8
                                            String regex = "RESOLUTION.*x(.*?),.*[\\s\\S]http(.*?)8";
                                            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                                            Matcher matcher = pattern.matcher(response1);

                                            ArrayList<XModel> xModels = new ArrayList<>();
                                            while (matcher.find()) {
                                                XModel jModel = new XModel();
                                                jModel.setUrl("http"+matcher.group(2)+"8");
                                                jModel.setQuality(matcher.group(1)+"p");
                                                xModels.add(jModel);
                                            }

                                            if(xModels.size() > 1){
                                                onComplete.onTaskCompleted(xModels,true);
                                            } else {
                                                onComplete.onTaskCompleted(xModels,false);
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            anError.printStackTrace();
                                            onComplete.onError();
                                        }
                                    });
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

    private static String fixURL(String uri){
        return Utils.getDomainFromURL(uri)+"/embed-"+uri.substring(uri.lastIndexOf("/") + 1)+".html";
    }
}
