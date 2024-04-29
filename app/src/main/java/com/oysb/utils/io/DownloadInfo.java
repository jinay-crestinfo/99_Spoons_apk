package com.oysb.utils.io;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class DownloadInfo implements Serializable {
    public static String afxDownloadinfo = "_downloadinfo";
    private static final long serialVersionUID = 1;
    private String url;
    private String fileName = "";
    private String tmpFlieName = "";
    private String fileDir = "";
    private long fileLength = 0;
    private long finishedLength = 0;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getTmpFlieName() {
        return this.tmpFlieName;
    }

    public void setTmpFlieName(String str) {
        this.tmpFlieName = str;
    }

    public String getFileDir() {
        return this.fileDir;
    }

    public void setFileDir(String str) {
        this.fileDir = str;
    }

    public long getFinishedLength() {
        return this.finishedLength;
    }

    public void setFinishedLength(long j) {
        this.finishedLength = j;
    }

    public long getFileLength() {
        return this.fileLength;
    }

    public void setFileLength(long j) {
        this.fileLength = j;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
