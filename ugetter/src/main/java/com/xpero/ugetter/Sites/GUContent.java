package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.Utils.Utils.putModel;

import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.util.ArrayList;

public class GUContent {

    private static ArrayList<XModel> videoModels;

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){

        videoModels = new ArrayList<>();
        putModel(url,"Normal", videoModels);
        if (videoModels !=null) {
            if(videoModels.size() == 0){
                onComplete.onError();
            } else {
                onComplete.onTaskCompleted(videoModels,false);
            }
        }else onComplete.onError();
    }
}
