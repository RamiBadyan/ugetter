package com.xpero.ugetter.Sites;

import static com.xpero.ugetter.LowCostVideo.agent;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Amazon {

    private static String ShareID;

    public static void fetch(String url, final LowCostVideo.OnTaskCompleted onComplete){
        ShareID = get_ShareID(url);
        String baseurl = "https://www.amazon.com/drive/v1/shares/"+ShareID+"?shareId="+ShareID+"&resourceVersion=V2&ContentType=JSON&_="+System.currentTimeMillis();
        AndroidNetworking.get(baseurl)
                .setUserAgent(agent)
                .addHeaders("Referer", "https://www.amazon.com/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        final String regex = "id\":\"(.*?)\"";
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                            String nodeInfo = matcher.group(1);
                            String data_url = "https://www.amazon.com/drive/v1/nodes/"+nodeInfo+"/children?asset=ALL&limit=1&searchOnFamily=false&tempLink=true&shareId="+ShareID+"&offset=0&resourceVersion=V2&ContentType=JSON&_="+System.currentTimeMillis();
                            AndroidNetworking.get(data_url)
                                    .setUserAgent(agent)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            ArrayList<XModel> xModel = parse(response);
                                            if (xModel!=null){
                                                onComplete.onTaskCompleted(xModel,false);
                                            }else onComplete.onError();
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            onComplete.onError();
                                        }
                                    });

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        onComplete.onError();
                    }
                });
    }

    private static ArrayList<XModel> parse(JSONObject response){
        try {
            JSONArray array = response.getJSONArray("data");
            JSONObject A = array.getJSONObject(0);
            String source = A.getString("tempLink");
            if (source!=null && source.length()>0) {
                XModel jModel = new XModel();
                jModel.setUrl(source);
                jModel.setQuality("Normal");

                ArrayList<XModel> xModel = new ArrayList<>();
                xModel.add(jModel);
                return xModel;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String get_ShareID(String link){
        String[] seperate = link.split("/");
        return seperate[seperate.length-1];
    }
}
