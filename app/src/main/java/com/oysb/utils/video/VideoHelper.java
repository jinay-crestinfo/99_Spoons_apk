package com.oysb.utils.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.mjdev.libaums.fs.UsbFile;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.iflytek.speech.VoiceWakeuperAidl;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import com.oysb.utils.cache.CacheHelper;
import com.oysb.utils.date.DateUtil;
import com.oysb.utils.io.file.SDFileUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.tencent.wxpayface.WxfacePayCommonCode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public class VideoHelper {
    public static final int IMG_TIME = 10000;
    public static final String MODE_IJK = "ijk";
    public static final String MODE_SYS = "sys";
    public static final int MSG_PLAYNEXT = 200;
    public static final int NOFILE_DELAY_TIME = 30000;
    public static final int PLAYNEXT_SPA = 200;
    static MediaMetadataRetriever mediaRetriever = null;
    public static final String supportFiles = "mp4;rmvb;avi;mov;png;jpg;bmp;";
    String folder;
    EmptyControlVideo video;
    int videoLayout;
    private VideoListener videoListener;
    WeakReference<FrameLayout> wkFrameContainer;
    WeakReference<ImageView> wkImageView;
    static HashMap<String, MedialInfo> medialInfos = new HashMap<>();
    static ExecutorService threadPool = null;
    Date date = new Date();
    String fileFormat = "mp4;3gp;rmvb;avi;mov;png;jpg;bmp;";
    String lastFile = "";
    boolean isPlayingPlanFile = false;
    boolean isFirstPlay = true;
    List<String> files = null;
    List<String> tempfiles = null;
    JSONObject playSetting = null;
    int audioType = 3;
    Bitmap currentBmp = null;
    int bgImageResource = -1;
    int idx = 0;
    int lastImageIdx = -1;
    long lastVideoPlayTime = Long.MAX_VALUE;
    long vidoeItemTimeLen = 10000;
    int videoPosition = 0;
    boolean started = false;
    private boolean isFullScreen = false;
    Timer checkTimer = null;
    String currentVideoFile = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                if (message.what == 200) {
                    VideoHelper.this.playNext();
                }
            } catch (Exception ignored) {
            }
        }
    };

    long lastUpdateTime = -1;

    /* loaded from: classes2.dex */
    public interface VideoListener {
        void onPlayItemChanged(String str, int i, int i2);

        boolean shuldPlayNextLoop();
    }

    public VideoHelper(Context context) {
        init("sys");
    }

    public static void setPlayerMode(String str) {
        try {
            CacheHelper.getFileCache().put("VIDEO_PLAY_MODE", str);
        } catch (Exception unused) {
        }
    }

    public static String getPlayerMode() {
        try {
            String asString = CacheHelper.getFileCache().getAsString("VIDEO_PLAY_MODE");
            if (asString != null) {
                return asString;
            }
            CacheHelper.getFileCache().put("VIDEO_PLAY_MODE", "sys");
            return "sys";
        } catch (Exception unused) {
            return "sys";
        }
    }

    public void init(String str) {
        if (MODE_IJK.equals(str)) {
            PlayerFactory.setPlayManager(IjkPlayerManager.class);
            IjkPlayerManager.setLogLevel(8);
        } else {
            PlayerFactory.setPlayManager(SystemPlayerManager.class);
        }
        CacheFactory.setCacheManager(ProxyCacheManager.class);
        GSYVideoType.setShowType(this.isFullScreen ? 4 : 0);
        if (Build.VERSION.SDK_INT >= 23) {
            GSYVideoType.setRenderType(0);
        } else {
            GSYVideoType.setRenderType(1);
        }
    }

    public void setFullScreen(boolean z) {
        this.isFullScreen = z;
        List<String> list = this.files;
        if (list != null) {
            list.clear();
        }
        List<String> list2 = this.tempfiles;
        if (list2 != null) {
            list2.clear();
            this.tempfiles = null;
        }
    }

    /* loaded from: classes2.dex */
    public class EmptyControlVideo extends StandardGSYVideoPlayer {
        public EmptyControlVideo(Context context) {
            super();
            this.onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int i) {
                    // Empty implementation
                }
            };
        }

        @Override
        public int getLayoutId() {
            return VideoHelper.this.videoLayout;
        }

        @Override
        protected void touchSurfaceMoveFullLogic(float f, float f2) {
            super.touchSurfaceMoveFullLogic(f, f2);
            this.mChangePosition = false;
            this.mChangeVolume = false;
            this.mBrightness = false;
        }
    }


    void cancelCheckTimer() {
        Timer timer = this.checkTimer;
        if (timer != null) {
            timer.cancel();
            this.checkTimer = null;
        }
    }

    void resetCheckTimer() {
        cancelCheckTimer();
        Timer timer = new Timer();
        this.checkTimer = timer;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long currentTimeMillis = System.currentTimeMillis() - VideoHelper.this.lastVideoPlayTime - VideoHelper.this.vidoeItemTimeLen;
                if (VideoHelper.this.started && VideoHelper.this.files != null && VideoHelper.this.files.size() > 0 && currentTimeMillis > HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS) {
                    VideoHelper.this.handler.sendEmptyMessage(200);
                } else if (VideoHelper.this.started && (VideoHelper.this.files == null || VideoHelper.this.files.size() == 0) && currentTimeMillis > 30000) {
                    VideoHelper.this.handler.sendEmptyMessage(200);
                }
            }
        }, 10000L);
    }


    /* renamed from: com.oysb.utils.video.VideoHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            long currentTimeMillis = (System.currentTimeMillis() - VideoHelper.this.lastVideoPlayTime) - VideoHelper.this.vidoeItemTimeLen;
            if (VideoHelper.this.started && VideoHelper.this.files != null && VideoHelper.this.files.size() > 0 && currentTimeMillis > HlsChunkSource.DEFAULT_MIN_BUFFER_TO_SWITCH_UP_MS) {
                VideoHelper.this.handler.sendEmptyMessage(200);
                return;
            }
            if (VideoHelper.this.started) {
                if ((VideoHelper.this.files == null || VideoHelper.this.files.size() == 0) && currentTimeMillis > 30000) {
                    VideoHelper.this.handler.sendEmptyMessage(200);
                }
            }
        }
    }

    public void setLoadingImage(int i) {
        this.bgImageResource = i;
    }

    public static boolean hasSupportFiles(String str, String str2, boolean z) {
        if (str2 == null || str2.length() == 0) {
            str2 = supportFiles;
        }
        List<File> files = SDFileUtils.getFiles(str, "");
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                String absolutePath = file.getAbsolutePath();
                if (str2.contains(name.substring(name.length() - 3).toLowerCase() + VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                    if (absolutePath.endsWith(".png") || absolutePath.endsWith(".jpg") || absolutePath.endsWith(".bmp") || !z || getMedialInfos(file.getAbsolutePath(), false) != null) {
                        return true;
                    } else {
                        if (threadPool == null) {
                            threadPool = Executors.newSingleThreadExecutor();
                        }
                        threadPool.submit(() -> {
                            try {
                                VideoHelper.getMedialInfos(absolutePath, true);
                            } catch (Exception ignored) {
                            }
                        });
                        return false;
                    }
                }
            }
        }
        return false;
    }


    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.video.VideoHelper$2 */
    /* loaded from: classes2.dex */
    public class MediaInfoRunnable implements Runnable {
        private final String filePath;

        public MediaInfoRunnable(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            try {
                VideoHelper.getMedialInfos(filePath, true);
            } catch (Exception ignored) {
            }
        }
    }


    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.video.VideoHelper$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends Handler {


        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                if (message.what != 200) {
                    return;
                }
                VideoHelper.this.playNext();
            } catch (Exception unused) {
            }
        }
    }

    public void setVideoVoice(Context context, int i) {
        Loger.writeLog("UI", "setVideoVoice voice=" + i);
        Loger.writeLog("UI", "setVideoVoice" + Log.getStackTraceString(new Throwable()));
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            double streamMaxVolume = audioManager.getStreamMaxVolume(this.audioType) * i;
            Double.isNaN(streamMaxVolume);
            int i2 = (int) (streamMaxVolume / 100.0d);
            if (audioManager.getStreamVolume(this.audioType) == i2) {
                return;
            }
            Loger.writeLog("UI", "param voice:" + i + " max:" + audioManager.getStreamMaxVolume(this.audioType) + " current:" + audioManager.getStreamVolume(this.audioType) + " setVideoVoice:" + i2);
            audioManager.setStreamVolume(this.audioType, i2 == 0 ? 1 : i2, 0);
            if (i2 == 0) {
                audioManager.adjustStreamVolume(this.audioType, -1, AudioManager.FLAG_SHOW_UI);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPlay(int playDelay, int currentIndex, FrameLayout frameLayout, int videoLayout, ImageView imageView) {
        if (frameLayout == null || imageView == null) {
            return;
        }
        handler.removeMessages(200);
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("startPlay:");
        logMessage.append(folder);
        logMessage.append(" filesize:");
        logMessage.append(files == null ? 0 : files.size());
        Loger.writeLog("VIDEO", logMessage.toString());

        // Recycle the current bitmap if not already recycled
        Bitmap currentBitmap = currentBmp;
        if (currentBitmap != null && !currentBitmap.isRecycled()) {
            Loger.writeLog("BITMAP", "recycle:" + currentBmp.toString());
            imageView.setWillNotDraw(true);
            currentBitmap.recycle();
            currentBmp = null;
        }

        // If files list is null, return
        if (files == null) {
            return;
        }

        // Set the video layout
        this.videoLayout = videoLayout;

        try {
            wkFrameContainer = new WeakReference<>(frameLayout);

            // Remove the existing video player from the frame layout if exists
            EmptyControlVideo existingVideoPlayer = video;
            if (existingVideoPlayer != null && existingVideoPlayer.getParent() == frameLayout) {
                frameLayout.removeView(video);
            }
        } catch (Exception ignored) {
        }

        // Create a new video player and add it to the frame layout
        EmptyControlVideo newVideoPlayer = new EmptyControlVideo(frameLayout.getContext());
        video = newVideoPlayer;
        frameLayout.addView(newVideoPlayer, 0, new ViewGroup.LayoutParams(-1, -1));

        // Set video callbacks
        video.setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                changeImageView(8);
                vidoeItemTimeLen = video.getDuration() + 100000;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                handler.removeMessages(200);
                handler.sendEmptyMessageDelayed(200, (files == null || files.size() == 0) ? 30000L : 200L);
            }

            @Override
            public void onStartPrepared(String url, Object... objects) {
                try {
                    GSYVideoManager.instance().getPlayer().getMediaPlayer().setAudioStreamType(audioType);
                } catch (Exception e) {
                    Loger.writeException("VIDEO", e);
                }
            }
            // Other callback methods...
        });

        wkImageView = new WeakReference<>(imageView);

        // If files list is empty, schedule a message to play the next video after a delay
        if (files.size() == 0) {
            handler.removeMessages(200);
            handler.sendEmptyMessageDelayed(200, files.isEmpty() ? 30000L : 200L);
            started = true;
            return;
        }

        lastVideoPlayTime = Long.MAX_VALUE;

        // If already started, return
        if (started) {
            return;
        }

        started = true;
        idx = currentIndex - 1;
        handler.removeMessages(200);
        handler.sendEmptyMessageDelayed(200, playDelay < 1000 ? 1000L : playDelay);
        Loger.writeLog("VIDEO", "toPlayerNext");
    }


    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.oysb.utils.video.VideoHelper$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements VideoAllCallBack {
        @Override
        public void onClickBlank(String str, Object... objArr) {
        }

        @Override
        public void onClickBlankFullscreen(String str, Object... objArr) {
        }

        @Override
        public void onClickResume(String str, Object... objArr) {
        }

        @Override
        public void onClickResumeFullscreen(String str, Object... objArr) {
        }

        @Override
        public void onClickSeekbar(String str, Object... objArr) {
        }

        @Override
        public void onClickSeekbarFullscreen(String str, Object... objArr) {
        }

        @Override
        public void onClickStartError(String str, Object... objArr) {
        }

        @Override
        public void onClickStartIcon(String str, Object... objArr) {
        }

        @Override
        public void onClickStartThumb(String str, Object... objArr) {
        }

        @Override
        public void onClickStop(String str, Object... objArr) {
        }

        @Override
        public void onClickStopFullscreen(String str, Object... objArr) {
        }

        @Override
        public void onEnterFullscreen(String str, Object... objArr) {
        }

        @Override
        public void onEnterSmallWidget(String str, Object... objArr) {
        }

        @Override
        public void onPlayError(String str, Object... objArr) {
        }

        @Override
        public void onQuitFullscreen(String str, Object... objArr) {
        }

        @Override
        public void onQuitSmallWidget(String str, Object... objArr) {
        }

        @Override
        public void onTouchScreenSeekLight(String str, Object... objArr) {
        }

        @Override
        public void onTouchScreenSeekPosition(String str, Object... objArr) {
        }

        @Override
        public void onTouchScreenSeekVolume(String str, Object... objArr) {
        }

        @Override
        public void onStartPrepared(String str, Object... objArr) {
            try {
                GSYVideoManager.instance().getPlayer().getMediaPlayer().setAudioStreamType(VideoHelper.this.audioType);
            } catch (Exception e) {
                Loger.writeException("VIDEO", e);
            }
        }

        @Override
        public void onPrepared(String str, Object... objArr) {
            VideoHelper.this.changeImageView(8);
            VideoHelper.this.vidoeItemTimeLen = r3.video.getDuration() + 100000;
        }

        @Override
        public void onAutoComplete(String str, Object... objArr) {
            VideoHelper.this.handler.removeMessages(200);
            VideoHelper.this.handler.sendEmptyMessageDelayed(200, (VideoHelper.this.files == null || VideoHelper.this.files.size() == 0) ? 30000L : 200L);
        }
    }


    public boolean isStarted() {
        return this.started;
    }

    public void stop() {
        EmptyControlVideo emptyControlVideo;
        try {
            Loger.writeLog("VIDEO", "stop:" + this.folder);
            this.started = false;
            cancelCheckTimer();
            this.handler.removeMessages(200);
            this.fileFormat = "mp4;3gp;rmvb;avi;mov;flv;png;jpg;bmp;";
            this.lastImageIdx = -1;
            this.videoListener = null;
            this.handler.removeCallbacksAndMessages(null);
            WeakReference<FrameLayout> weakReference = this.wkFrameContainer;
            FrameLayout frameLayout = weakReference == null ? null : weakReference.get();
            if (frameLayout != null && (emptyControlVideo = this.video) != null && emptyControlVideo.getParent() == frameLayout) {
                frameLayout.removeView(this.video);
            }
            EmptyControlVideo emptyControlVideo2 = this.video;
            if (emptyControlVideo2 != null) {
                emptyControlVideo2.release();
            }
            this.currentVideoFile = null;
            this.wkFrameContainer = null;
        } catch (Exception unused) {
        }
        try {
            GSYVideoManager.releaseAllVideos();
        } catch (Exception unused2) {
        }
    }

    public void setVideoFormat(String str) {
        this.fileFormat = str;
    }

    public void updatePlayFiles() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.lastUpdateTime > 120000) {
            this.lastUpdateTime = currentTimeMillis;
            Loger.writeLog("VIDEO", "updatePlayFiles:" + this.folder);
            setVideoFolder(this.folder);
        }
    }

    public static List<File> getVideoFiles(String str, String str2) {
        if (str2 == null || str2.length() == 0) {
            str2 = supportFiles;
        }
        List<File> files = SDFileUtils.getFiles(str, "");
        ArrayList arrayList = new ArrayList();
        if (files != null) {
            for (File file : files) {
                if (str2.contains(file.getName().substring(r2.length() - 3).toLowerCase() + VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }

    public void setVideoFolder(String str) {
        if (this.tempfiles != null) {
            return;
        }
        Loger.writeLog("VIDEO", "setVideoFolder:" + str);
        if (!new File(str).exists()) {
            SDFileUtils.creatDataDir(str);
        }
        try {
            File file = new File(str + "/playSetting.json");
            if (file.exists()) {
                this.playSetting = new JSONObject(SDFileUtils.readFile(file.getAbsolutePath()));
            }
        } catch (Exception unused) {
        }
        this.folder = str;
        List<File> files = SDFileUtils.getFiles(str, "");
        this.tempfiles = new ArrayList();
        if (files != null) {
            for (File file2 : files) {
                if (!file2.isDirectory()) {
                    try {
                        String lowerCase = file2.getName().substring(file2.getName().length() - 3).toLowerCase();
                        if (this.fileFormat.contains(lowerCase + VoiceWakeuperAidl.PARAMS_SEPARATE)) {
                            this.tempfiles.add(file2.getAbsolutePath());
                        }
                    } catch (Exception e) {
                        Loger.safe_inner_exception_catch(e);
                        Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e);
                    }
                }
            }
        }
        if (!this.isFullScreen) {
            this.files = new ArrayList(this.tempfiles);
            this.tempfiles = null;
        } else {
            if (threadPool == null) {
                threadPool = Executors.newSingleThreadExecutor();
            }
            threadPool.submit(new Runnable() { // from class: com.oysb.utils.video.VideoHelper.5
                @Override
                public void run() {
                    VideoHelper.this.files = new ArrayList(VideoHelper.this.tempfiles);
                    if (VideoHelper.this.tempfiles == null) {
                        return;
                    }
                    for (String str2 : VideoHelper.this.tempfiles) {
                        try {
                            if (!VideoHelper.this.files.contains(str2)) {
                                VideoHelper.getMedialInfos(str2, false);
                                VideoHelper.this.files.add(str2);
                            }
                        } catch (Exception unused2) {
                        }
                    }
                    VideoHelper.this.tempfiles = null;
                }
            });
        }
    }


    /* renamed from: com.oysb.utils.video.VideoHelper$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            VideoHelper.this.files = new ArrayList(VideoHelper.this.tempfiles);
            if (VideoHelper.this.tempfiles == null) {
                return;
            }
            for (String str2 : VideoHelper.this.tempfiles) {
                try {
                    if (!VideoHelper.this.files.contains(str2)) {
                        VideoHelper.getMedialInfos(str2, false);
                        VideoHelper.this.files.add(str2);
                    }
                } catch (Exception unused2) {
                }
            }
            VideoHelper.this.tempfiles = null;
        }
    }

    public int getFileCount() {
        return this.files.size();
    }

    public void playFile(String str) {
        this.currentVideoFile = null;
        playVideo(str, true);
    }

    private synchronized int _playPlanFile(String str) {
        Bitmap bitmap;
        Loger.writeLog("VIDEO", "_playFile2:" + str);
        if (str == null || str.length() == 0) {
            return 1;
        }
        try {
            if (!this.started) {
                return 0;
            }
            if (isImage(str)) {
                Bitmap bitmap2 = this.currentBmp;
                int i = 8;
                changeImageView((bitmap2 == null || !bitmap2.isRecycled()) ? 0 : 8);
                if (this.lastFile.equalsIgnoreCase(str) && (bitmap = this.currentBmp) != null && !bitmap.isRecycled()) {
                    return 1;
                }
                Bitmap bitmap3 = this.currentBmp;
                try {
                    this.currentBmp = BitmapFactory.decodeStream(new FileInputStream(str));
                    Loger.writeLog("BITMAP", "decodeBmp:" + this.currentBmp.toString());
                    this.wkImageView.get().setImageBitmap(this.currentBmp);
                    Bitmap bitmap4 = this.currentBmp;
                    if (bitmap4 != null && !bitmap4.isRecycled()) {
                        i = 0;
                    }
                    changeImageView(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bitmap3 != null) {
                    try {
                        if (!bitmap3.isRecycled()) {
                            Loger.writeLog("BITMAP", "recycle:" + bitmap3.toString());
                            bitmap3.recycle();
                        }
                    } catch (Exception unused) {
                    }
                }
                return 1;
            }
            playVideo(str, true);
            return 0;
        } catch (Exception unused2) {
            return 0;
        }
    }

    private synchronized int _playFile(String str) {
        Bitmap bitmap;
        Loger.writeLog("VIDEO", "_playFile:" + str);
        if (str == null || str.length() == 0) {
            return 1;
        }
        try {
            if (!this.started) {
                return 0;
            }
            VideoListener videoListener = this.videoListener;
            if (videoListener != null && !this.isFirstPlay) {
                videoListener.onPlayItemChanged(str, this.idx, this.files.size());
            }
            if (isImage(str)) {
                Bitmap bitmap2 = this.currentBmp;
                int i = 8;
                changeImageView((bitmap2 == null || !bitmap2.isRecycled()) ? 0 : 8);
                if (this.idx == this.lastImageIdx && (bitmap = this.currentBmp) != null && !bitmap.isRecycled()) {
                    this.isFirstPlay = false;
                    return 1;
                }
                this.lastImageIdx = this.idx;
                Loger.writeLog("VIDEO", "img: idx:" + this.idx + " file:" + str);
                Bitmap bitmap3 = this.currentBmp;
                try {
                    this.currentBmp = BitmapFactory.decodeStream(new FileInputStream(str));
                    Loger.writeLog("BITMAP", "decodeBmp:" + this.currentBmp.toString());
                    this.wkImageView.get().setImageBitmap(this.currentBmp);
                    Bitmap bitmap4 = this.currentBmp;
                    if (bitmap4 != null && !bitmap4.isRecycled()) {
                        i = 0;
                    }
                    changeImageView(i);
                    Loger.writeLog("VIDEO", "img loaded");
                } catch (Exception e) {
                    Loger.writeException("VIDEO", e);
                }
                if (bitmap3 != null) {
                    try {
                        if (!bitmap3.isRecycled()) {
                            Loger.writeLog("BITMAP", "recycle:" + bitmap3.toString());
                            bitmap3.recycle();
                        }
                    } catch (Exception e2) {
                        Loger.writeException("VIDEO", e2);
                    }
                }
                this.isFirstPlay = false;
                return 1;
            }
            Loger.writeLog("VIDEO", "idx:" + this.idx + " file:" + str);
            playVideo(str, true);
            this.isFirstPlay = false;
            return 0;
        } catch (Exception e3) {
            Loger.writeException("VIDEO", e3);
            this.isFirstPlay = false;
            return 1;
        }
    }

    /* loaded from: classes2.dex */
    public static class MedialInfo {
        int duration;
        int height;
        double rotation;
        int width;

        MedialInfo() {
        }
    }

    static MedialInfo getMedialInfos(String str, boolean z) {
        double d;
        try {
            if (medialInfos.containsKey(str)) {
                return medialInfos.get(str);
            }
            if (z) {
                return null;
            }
            if (mediaRetriever == null) {
                mediaRetriever = new MediaMetadataRetriever();
            }
            mediaRetriever.setDataSource(str);
            int parseInt = Integer.parseInt(mediaRetriever.extractMetadata(19));
            int parseInt2 = Integer.parseInt(mediaRetriever.extractMetadata(18));
            try {
                d = Double.parseDouble(mediaRetriever.extractMetadata(24));
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                d = 0.0d;
            }
            int parseInt3 = Integer.parseInt(mediaRetriever.extractMetadata(9));
            mediaRetriever.release();
            mediaRetriever = null;
            MedialInfo medialInfo = new MedialInfo();
            medialInfo.height = parseInt;
            medialInfo.width = parseInt2;
            medialInfo.rotation = d;
            medialInfo.duration = parseInt3;
            medialInfos.put(str, medialInfo);
            return medialInfo;
        } catch (Exception e2) {
            Loger.safe_inner_exception_catch(e2);
            Loger.writeException(WxfacePayCommonCode.VAL_RSP_PARAMS_ERROR, e2);
            return null;
        }
    }

    void playVideo(String filePath, boolean isTest) {
        this.currentVideoFile = filePath;
        this.vidoeItemTimeLen = 10000L;
        this.videoPosition = 0;
        this.video.setUp("file://" + filePath, false, "test");
        try {
            Context context = this.wkFrameContainer.get().getContext();
            int screenWidth = CommonTool.screenWidth(context);
            if (this.isFullScreen) {
                MedialInfo medialInfo = getMedialInfos(filePath, false);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.video.getLayoutParams();
                if (medialInfo.height < medialInfo.width) {
                    double aspectRatio = 9.0 / 16.0;
                    int videoHeight = (int) (screenWidth * aspectRatio);
                    layoutParams.height = videoHeight;
                    layoutParams.gravity = Gravity.CENTER;
                } else {
                    layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = Gravity.CENTER;
                }
                this.video.setLayoutParams(layoutParams);
            }
        } catch (Exception ignored) {
        }
        this.video.startPlayLogic();
    }


    protected void playNext() {
        long j;
        long j2;
        this.lastVideoPlayTime = System.currentTimeMillis();
        this.currentVideoFile = null;
        StringBuilder sb = new StringBuilder();
        sb.append("fileSize:");
        List<String> list = this.files;
        sb.append(list == null ? 0 : list.size());
        sb.append(" curIdx:");
        sb.append(this.idx);
        sb.append(" playNext:");
        sb.append(this.folder);
        Loger.writeLog("VIDEO", sb.toString());
        try {
            List<String> list2 = this.files;
            if (list2 == null || list2.size() == 0 || !this.started) {
                VideoListener videoListener = this.videoListener;
                if (videoListener == null || videoListener.shuldPlayNextLoop()) {
                    this.handler.removeMessages(200);
                    Handler handler = this.handler;
                    List<String> list3 = this.files;
                    handler.sendEmptyMessageDelayed(200, (list3 == null || list3.size() == 0) ? 30000L : 200L);
                    return;
                }
                return;
            }
            try {
                String nextPlanFile = getNextPlanFile();
                if (nextPlanFile != null) {
                    if (_playPlanFile(nextPlanFile) == 1) {
                        this.isPlayingPlanFile = true;
                        this.lastFile = nextPlanFile;
                        this.handler.removeMessages(200);
                        Handler handler2 = this.handler;
                        List<String> list4 = this.files;
                        if (list4 != null && list4.size() != 0) {
                            j2 = 10000;
                            handler2.sendEmptyMessageDelayed(200, j2);
                            this.vidoeItemTimeLen = 10000L;
                            Loger.writeLog("VIDEO", "toPlayerNext");
                            return;
                        }
                        j2 = 30000;
                        handler2.sendEmptyMessageDelayed(200, j2);
                        this.vidoeItemTimeLen = 10000L;
                        Loger.writeLog("VIDEO", "toPlayerNext");
                        return;
                    }
                    this.lastFile = nextPlanFile;
                    return;
                }
                this.isPlayingPlanFile = false;
                this.lastFile = "";
                if (this.idx + 1 >= this.files.size()) {
                    this.idx = 0;
                    Loger.writeLog("VIDEO", "videoListener:" + this.videoListener);
                    VideoListener videoListener2 = this.videoListener;
                    if (videoListener2 != null && !videoListener2.shuldPlayNextLoop()) {
                        return;
                    }
                } else {
                    this.idx++;
                }
                String str = this.files.get(this.idx);
                Loger.writeLog("VIDEO", "next file:" + str);
                if (_playFile(str) == 1) {
                    this.handler.removeMessages(200);
                    Handler handler3 = this.handler;
                    List<String> list5 = this.files;
                    if (list5 != null && list5.size() != 0) {
                        j = 10000;
                        handler3.sendEmptyMessageDelayed(200, j);
                        this.vidoeItemTimeLen = 10000L;
                        Loger.writeLog("VIDEO", "toPlayerNext");
                    }
                    j = 30000;
                    handler3.sendEmptyMessageDelayed(200, j);
                    this.vidoeItemTimeLen = 10000L;
                    Loger.writeLog("VIDEO", "toPlayerNext");
                }
            } catch (Exception unused) {
                this.handler.removeMessages(200);
                Handler handler4 = this.handler;
                List<String> list6 = this.files;
                handler4.sendEmptyMessageDelayed(200, (list6 == null || list6.size() == 0) ? 30000L : 200L);
                Loger.writeLog("VIDEO", "toPlayerNext");
            }
        } catch (Exception unused2) {
        }
    }

    protected String getNextPlanFile() {
        Loger.writeLog("VIDEO", "getNextPlanFile:" + this.folder);
        this.date.setTime(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(DateUtil.YMD_TIME);
        String format = simpleDateFormat.format(this.date);
        String format2 = simpleDateFormat2.format(this.date);
        JSONObject jSONObject = this.playSetting;
        if (jSONObject == null || jSONObject.isNull("files")) {
            return null;
        }
        try {
            JSONArray jSONArray = this.playSetting.getJSONArray("files");
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    String string = !jSONObject2.isNull("startDate") ? jSONObject2.getString("startDate") : null;
                    String string2 = !jSONObject2.isNull("endDate") ? jSONObject2.getString("endDate") : null;
                    String string3 = !jSONObject2.isNull("startTime") ? jSONObject2.getString("startTime") : null;
                    String string4 = !jSONObject2.isNull("endTime") ? jSONObject2.getString("endTime") : null;
                    boolean z = string == null || (format.compareTo(string) >= 0 && format.compareTo(string2) <= 0);
                    boolean z2 = string3 == null || (format2.compareTo(string3) >= 0 && format2.compareTo(string4) <= 0);
                    if (z && z2) {
                        arrayList.add(jSONObject2);
                    }
                } catch (Exception unused) {
                }
            }
            if (arrayList.size() == 0) {
                return null;
            }
            int i2 = -1;
            int i3 = 0;
            while (true) {
                if (i3 >= arrayList.size()) {
                    break;
                }
                String str = this.folder + UsbFile.separator + ((JSONObject) arrayList.get(i3)).getString("file");
                if (new File(str).exists() && this.lastFile.equalsIgnoreCase(str)) {
                    i2 = i3;
                    break;
                }
                i3++;
            }
            int i4 = i2 + 1;
            File file = new File(this.folder + UsbFile.separator + ((JSONObject) arrayList.get(i4 > arrayList.size() - 1 ? 0 : i4)).getString("file"));
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            return null;
        } catch (Exception unused2) {
            return null;
        }
    }

    protected boolean isImage(String str) {
        try {
            String lowerCase = str.toLowerCase();
            if (!lowerCase.endsWith(".jpg") && !lowerCase.endsWith(".png")) {
                if (!lowerCase.endsWith(".bmp")) {
                    return false;
                }
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public VideoListener getVideoListener() {
        return this.videoListener;
    }

    public void setVideoListener(VideoListener videoListener) {
        this.videoListener = videoListener;
    }

    public void changeImageView(int i) {
        AlphaAnimation alphaAnimation;
        try {
            WeakReference<ImageView> weakReference = this.wkImageView;
            ImageView imageView = weakReference == null ? null : weakReference.get();
            imageView.clearAnimation();
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (imageView.getVisibility() == i) {
                return;
            }
            imageView.refreshDrawableState();
            imageView.setAnimation(null);
            if (i != 4 && i != 8) {
                alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(500L);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.oysb.utils.video.VideoHelper.6
                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationStart(Animation animation) {
                    }


                    @Override // android.view.animation.Animation.AnimationListener
                    public void onAnimationEnd(Animation animation) {
                        try {
                            (VideoHelper.this.wkImageView == null ? null : VideoHelper.this.wkImageView.get()).clearAnimation();
                        } catch (Exception unused) {
                        }
                    }
                });
                imageView.setAnimation(alphaAnimation);
                imageView.setVisibility(i);
            }
            alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(500L);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.oysb.utils.video.VideoHelper.6
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }


                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    try {
                        (VideoHelper.this.wkImageView == null ? null : VideoHelper.this.wkImageView.get()).clearAnimation();
                    } catch (Exception unused) {
                    }
                }
            });
            imageView.setAnimation(alphaAnimation);
            imageView.setVisibility(i);
        } catch (Exception unused) {
        }
    }

    /* renamed from: com.oysb.utils.video.VideoHelper$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements Animation.AnimationListener {
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        AnonymousClass6() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            try {
                (VideoHelper.this.wkImageView == null ? null : VideoHelper.this.wkImageView.get()).clearAnimation();
            } catch (Exception unused) {
            }
        }
    }
}
