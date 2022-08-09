package com.xpero.ugetterexample;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.xpero.ugetter.LowCostVideo;
import com.xpero.ugetter.Model.XModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    LowCostVideo xGetter;
    StyledPlayerView playerView;
    ProgressDialog progressDialog;
    String org;
    EditText edit_query;
    String player_referer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        playerView = findViewById(R.id.player_view);
        xGetter = new LowCostVideo(this);
        doTrustToCertificates();

        xGetter.onFinish(new LowCostVideo.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) {
                Log.d("mainActivity", vidURL.get(0).getUrl());
                progressDialog.dismiss();
                if (multiple_quality){
                    if (vidURL!=null) {
                        multipleQualityDialog(vidURL);
                    }else done(null);
                }else {
                    done(vidURL.get(0));
                }
            }

            @Override
            public void onError() {
                Log.d("mainActivity", "Error");
                progressDialog.dismiss();
                done(null);
            }
        });

        edit_query = findViewById(R.id.edit_query);
    }

    public void setPlayer(XModel vidURL) {

        Map<String, String> map = new HashMap<>();
        if (vidURL.getCookie() != null) {
            map.put("cookie", vidURL.getCookie());

            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory()
                    .setUserAgent(Util.getUserAgent(this, getApplicationInfo().loadLabel(getPackageManager()).toString()))
                    .setAllowCrossProtocolRedirects(true)
                    .setDefaultRequestProperties(map);

             if (Util.inferContentType(Uri.parse(vidURL.getUrl())) == C.TYPE_HLS) {
                 Log.d("mainActivity","TYPE_HLS");

                 HlsMediaSource hlsMediaSource =
                         new HlsMediaSource.Factory(dataSourceFactory)
                                 .createMediaSource(MediaItem.fromUri(vidURL.getUrl()));

                 ExoPlayer player = new ExoPlayer.Builder(this).build();
                 playerView.setPlayer(player);
                 player.setMediaSource(hlsMediaSource);
                 player.play();
                 player.prepare();
             }else {
                 Log.d("mainActivity","ProgressiveMediaSource");
                 MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                         .createMediaSource(MediaItem.fromUri(vidURL.getUrl()));
                 ExoPlayer player = new ExoPlayer.Builder(this).build();
                 playerView.setPlayer(player);
                 player.setMediaSource(mediaSource);
                 player.play();
                 player.prepare();
             }
        } else {
            map.put("Referer", vidURL.getUrl());

            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory()
                    .setUserAgent(Util.getUserAgent(this, getApplicationInfo().loadLabel(getPackageManager()).toString()))
                    .setAllowCrossProtocolRedirects(true)
                    .setDefaultRequestProperties(map);
            if (Util.inferContentType(Uri.parse(vidURL.getUrl())) == C.TYPE_HLS) {
                Log.d("mainActivity","TYPE_HLS 2");

                HlsMediaSource hlsMediaSource =
                        new HlsMediaSource.Factory(dataSourceFactory)
                                .createMediaSource(MediaItem.fromUri(vidURL.getUrl()));

                ExoPlayer player = new ExoPlayer.Builder(this).build();
                playerView.setPlayer(player);
                player.setMediaSource(hlsMediaSource);
                player.play();
                player.prepare();
            }else {
                Log.d("mainActivity","ProgressiveMediaSource");
                MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(vidURL.getUrl()));
                ExoPlayer player = new ExoPlayer.Builder(this).build();
                playerView.setPlayer(player);
                player.setMediaSource(mediaSource);
                player.play();
                player.prepare();
            }
        }

    }

    public void doTrustToCertificates() {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        //No need to implement.
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void fetch(View view) {
        letGo(edit_query.getText().toString(), edit_query.getText().toString());
    }

    public void zippyShare(View view) {
        letGo("https://www13.zippyshare.com/v/N5WQ9Qum/file.html", "https://www13.zippyshare.com/v/N5WQ9Qum/file.html");
    }
    public void wu(View view) {
        letGo("https://workupload.com/file/AGq2kQ3Dz57", "https://workupload.com/file/AGq2kQ3Dz57");
    }
    public void mp4upload(View view) {
        letGo(getString(R.string.s_mp4upload), getString(R.string.s_mp4upload));
    }

    public void gphotos(View view) {
        letGo(getString(R.string.s_gphotos), getString(R.string.s_gphotos));
    }

    public void fb(View view) {
        letGo(getString(R.string.s_facebook), getString(R.string.s_facebook));
    }

    public void mediafire(View view) {
        letGo(getString(R.string.s_mediafire), getString(R.string.s_mediafire));
    }

    public void okru(View view) {
        letGo(getString(R.string.s_okru), getString(R.string.s_okru));
    }

    public void vk(View view) {
        letGo(getString(R.string.s_vkdotcom), getString(R.string.s_vkdotcom));
    }

    public void twitter(View view) {
        letGo(getString(R.string.s_twitter), getString(R.string.s_twitter));
    }

    public void youtube(View view) {
        letGo(getString(R.string.s_youtube), getString(R.string.s_youtube));
    }

    public void solidfiles(View view) {
        letGo(getString(R.string.s_solidfiles), getString(R.string.s_solidfiles));
    }

    public void vidozafiles(View view) {
        letGo(getString(R.string.s_vidoza), getString(R.string.s_vidoza));
    }

    public void sendvid(View view) {
        letGo(getString(R.string.s_sendvid), getString(R.string.s_sendvid));
    }

    public void fEmbed(View view) {
        letGo(getString(R.string.s_fembed), getString(R.string.s_fembed));
    }

    public void filerio(View view) {
        letGo(getString(R.string.s_filerio), getString(R.string.s_filerio));
    }

    public void dailymotion(View view) {
        letGo(getString(R.string.s_dailymotion), getString(R.string.s_dailymotion));
    }

    public void vidbm(View view) {
        letGo(getString(R.string.s_vidbam), getString(R.string.s_vidbam));
    }

    public void bittube(View view) {
        letGo(getString(R.string.s_bittube), getString(R.string.s_bittube));
    }

    public void videobin(View view) {
        letGo(getString(R.string.s_videobin), getString(R.string.s_videobin));
    }

    public void fourshared(View view) {
        letGo(getString(R.string.s_fourshared), getString(R.string.s_fourshared));
    }

    public void streamtape(View view) {
        letGo(getString(R.string.s_streamtape), getString(R.string.s_streamtape));
    }

    public void vudeo(View view) {
        letGo(getString(R.string.s_vudeo), getString(R.string.s_vudeo));
    }

    public void amazon(View view) {
        letGo(getString(R.string.amazon), getString(R.string.amazon));
    }

    public void doodstream(View view) {
        letGo(getString(R.string.dood), getString(R.string.dood));
    }

    public void streamsb(View view) {
        letGo(getString(R.string.streamsb), getString(R.string.streamsb));
    }

    public void mixdrop(View view) {
        letGo(getString(R.string.mdrop), getString(R.string.mdrop));
    }

    public void gounlimited(View view) {
        letGo(getString(R.string.gounlimiteduri), getString(R.string.gounlimiteduri));
    }

    public void voxzer(View view) {
        letGo(getString(R.string.voxzer_uri), getString(R.string.voxzer_uri));
    }

    public void anavidz(View view) {
        letGo(getString(R.string.anavidz_uri), getString(R.string.anavidz_uri));
    }

    public void aparat(View view) {
        letGo(getString(R.string.aparat_uri), getString(R.string.aparat_uri));
    }

    public void archive(View view) {
        letGo(getString(R.string.archive_uri), getString(R.string.archive_uri));
    }

    public void bitchute(View view) {
        letGo(getString(R.string.bitchute), getString(R.string.bitchute));
    }

    public void brighteon(View view) {
        letGo(getString(R.string.brighteon), getString(R.string.brighteon));
    }

    public void deadlyblogger(View view) {
        letGo(getString(R.string.deadlyblogger), getString(R.string.deadlyblogger));
    }

    public void fansubs(View view) {
        letGo(getString(R.string.fansubs), getString(R.string.fansubs));
    }

    public void diasfem(View view) {
        letGo(getString(R.string.diasfem), getString(R.string.diasfem));
    }

    public void gdstream(View view) {
        letGo(getString(R.string.gdstream), getString(R.string.gdstream));
    }

    public void googleusercontent(View view) {
        letGo(getString(R.string.gusercontent), getString(R.string.google));
    }

    public void hdvid(View view) {
        letGo(getString(R.string.hdvid), getString(R.string.hdvid));
    }

    public void mediashore(View view) {
        letGo(getString(R.string.mediashore), getString(R.string.mediashore));
    }

    public void voesx(View view) {
        letGo(getString(R.string.voesx), getString(R.string.voesx));
    }

    public void gomoplayer(View view) {
        letGo(getString(R.string.gomplayer), getString(R.string.gomplayer));
    }

    public void eplayvid(View view) {
        letGo(getString(R.string.eplayvid), getString(R.string.eplayvid));
    }

    public void vidmoly(View view) {
        letGo(getString(R.string.vidmoly), getString(R.string.vidmolyref));
    }

    public void midian(View view) {
        letGo(getString(R.string.midian), getString(R.string.google));
    }

    public void upstream(View view) {
        letGo(getString(R.string.upstream), getString(R.string.upstream));
    }

    public void yodbox(View view) {
        letGo(getString(R.string.yodbox), getString(R.string.yodbox));
    }

    private void letGo(String url, String referer) {
        org = url;
        progressDialog.show();
        player_referer = referer;
        xGetter.find(url,"null");
    }

    private void done(XModel xModel) {
        String url = null;
        if (xModel != null) {
            url = xModel.getUrl();
            //checkFileSize(xModel);
        }
        if (url != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Congratulations!")
                    .setMessage(R.string.can_stream)
                    .setPositiveButton(R.string.ok_z, null)
                    .setPositiveButton(R.string.stream, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setPlayer(xModel);
                        }
                    })
                    .setNegativeButton(R.string.copy, new DialogInterface.OnClickListener() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            clipboardManager.setText(xModel.getUrl());
                            if (clipboardManager.hasText()) {
                                Toast.makeText(MainActivity.this, getString(R.string.copied), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.sorry))
                    .setMessage(R.string.error)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            builder.show();
        }
    }

    private void multipleQualityDialog(ArrayList<XModel> model) {
        CharSequence[] name = new CharSequence[model.size()];

        for (int i = 0; i < model.size(); i++) {
            name[i] = model.get(i).getQuality();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.quality)
                .setItems(name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        done(model.get(which));
                    }
                })
                .setPositiveButton(R.string.ok_z, null);
        builder.show();
    }

}