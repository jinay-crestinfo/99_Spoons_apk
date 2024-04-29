package com.shj.setting.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.exoplayer.util.MimeTypes;
import com.loopj.android.http.RequestParams;
import com.oysb.utils.Loger;
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.ScrollTipDialog;
import com.shj.setting.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class LogLookNoteView extends AbsItemView {
    private final String[][] MIME_MapTable;
    private String content;
    private List<File> fileList;
    private String fileName;
    private Handler handler;
    private LoadingDialog loadingDialog;
    private ListView lv_file;

    @Override // com.shj.setting.widget.AbsItemView
    public View getView() {
        return this;
    }

    public LogLookNoteView(Context context) {
        super(context);
        this.fileList = new ArrayList();
        this.handler = new Handler() { // from class: com.shj.setting.widget.LogLookNoteView.2
            AnonymousClass2() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                LogLookNoteView.this.loadingDialog.dismiss();
                ScrollTipDialog scrollTipDialog = new ScrollTipDialog(LogLookNoteView.this.context, 0, LogLookNoteView.this.fileName, LogLookNoteView.this.content, LogLookNoteView.this.context.getString(R.string.button_ok), "", "", true);
                scrollTipDialog.setSvMsgSize(LogLookNoteView.this.context.getResources().getDimensionPixelSize(R.dimen.x800), LogLookNoteView.this.context.getResources().getDimensionPixelSize(R.dimen.y1300));
                scrollTipDialog.setMsgTextSize(LogLookNoteView.this.context.getResources().getDimensionPixelSize(R.dimen.text_xsmall));
                scrollTipDialog.show();
            }
        };
        this.MIME_MapTable = new String[][]{new String[]{".3gp", MimeTypes.VIDEO_H263}, new String[]{".apk", "application/vnd.android.package-archive"}, new String[]{".asf", "video/x-ms-asf"}, new String[]{".avi", "video/x-msvideo"}, new String[]{".bin", RequestParams.APPLICATION_OCTET_STREAM}, new String[]{".bmp", "image/bmp"}, new String[]{".c", "text/plain"}, new String[]{".class", RequestParams.APPLICATION_OCTET_STREAM}, new String[]{".conf", "text/plain"}, new String[]{".cpp", "text/plain"}, new String[]{".doc", "application/msword"}, new String[]{".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, new String[]{".xls", "application/vnd.ms-excel"}, new String[]{".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, new String[]{".exe", RequestParams.APPLICATION_OCTET_STREAM}, new String[]{".gif", "image/gif"}, new String[]{".gtar", "application/x-gtar"}, new String[]{".gz", "application/x-gzip"}, new String[]{".h", "text/plain"}, new String[]{".htm", "text/html"}, new String[]{".html", "text/html"}, new String[]{".jar", "application/java-archive"}, new String[]{".java", "text/plain"}, new String[]{".jpeg", "image/jpeg"}, new String[]{".jpg", "image/jpeg"}, new String[]{".js", "application/x-javascript"}, new String[]{".log", "text/plain"}, new String[]{".m3u", "audio/x-mpegurl"}, new String[]{".m4a", MimeTypes.AUDIO_AAC}, new String[]{".m4b", MimeTypes.AUDIO_AAC}, new String[]{".m4p", MimeTypes.AUDIO_AAC}, new String[]{".m4u", "video/vnd.mpegurl"}, new String[]{".m4v", "video/x-m4v"}, new String[]{".mov", "video/quicktime"}, new String[]{".mp2", "audio/x-mpeg"}, new String[]{".mp3", "audio/x-mpeg"}, new String[]{".mp4", MimeTypes.VIDEO_MP4}, new String[]{".mpc", "application/vnd.mpohun.certificate"}, new String[]{".mpe", "video/mpeg"}, new String[]{".mpeg", "video/mpeg"}, new String[]{".mpg", "video/mpeg"}, new String[]{".mpg4", MimeTypes.VIDEO_MP4}, new String[]{".mpga", MimeTypes.AUDIO_MPEG}, new String[]{".msg", "application/vnd.ms-outlook"}, new String[]{".ogg", "audio/ogg"}, new String[]{".pdf", "application/pdf"}, new String[]{".png", "image/png"}, new String[]{".pps", "application/vnd.ms-powerpoint"}, new String[]{".ppt", "application/vnd.ms-powerpoint"}, new String[]{".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, new String[]{".prop", "text/plain"}, new String[]{".rc", "text/plain"}, new String[]{".rmvb", "audio/x-pn-realaudio"}, new String[]{".rtf", "application/rtf"}, new String[]{".sh", "text/plain"}, new String[]{".tar", "application/x-tar"}, new String[]{".tgz", "application/x-compressed"}, new String[]{".txt", "text/plain"}, new String[]{".wav", "audio/x-wav"}, new String[]{".wma", "audio/x-ms-wma"}, new String[]{".wmv", "audio/x-ms-wmv"}, new String[]{".wps", "application/vnd.ms-works"}, new String[]{".xml", "text/plain"}, new String[]{".z", "application/x-compress"}, new String[]{".zip", "application/x-zip-compressed"}, new String[]{"", "*/*"}};
        initView();
        getFiles();
        showList();
    }

    public void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_look_log_item, (ViewGroup) null);
        this.lv_file = (ListView) inflate.findViewById(R.id.lv_file);
        addContentView(inflate);
    }

    /* loaded from: classes2.dex */
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return LogLookNoteView.this.fileList.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.layout_look_log_list_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_name.setText(((File) LogLookNoteView.this.fileList.get(i)).getName());
            return view;
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public TextView tv_name;

            public ViewHolder() {
            }
        }
    }

    private void showList() {
        this.lv_file.setAdapter((ListAdapter) new MyAdapter(this.context));
        this.lv_file.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shj.setting.widget.LogLookNoteView.1
            AnonymousClass1() {
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Uri fromFile;
                File file = (File) LogLookNoteView.this.fileList.get(i);
                if (file.length() < 10240) {
                    LogLookNoteView.this.loadingDialog = new LoadingDialog(LogLookNoteView.this.context, LogLookNoteView.this.context.getString(R.string.loading));
                    LogLookNoteView.this.fileName = file.getName();
                    new Thread(new Runnable() { // from class: com.shj.setting.widget.LogLookNoteView.1.1
                        final /* synthetic */ File val$file;

                        RunnableC00761(File file2) {
                            file = file2;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            LogLookNoteView.this.content = LogLookNoteView.this.readFile(file);
                            LogLookNoteView.this.handler.sendEmptyMessage(0);
                        }
                    }).start();
                    LogLookNoteView.this.loadingDialog.show();
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.addFlags(268435456);
                    intent.setAction("android.intent.action.VIEW");
                    String mIMEType = LogLookNoteView.this.getMIMEType(file2);
                    if (Build.VERSION.SDK_INT >= 24) {
                        fromFile = FileProvider.getUriForFile(LogLookNoteView.this.context, "com.xyshj.machine.fileProvider", file2);
                        intent.addFlags(1);
                    } else {
                        fromFile = Uri.fromFile(file2);
                    }
                    intent.setDataAndType(fromFile, mIMEType);
                    LogLookNoteView.this.context.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                }
            }

            /* renamed from: com.shj.setting.widget.LogLookNoteView$1$1 */
            /* loaded from: classes2.dex */
            class RunnableC00761 implements Runnable {
                final /* synthetic */ File val$file;

                RunnableC00761(File file2) {
                    file = file2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    LogLookNoteView.this.content = LogLookNoteView.this.readFile(file);
                    LogLookNoteView.this.handler.sendEmptyMessage(0);
                }
            }
        });
    }

    /* renamed from: com.shj.setting.widget.LogLookNoteView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements AdapterView.OnItemClickListener {
        AnonymousClass1() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            Uri fromFile;
            File file2 = (File) LogLookNoteView.this.fileList.get(i);
            if (file2.length() < 10240) {
                LogLookNoteView.this.loadingDialog = new LoadingDialog(LogLookNoteView.this.context, LogLookNoteView.this.context.getString(R.string.loading));
                LogLookNoteView.this.fileName = file2.getName();
                new Thread(new Runnable() { // from class: com.shj.setting.widget.LogLookNoteView.1.1
                    final /* synthetic */ File val$file;

                    RunnableC00761(File file22) {
                        file = file22;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        LogLookNoteView.this.content = LogLookNoteView.this.readFile(file);
                        LogLookNoteView.this.handler.sendEmptyMessage(0);
                    }
                }).start();
                LogLookNoteView.this.loadingDialog.show();
                return;
            }
            try {
                Intent intent = new Intent();
                intent.addFlags(268435456);
                intent.setAction("android.intent.action.VIEW");
                String mIMEType = LogLookNoteView.this.getMIMEType(file22);
                if (Build.VERSION.SDK_INT >= 24) {
                    fromFile = FileProvider.getUriForFile(LogLookNoteView.this.context, "com.xyshj.machine.fileProvider", file22);
                    intent.addFlags(1);
                } else {
                    fromFile = Uri.fromFile(file22);
                }
                intent.setDataAndType(fromFile, mIMEType);
                LogLookNoteView.this.context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
            }
        }

        /* renamed from: com.shj.setting.widget.LogLookNoteView$1$1 */
        /* loaded from: classes2.dex */
        class RunnableC00761 implements Runnable {
            final /* synthetic */ File val$file;

            RunnableC00761(File file22) {
                file = file22;
            }

            @Override // java.lang.Runnable
            public void run() {
                LogLookNoteView.this.content = LogLookNoteView.this.readFile(file);
                LogLookNoteView.this.handler.sendEmptyMessage(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.widget.LogLookNoteView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends Handler {
        AnonymousClass2() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            LogLookNoteView.this.loadingDialog.dismiss();
            ScrollTipDialog scrollTipDialog = new ScrollTipDialog(LogLookNoteView.this.context, 0, LogLookNoteView.this.fileName, LogLookNoteView.this.content, LogLookNoteView.this.context.getString(R.string.button_ok), "", "", true);
            scrollTipDialog.setSvMsgSize(LogLookNoteView.this.context.getResources().getDimensionPixelSize(R.dimen.x800), LogLookNoteView.this.context.getResources().getDimensionPixelSize(R.dimen.y1300));
            scrollTipDialog.setMsgTextSize(LogLookNoteView.this.context.getResources().getDimensionPixelSize(R.dimen.text_xsmall));
            scrollTipDialog.show();
        }
    }

    private void getFiles() {
        for (File file : new File(SDFileUtils.SDCardRoot + "/xyShj/log").listFiles()) {
            if (file.isFile() && file.getName().contains("机器设置")) {
                this.fileList.add(file);
            }
        }
        Collections.sort(this.fileList);
        Collections.reverse(this.fileList);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v17 */
    /* JADX WARN: Type inference failed for: r6v18 */
    /* JADX WARN: Type inference failed for: r6v19 */
    /* JADX WARN: Type inference failed for: r6v20 */
    /* JADX WARN: Type inference failed for: r6v4, types: [java.lang.String] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x0036 -> B:14:0x0050). Please report as a decompilation issue!!! */
    public String readFile(File file) {
        Exception e;
        String str;
        String readLine;
        BufferedReader bufferedReader = null;
        BufferedReader bufferedReader2 = null;
        bufferedReader = null;
        try {
        } catch (IOException e2) {
            e2.printStackTrace();
            bufferedReader = bufferedReader;
            file = file;
        }
        try {
            try {
                BufferedReader bufferedReader3 = new BufferedReader(new FileReader(file));
                String str2 = "";
                while (true) {
                    try {
                        readLine = bufferedReader3.readLine();
                        if (readLine == null) {
                            break;
                        }
                        str2 = str2 + readLine + StringUtils.LF;
                    } catch (Exception e3) {
                        e = e3;
                        bufferedReader2 = bufferedReader3;
                        str = str2;
                        Loger.writeException("UI", e);
                        bufferedReader = bufferedReader2;
                        file = str;
                        if (bufferedReader2 != null) {
                            bufferedReader2.close();
                            bufferedReader = bufferedReader2;
                            file = str;
                        }
                        return file;
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader = bufferedReader3;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
                boolean equals = str2.equals("");
                String str3 = str2;
                if (equals) {
                    str3 = "没有该时段的日志";
                }
                bufferedReader3.close();
                bufferedReader = readLine;
                file = str3;
            } catch (Exception e5) {
                e = e5;
                str = "";
            }
            return file;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public String getMIMEType(File file) {
        String lowerCase;
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        String str = "*/*";
        if (lastIndexOf < 0 || (lowerCase = name.substring(lastIndexOf, name.length()).toLowerCase()) == "") {
            return "*/*";
        }
        int i = 0;
        while (true) {
            String[][] strArr = this.MIME_MapTable;
            if (i >= strArr.length) {
                return str;
            }
            if (lowerCase.equals(strArr[i][0])) {
                str = this.MIME_MapTable[i][1];
            }
            i++;
        }
    }
}
