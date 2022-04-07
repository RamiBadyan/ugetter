package com.xpero.ugetter.Sites;

import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import java.util.ArrayList;

public class Midian {

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){

        if(url.endsWith("avi")){
            onComplete.onError();
        } else {
            ArrayList<XModel> xModels = new ArrayList<>();

            XModel jModel = new XModel();
            jModel.setUrl(url);
            jModel.setQuality("Normal");
            xModels.add(jModel);

            onComplete.onTaskCompleted(xModels,false);
        }


    }
}
