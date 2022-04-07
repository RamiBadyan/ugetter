package com.xpero.ugetter.Sites;

import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import java.util.ArrayList;

public class DeadlyBlogger {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){

        if(url.contains(".mp4")){
            XModel xModel = new XModel();
            xModel.setUrl(url);
            xModel.setQuality("Normal");
            ArrayList<XModel> xModels = new ArrayList<>();
            xModels.add(xModel);
            onComplete.onTaskCompleted(xModels,false);
        } else if(url.contains(".html")){
            XModel xModel = new XModel();
            xModel.setUrl(url.replace(".html" , ".webm"));
            xModel.setQuality("Normal");
            ArrayList<XModel> xModels = new ArrayList<>();
            xModels.add(xModel);
            onComplete.onTaskCompleted(xModels,false);
        } else {
            onComplete.onError();
        }
    }
}
