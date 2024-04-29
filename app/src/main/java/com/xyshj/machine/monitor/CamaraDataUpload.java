package com.xyshj.machine.monitor;

import android.hardware.Camera;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/* loaded from: classes2.dex */
public class CamaraDataUpload {
    public static void upload(String str, int i, byte[] bArr, Camera camera) {
    }

    public static void uploadAudio(String str, int i, byte[] bArr) {
    }

    /* loaded from: classes2.dex */
    public static class MyThread extends Thread {
        private byte[] byteBuffer = new byte[1024];
        private String ipname;
        private ByteArrayOutputStream myoutputstream;
        private OutputStream outsocket;
        private int port;

        public MyThread(ByteArrayOutputStream byteArrayOutputStream, String str, int i) {
            this.myoutputstream = byteArrayOutputStream;
            this.ipname = str;
            this.port = i;
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                Socket socket = new Socket(this.ipname, this.port);
                this.outsocket = socket.getOutputStream();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.myoutputstream.toByteArray());
                while (true) {
                    int read = byteArrayInputStream.read(this.byteBuffer);
                    if (read != -1) {
                        this.outsocket.write(this.byteBuffer, 0, read);
                    } else {
                        this.myoutputstream.flush();
                        this.myoutputstream.close();
                        socket.close();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
