package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.Utils.Utils.putModel;

import android.os.StrictMode;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Archive {

    public static void fetch(final String urls, final LowCostVideo.OnTaskCompleted onComplete){

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(urls);
            HttpURLConnection ucon = null;
            ucon = (HttpURLConnection) url.openConnection();
            ucon.setRequestProperty("Referer", urls);
            ucon.setRequestProperty("User-Agent", LowCostVideo.agent);
            ucon.setInstanceFollowRedirects(false);
            URL secondURL = new URL(ucon.getHeaderField("Location"));
            String finallink = secondURL.toString();

            ArrayList<XModel> videoModels = new ArrayList<>();
            putModel(finallink,"Normal", videoModels);
            if (videoModels !=null) {
                if(videoModels.size() == 0){
                    onComplete.onError();
                } else onComplete.onTaskCompleted(videoModels, videoModels.size() > 1);
            }else onComplete.onError();
        } catch (Exception E){
            onComplete.onError();
            E.printStackTrace();
        }
    }
}
