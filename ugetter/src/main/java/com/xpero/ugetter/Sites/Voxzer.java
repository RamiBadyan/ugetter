package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.LowCostVideo.agent;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

public class Voxzer {

    private static String mediaID;

    public static void fetch(final String url, final LowCostVideo.OnTaskCompleted onComplete) {

        //https://player.voxzer.org/view/691379d68d354fe6e71265e1

        AndroidNetworking.get(url)
                .setUserAgent(agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String listuri = url.replace("/view/", "/list/");
                        AndroidNetworking.get(listuri)
                                .setUserAgent(agent)
                                .addHeaders("Referer", url)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Iterator<String> keys = response.keys();
                                            ArrayList<XModel> xModels = new ArrayList<>();
                                            while(keys.hasNext()) {
                                                String key = keys.next();
                                                XModel jModel = new XModel();
                                                jModel.setUrl(response.getString(key));
                                                jModel.setQuality("Normal");
                                                xModels.add(jModel);
                                            }

                                            if(xModels.size() > 1){
                                                onComplete.onTaskCompleted(xModels,true);
                                            } else {
                                                onComplete.onTaskCompleted(xModels,false);
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

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }
}
