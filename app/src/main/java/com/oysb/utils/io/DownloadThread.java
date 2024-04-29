package com.oysb.utils.io;

import com.oysb.utils.io.file.SDFileUtils;
import java.io.File;

/* loaded from: classes2.dex */
public class DownloadThread extends Thread {
    File fileDir;
    private DownloadInfo fileInfo;
    DownlaodListener l;
    private boolean running = false;

    public DownloadThread(DownloadInfo downloadInfo, DownlaodListener downlaodListener) {
        this.l = downlaodListener;
        this.fileInfo = downloadInfo;
        downloadInfo.setFileName(SDFileUtils.getFileName(downloadInfo.getUrl()));
        downloadInfo.setTmpFlieName(downloadInfo.getFileName() + "_temp");
        File file = new File(downloadInfo.getFileDir());
        this.fileDir = file;
        if (file.exists()) {
            return;
        }
        this.fileDir.mkdir();
    }

    public DownloadInfo getDownloadInfo() {
        return this.fileInfo;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0288 A[Catch: Exception -> 0x02ac, TryCatch #3 {Exception -> 0x02ac, blocks: (B:120:0x027a, B:122:0x0288, B:124:0x02a7, B:109:0x0257, B:126:0x0270, B:127:0x0279), top: B:108:0x0257 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x033e  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0351 A[Catch: Exception -> 0x03bf, TRY_LEAVE, TryCatch #11 {Exception -> 0x03bf, blocks: (B:30:0x0346, B:32:0x0351, B:42:0x03bb, B:35:0x0397, B:37:0x03a5, B:39:0x03b6), top: B:29:0x0346, inners: #16 }] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0333 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0328 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x031d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v42, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v11, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r12v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r12v20 */
    /* JADX WARN: Type inference failed for: r12v21 */
    /* JADX WARN: Type inference failed for: r12v22 */
    /* JADX WARN: Type inference failed for: r12v23 */
    /* JADX WARN: Type inference failed for: r12v24 */
    /* JADX WARN: Type inference failed for: r12v27, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r12v29, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r12v3 */
    /* JADX WARN: Type inference failed for: r12v30 */
    /* JADX WARN: Type inference failed for: r12v32 */
    /* JADX WARN: Type inference failed for: r12v33 */
    /* JADX WARN: Type inference failed for: r12v34 */
    /* JADX WARN: Type inference failed for: r12v6, types: [java.net.URL] */
    /* JADX WARN: Type inference failed for: r12v7 */
    /* JADX WARN: Type inference failed for: r12v8 */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.io.RandomAccessFile] */
    /* JADX WARN: Type inference failed for: r8v10, types: [java.io.RandomAccessFile] */
    /* JADX WARN: Type inference failed for: r8v11 */
    /* JADX WARN: Type inference failed for: r8v15 */
    /* JADX WARN: Type inference failed for: r8v16 */
    /* JADX WARN: Type inference failed for: r8v17 */
    /* JADX WARN: Type inference failed for: r8v7, types: [java.io.RandomAccessFile] */
    /* JADX WARN: Type inference failed for: r8v8 */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 968
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.oysb.utils.io.DownloadThread.run():void");
    }

    public void stopTask() {
        this.running = false;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean z) {
        this.running = z;
    }
}
