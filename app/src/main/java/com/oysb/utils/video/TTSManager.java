package com.oysb.utils.video;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.oysb.utils.Loger;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TTSManager {
    private static long lastAddTextTime;
    public static TextToSpeech textToSpeech;
    private static Context ttsContext;

    /* loaded from: classes2.dex */
    public enum Speed {
        Normal,
        Faster,
        Slower
    }

    public static void setSpeekSpeed(Speed speed) {
        if (textToSpeech != null) {
            float f = 1.0f;
            if (speed != Speed.Normal) {
                if (speed == Speed.Faster) {
                    f = 1.2f;
                } else if (speed == Speed.Slower) {
                    f = 0.8f;
                }
            }
            textToSpeech.setSpeechRate(f);
        }
    }

    public static void addText(String str) {
        if (textToSpeech != null) {
            lastAddTextTime = System.currentTimeMillis();
            textToSpeech.speak(str, 1, null);
        }
    }

    /* renamed from: com.oysb.utils.video.TTSManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements TextToSpeech.OnInitListener {
        AnonymousClass1() {
        }

        @Override // android.speech.tts.TextToSpeech.OnInitListener
        public void onInit(int i) {
            TextToSpeech textToSpeech = TTSManager.textToSpeech;
            if (i == 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    TTSManager.textToSpeech.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(4).build());
                }
                TTSManager.textToSpeech.setLanguage(Locale.CHINA);
            }
        }
    }

    public static void init(Context context) {
        ttsContext = context;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textToSpeech.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build());
                    }
                    textToSpeech.setLanguage(Locale.CHINA);
                }
            }
        });
    }


    public static void clear() {
        TextToSpeech textToSpeech2 = textToSpeech;
        if (textToSpeech2 != null) {
            textToSpeech2.stop();
        }
    }

    public static long getLastAddTextTime() {
        return lastAddTextTime;
    }

    public static void setTTSVoice(int i) {
        Loger.writeLog("UI", "setTTSVoice voice=" + i);
        Loger.writeLog("UI", "setTTSVoice" + Log.getStackTraceString(new Throwable()));
        Context context = ttsContext;
        if (context != null) {
            try {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                double streamMaxVolume = audioManager.getStreamMaxVolume(4) * i;
                Double.isNaN(streamMaxVolume);
                int i2 = (int) (streamMaxVolume / 100.0d);
                if (audioManager.getStreamVolume(4) == i2) {
                    return;
                }
                Loger.writeLog("UI", "param voice:" + i + " max:" + audioManager.getStreamMaxVolume(4) + " current:" + audioManager.getStreamVolume(4) + " setTTSVoice:" + i2);
                audioManager.setStreamVolume(4, i2 == 0 ? 1 : i2, 0);
                if (i2 == 0) {
                    audioManager.adjustStreamVolume(4, -1, AudioManager.FLAG_SHOW_UI);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
