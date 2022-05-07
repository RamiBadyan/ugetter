package com.xpero.ugetter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.xpero.ugetter.Sites.Amazon;
import com.xpero.ugetter.Sites.Anavidz;
import com.xpero.ugetter.Sites.Aparat;
import com.xpero.ugetter.Sites.Archive;
import com.xpero.ugetter.Sites.BitChute;
import com.xpero.ugetter.Sites.BitTube;
import com.xpero.ugetter.Sites.Brighteon;
import com.xpero.ugetter.Sites.DeadlyBlogger;
import com.xpero.ugetter.Sites.Diasfem;
import com.xpero.ugetter.Sites.EplayVid;
import com.xpero.ugetter.Sites.FShared;
import com.xpero.ugetter.Sites.GDStream;
import com.xpero.ugetter.Sites.GUContent;
import com.xpero.ugetter.Sites.GoMo;
import com.xpero.ugetter.Sites.HDVid;
import com.xpero.ugetter.Sites.MediaShore;
import com.xpero.ugetter.Sites.MegaUp;
import com.xpero.ugetter.Sites.Midian;
import com.xpero.ugetter.Sites.StreamTape;
import com.xpero.ugetter.Sites.Upstream;
import com.xpero.ugetter.Sites.VidMoly;
import com.xpero.ugetter.Sites.VideoBIN;
import com.xpero.ugetter.Sites.VivoSX;
import com.xpero.ugetter.Sites.Vlare;
import com.xpero.ugetter.Sites.Pstream;
import com.xpero.ugetter.Sites.CoCoScope;
import com.xpero.ugetter.Sites.DailyMotion;
import com.xpero.ugetter.Sites.GoUnlimited;
import com.xpero.ugetter.Sites.Muvix;
import com.xpero.ugetter.Sites.VideoBM;
import com.xpero.ugetter.Sites.Voxzer;
import com.xpero.ugetter.Sites.Vudeo;
import com.xpero.ugetter.Sites.YodBox;
import com.xpero.ugetter.Sites.Zippy;
import com.xpero.ugetter.Utils.DailyMotionUtils;
import com.xpero.ugetter.Core.GDrive2020;
import com.xpero.ugetter.Model.XModel;
import com.xpero.ugetter.Sites.FB;
import com.xpero.ugetter.Sites.FEmbed;
import com.xpero.ugetter.Sites.FanSubs;
import com.xpero.ugetter.Sites.FileRIO;
import com.xpero.ugetter.Sites.GPhotos;
import com.xpero.ugetter.Sites.MFire;
import com.xpero.ugetter.Sites.MP4Upload;
import com.xpero.ugetter.Sites.OKRU;
import com.xpero.ugetter.Sites.SendVid;
import com.xpero.ugetter.Sites.SolidFiles;
import com.xpero.ugetter.Sites.TW;
import com.xpero.ugetter.Sites.UpToStream;
import com.xpero.ugetter.Sites.VK;
import com.xpero.ugetter.Sites.Vidoza;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xpero.ugetter.Utils.FacebookUtils.check_fb_video;
import static com.xpero.ugetter.Utils.GDriveUtils.get_drive_id;

import okhttp3.OkHttpClient;

