package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.shj.setting.R;
import com.xyshj.machine.monitor.CamaraDataUpload;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MicrophoneTestDialog extends Dialog {
    private AudioManager audioManager;
    private Button bt_test;
    private Button button_close;
    private Context context;
    private boolean isStart;
    private List<String> list;
    private MicropRunnable run_Metned;
    private List<Integer> sourceList;
    private Spinner spinner;

    public MicrophoneTestDialog(Context context) {
        super(context, R.style.loading_style);
        this.isStart = false;
        this.run_Metned = new MicropRunnable();
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_microphone_test);
        this.button_close = (Button) findViewById(R.id.button_close);
        this.bt_test = (Button) findViewById(R.id.bt_test);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        showSpinner();
        setCanceledOnTouchOutside(false);
        setListener();
    }

    private void showSpinner() {
        ArrayList arrayList = new ArrayList();
        this.list = arrayList;
        arrayList.add("MIC");
        this.list.add("CAMCORDER");
        this.list.add("DEFAULT");
        this.list.add("VOICE_CALL");
        this.list.add("VOICE_COMMUNICATION");
        this.list.add("VOICE_DOWNLINK");
        this.list.add("VOICE_RECOGNITION");
        this.list.add("VOICE_UPLINK");
        ArrayList arrayList2 = new ArrayList();
        this.sourceList = arrayList2;
        arrayList2.add(1);
        this.sourceList.add(5);
        this.sourceList.add(0);
        this.sourceList.add(4);
        this.sourceList.add(7);
        this.sourceList.add(3);
        this.sourceList.add(6);
        this.sourceList.add(2);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.context, R.layout.layout_spinner_item_simple, this.list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter((SpinnerAdapter) arrayAdapter);
    }

    /* renamed from: com.shj.setting.Dialog.MicrophoneTestDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            MicrophoneTestDialog.this.run_Metned.no();
            MicrophoneTestDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.MicrophoneTestDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MicrophoneTestDialog.this.run_Metned.no();
                MicrophoneTestDialog.this.dismiss();
            }
        });
        this.bt_test.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.MicrophoneTestDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MicrophoneTestDialog.this.isStart) {
                    MicrophoneTestDialog.this.isStart = false;
                    MicrophoneTestDialog.this.run_Metned.no();
                    MicrophoneTestDialog.this.bt_test.setText(MicrophoneTestDialog.this.context.getString(R.string.start));
                } else {
                    MicrophoneTestDialog.this.isStart = true;
                    MicrophoneTestDialog.this.start_test();
                    MicrophoneTestDialog.this.bt_test.setText(MicrophoneTestDialog.this.context.getString(R.string.stop));
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.MicrophoneTestDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (MicrophoneTestDialog.this.isStart) {
                MicrophoneTestDialog.this.isStart = false;
                MicrophoneTestDialog.this.run_Metned.no();
                MicrophoneTestDialog.this.bt_test.setText(MicrophoneTestDialog.this.context.getString(R.string.start));
            } else {
                MicrophoneTestDialog.this.isStart = true;
                MicrophoneTestDialog.this.start_test();
                MicrophoneTestDialog.this.bt_test.setText(MicrophoneTestDialog.this.context.getString(R.string.stop));
            }
        }
    }

    public void start_test() {
        openSpeaker();
        new Thread(this.run_Metned).start();
    }

    public void openSpeaker() {
        try {
            AudioManager audioManager = (AudioManager) this.context.getSystemService("audio");
            this.audioManager = audioManager;
            audioManager.setMode(2);
            this.audioManager.getStreamVolume(0);
            if (this.audioManager.isSpeakerphoneOn()) {
                return;
            }
            this.audioManager.setSpeakerphoneOn(true);
            AudioManager audioManager2 = this.audioManager;
            audioManager2.setStreamVolume(0, audioManager2.getStreamMaxVolume(0), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes2.dex */
    public class MicropRunnable implements Runnable {
        static final int audioEncoding = 2;
        static final int channelConfiguration = 2;
        static final int frequency = 44100;
        boolean isRecording = false;
        int recBufSize = AudioRecord.getMinBufferSize(frequency, 2, 2);
        int playBufSize = AudioTrack.getMinBufferSize(frequency, 2, 2);
        AudioRecord audioRecord = new AudioRecord(5, frequency, 16, 2, AudioRecord.getMinBufferSize(frequency, 16, 2));
        AudioTrack audioTrack = new AudioTrack(0, frequency, 2, 2, this.playBufSize, 1);

        public MicropRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                byte[] bArr = new byte[this.recBufSize];
                this.audioRecord.startRecording();
                this.audioTrack.play();
                this.isRecording = true;
                while (this.isRecording) {
                    int read = this.audioRecord.read(bArr, 0, this.recBufSize);
                    byte[] bArr2 = new byte[read];
                    System.arraycopy(bArr, 0, bArr2, 0, read);
                    CamaraDataUpload.uploadAudio("192.168.2.121", 7000, bArr2);
                    this.audioTrack.write(bArr2, 0, read);
                }
                this.audioTrack.stop();
                this.audioRecord.stop();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        public void no() {
            this.isRecording = false;
        }
    }
}
