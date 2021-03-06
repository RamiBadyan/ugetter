package com.xpero.ugetter.Sites;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import java.util.ArrayList;

import static com.xpero.ugetter.LowCostVideo.agent;
import static com.xpero.ugetter.Utils.FacebookUtils.getFbLink;
import static com.xpero.ugetter.Utils.Utils.putModel;

public class FB {
    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onTaskCompleted){
        AndroidNetworking.post("https://fbdown.net/download.php")
                .addBodyParameter("URLz", "https://www.facebook.com/video.php?v="+ url)
                .addHeaders("User-agent", agent)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<XModel> xModels = new ArrayList<>();
                        String sd = getFbLink(response, false);
                        if (sd!=null){
                            putModel(sd,"SD",xModels);
                        }
                        String hd = getFbLink(response, true);
                        if (hd!=null) {
                            putModel(hd, "HD", xModels);
                        }
                        if (xModels.isEmpty()){
                            onTaskCompleted.onError();
                        }else onTaskCompleted.onTaskCompleted(xModels,true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        onTaskCompleted.onError();
                    }
                });
    }
}