public class LowCostVideo {
    private Context context;
    private OnTaskCompleted onComplete;
    public static final String agent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.99 Safari/537.36";
    private final String mp4upload = "https?:\\/\\/(www\\.)?(mp4upload)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String filerio = "https?:\\/\\/(www\\.)?(filerio)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String sendvid = "https?:\\/\\/(www\\.)?(sendvid)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String gphoto = "https?:\\/\\/(photos.google.com)\\/(u)?\\/?(\\d)?\\/?(share)\\/.+(key=).+";
    private final String mediafire = "https?:\\/\\/(www\\.)?(mediafire)\\.[^\\/,^\\.]{2,}\\/(file)\\/.+";
    private final String okru = "https?:\\/\\/(www.|m.)?(ok)\\.[^\\/,^\\.]{2,}\\/(video|videoembed)\\/.+";
    private final String vk = "https?:\\/\\/(www\\.)?vk\\.[^\\/,^\\.]{2,}\\/video\\-.+";
    private final String twitter = "http(?:s)?:\\/\\/(?:www\\.)?twitter\\.com\\/([a-zA-Z0-9_]+)";
    private final String youtube = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
    private final String solidfiles = "https?:\\/\\/(www\\.)?(solidfiles)\\.[^\\/,^\\.]{2,}\\/(v)\\/.+";
    private final String vidoza = "https?:\\/\\/(www\\.)?(vidoza)\\.[^\\/,^\\.]{2,}.+";
    private final String uptostream = "https?:\\/\\/(www\\.)?(uptostream|uptobox)\\.[^\\/,^\\.]{2,}.+";
    private final String fansubs = "https?:\\/\\/(www\\.)?(fansubs\\.tv)\\/(v|watch)\\/.+";
    private final String fembed = "https?:\\/\\/(www\\.)?(fembed|vcdn)\\.[^\\/,^\\.]{2,}\\/(v|f)\\/.+";
    private final String megaup = "https?:\\/\\/(www\\.)?(megaup)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String gounlimited = "https?:\\/\\/(www\\.)?(gounlimited)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String cocoscope = "https?:\\/\\/(www\\.)?(cocoscope)\\.[^\\/,^\\.]{2,}\\/(watch\\?v).+";
    //private final String vidbm = "https?:\\/\\/(www\\.)?(vidbm)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String muvix = "https?:\\/\\/(www\\.)?(muvix)\\.[^\\/,^\\.]{2,}\\/(video|download).+";
    private final String pstream = "https?:\\/\\/(www\\.)?(pstream)\\.[^\\/,^\\.]{2,}\\/(.*)\\/.+";
    private final String vlareTV = "https?:\\/\\/(www\\.)?(vlare\\.tv)\\/(.*)\\/.+";
    private final String vivoSX = "https?:\\/\\/(www\\.)?(vivo\\.sx)\\/.+";
    private final String streamKiwi = "https?:\\/\\/(www\\.)?(stream\\.kiwi)\\/(.*)\\/.+";
    private final String bitTube = "https?:\\/\\/(www\\.)?(bittube\\.video\\/videos)\\/(watch|embed)\\/.+";
    //private final String videoBIN = "https?:\\/\\/(www\\.)?(videobin\\.co)\\/.+";
    private final String fourShared = "https?:\\/\\/(www\\.)?(4shared\\.com)\\/(video|web\\/embed)\\/.+";
    private final String streamtape = "https?:\\/\\/(www\\.)?(streamtape\\.com)\\/(v|e)\\/.+";
    private final String vudeo = "https?:\\/\\/(www\\.)?(vudeo\\.net)\\/.+";
    private final String zippy = "https?:\\/\\/(www.*\\.)(zippyshare\\.com)\\/.+";
    private final String vidbm = "https?:\\/\\/(vidbam)\\.[^\\/,^\\.]{2,}\\/.+";
    private final String videoBIN = "https?:\\/\\/(videobin\\.co)\\/.+";
    private final String amazon = "https?:\\/\\/(www\\.)?(amazon\\.com)\\/?(clouddrive)\\/+";
    private final String doodstream = "(?://|\\.)(dood(?:stream)?\\.(?:com|watch|to|so|la|ws|sh))/(?:d|e)/([0-9a-zA-Z]+)";
    private final String streamsb = ".+(streamsb|sbplay|sbplay2|sbembed|sbembed1|sbvideo|cloudemb|playersb|tubesb|sbplay1|embedsb|watchsb)\\.(com|net|one|org)/.+";
    private final String mixdrop = ".+(mixdrop)\\.(co|to|sx|bz)\\/.+";
    private final String voxzer = "https?:\\/\\/(player\\.)?(voxzer\\.)(?:org).+";
    private final String anavidz = ".+(anavidz\\.com).+";
    private final String aparat = ".+(aparat\\.com/v/).+";
    private final String archive = ".+(archive)\\.(org)\\/.+";
    private final String bitchute = ".+(bitchute\\.com)/(?:video|embed).+";
    private final String brighteon = ".+(brighteon\\.com).+";
    private final String deadlyblogger = ".+(deadlyblogger\\.com).+";
    private final String diasfem = ".+(diasfem\\.com|suzihaza)/v|f/.+";
    private final String gdstream = ".+(gdstream\\.net)/v|f/.+";
    private final String googleusercontent = ".+(googleusercontent\\.com).+";
    private final String hdvid = ".+(hdvid|vidhdthe)\\.(tv|fun|online)/.+";
    private final String mediashore = ".+(mediashore\\.org)/v|f/.+";
    private final String voesx = ".+(voe\\.sx).+";
    private final String gomoplayer = ".+(gomoplayer\\.com).+";
    private final String eplayvid = ".+(eplayvid)\\.(com|net)/.+";
    private final String vidmoly = ".+(vidmoly)\\.(me)/.+";
    private final String midian =  ".+(midian\\.appboxes)\\.(co)/.+";
    private final String upstream = ".+(upstream)\\.(to)/.+";
    private final String yodbox = ".+(yodbox)\\.(com)/.+";

    public LowCostVideo(@NonNull Context context){
        this.context=context;
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(20000, TimeUnit.MILLISECONDS)
                .readTimeout(20000, TimeUnit.MILLISECONDS)
                .writeTimeout(20000, TimeUnit.MILLISECONDS)
                .build();

        AndroidNetworking.initialize(context,okHttpClient);
    }

    public void find(String url){
        if (check(mp4upload, url)) {
            //https://www.mp4upload.com/
            MP4Upload.fetch(url,onComplete);
        } else if (check(sendvid, url)) {
            //http://sendvid.com/
            SendVid.fetch(url,onComplete);
        } else if (check(gphoto, url)) {
            //https://photos.google.com/share/AF1QipMkSCF43RzZEXWyGNMYWHCegzCgdW5ao_qJEBVZ8SPkS2IQmHZFz4a13PfAZGgvUQ/photo/AF1QipNnj95SaWHJca-Q8rUxzuRkYxX6UmnDSVykJhhw?key=dGhiZnl1SURYZmRhcFF0OVdueEk2TEtDWG9pb0J3
            GPhotos.fetch(url,onComplete);
        } else if (url.contains("drive.google.com") && get_drive_id(url) != null) {
            //https://drive.google.com/open?id=1IebKJvPykCjbWroUAhFtxLkjfbEh8nVU
            GDrive2020.get(context,url,onComplete);
        } else if (check_fb_video(url)) {
            FB.fetch(url,onComplete);
        } else if (check(mediafire, url)) {
            MFire.fetch(url,onComplete);
        } else if (check(okru,url)){
            OKRU.fetch(url,onComplete);
        } else if (check(vk,url)){
            VK.fetch(url,onComplete);
        } else if (check(twitter,url)){
            TW.fetch(url,onComplete);
        } else if (check(solidfiles,url)){
            SolidFiles.fetch(url,onComplete);
        } else if (check(vidoza,url)){
            Vidoza.fetch(url,onComplete);
        } else if (check(uptostream, url)) {
            UpToStream.fetch(context,url,onComplete);
        } else if (check(fansubs,url)){
            FanSubs.fetch(url,onComplete);
        } else if (url.contains("fembed")){
            FEmbed.fetch(url,onComplete);
        } else if (check(filerio,url)){
            FileRIO.fetch(url,onComplete);
        } else if (DailyMotionUtils.getDailyMotionID(url)!=null){
            DailyMotion.fetch(url,onComplete);
        } else if (check(megaup,url)){
            new MegaUp().get(context,url,onComplete);
        } else  if (check(gounlimited,url)){
            GoUnlimited.fetch(url,onComplete);
        } else if (check(cocoscope,url)){
            CoCoScope.fetch(url,onComplete);
        } else if (check(vidbm,url)){
            VideoBM.fetch(url,onComplete);
        } else if (check(muvix,url)){
            Muvix.fetch(url,onComplete);
        } else if (check(pstream,url)){
            Pstream.fetch(url,onComplete);
        } else if(check(vlareTV,url)){
            Vlare.fetch(url,onComplete);
        }else if (check(vivoSX,url)){
            VivoSX.fetch(url,onComplete);
//        }else if (check(streamKiwi,url)){
//            StreamKIWI.get(context,url,onComplete);
        }else if (check(bitTube,url)){
            BitTube.fetch(url,onComplete);
        }else if (check(videoBIN,url)){
            VideoBIN.fetch(url,onComplete);
        }else if (check(fourShared,url)){
            FShared.fetch(url,onComplete);
        }else if (check(streamtape,url)){
            StreamTape.fetch(url,onComplete);
        }else if (check(vudeo,url)) {
            Vudeo.fetch(url, onComplete);
        }else if (check(zippy,url)) {
            Zippy.fetch(context,url, onComplete);
        }else if (check(vidmoly,url)){
            VidMoly.fetch(url,onComplete);
        }else if (check(yodbox,url)){
            YodBox.fetch(url,onComplete);
        }else if (check(midian,url)){
            Midian.fetch(url,onComplete);
        } else if (check(eplayvid,url)){
            EplayVid.fetch(url,onComplete);
        }else if (check(gomoplayer, url)) {
            if(!url.contains("embed")) {
                String[] splits = url.split("/");
                String ID = splits[splits.length-1];
                String newurl = "https://gomoplayer.com/embed-"+ID+".html";
                GoMo.fetch(newurl, onComplete);
            } else {
                GoMo.fetch(url, onComplete);
            }
        } else if (check(mediashore,url)){
            MediaShore.fetch(url,onComplete);
        } else if (check(hdvid, url)) {
            if(url.contains("embed")){
                HDVid.fetch(url,onComplete);
            } else {
                String[] splits = url.split("/");
                String ID = splits[splits.length-1];
                String new_url = "https://hdvid.fun/embed-"+ID;
                HDVid.fetch(new_url,onComplete);
            }
        }else if (check(googleusercontent, url)) {
            GUContent.fetch(url,onComplete);
        }else if (check(sendvid, url)) {
            SendVid.fetch(url,onComplete);
        }else if (check(gdstream, url)) {
            GDStream.fetch(url,onComplete);
        }else if (check(diasfem, url)) {
            Diasfem.fetch(url,onComplete);
        }else if (check(fansubs, url)) {
            FanSubs.fetch(url,onComplete);
        }else if (check(deadlyblogger, url)) {
            DeadlyBlogger.fetch(url,onComplete);
        }else if (check(brighteon, url)) {
            Brighteon.fetch(url,onComplete);
        }else if (check(bitchute, url)) {
            BitChute.fetch(url,onComplete);
        }else if (check(archive, url)) {
            Archive.fetch(url,onComplete);
        }else if (check(aparat, url)) {
            Aparat.fetch(url,onComplete);
        }else if (check(anavidz, url)) {
            Anavidz.fetch(url,onComplete);
        }else if (check(voxzer, url)) {
            Voxzer.fetch(url,onComplete);
        }else if (check(amazon, url)) {
            Amazon.fetch(url,onComplete);
        } else if (check(upstream,url)){
            Upstream.fetch(url,onComplete);
        }else onComplete.onError();
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality);
        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    private boolean check(String regex, String string) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
}
