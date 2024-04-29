package com.oysb.utils.ftp;

import com.github.mjdev.libaums.fs.UsbFile;
import com.oysb.utils.Loger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;

/* loaded from: classes2.dex */
public class FTPManager {
    FTPClient ftpClient;

    public FTPManager() {
        this.ftpClient = null;
        this.ftpClient = new FTPClient();
    }

    public synchronized boolean connect(String str, int i, String str2, String str3) throws Exception {
        boolean z;
        z = false;
        if (this.ftpClient.isConnected()) {
            this.ftpClient.disconnect();
        }
        this.ftpClient.setDataTimeout(20000);
        this.ftpClient.setControlEncoding("utf-8");
        this.ftpClient.connect(str, i);
        if (FTPReply.isPositiveCompletion(this.ftpClient.getReplyCode()) && this.ftpClient.login(str2, str3)) {
            z = true;
        }
        Loger.writeLog("FTP", z ? "Ftp连接成功" : "Ftp连接失败");
        return z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r1v5, types: [int] */
    public boolean createDirectory(String directoryPath) throws  IOException {
        boolean directoryCreated = false;
        String parentPath = directoryPath.substring(0, directoryPath.lastIndexOf(File.separator) + 1);
        int startIndex = parentPath.startsWith(File.separator) ? 1 : 0;
        int nextIndex = parentPath.indexOf(File.separator, startIndex);

        do {
            String subDirectory = parentPath.substring(startIndex, nextIndex);
            if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                ftpClient.makeDirectory(subDirectory);
                ftpClient.changeWorkingDirectory(subDirectory);
                directoryCreated = true;
            }
            startIndex = nextIndex + 1;
            nextIndex = parentPath.indexOf(File.separator, startIndex);
        } while (nextIndex != -1);

        return directoryCreated;
    }


    public synchronized boolean uploadFile(String localFilePath, String remoteDirectory) {
        File localFile = new File(localFilePath);

        if (!localFile.exists()) {
            System.out.println("Local file does not exist.");
            return false;
        }

        OutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;

        try {
            System.out.println("Local file exists. Name: " + localFile.getName());
            createDirectory(remoteDirectory);
            System.out.println("Server file path: " + remoteDirectory + localFile.getName());

            long localFileSize = localFile.length();
            String fileName = localFile.getName();

            FTPFile[] remoteFiles = this.ftpClient.listFiles(fileName);
            long remoteFileSize = remoteFiles.length == 0 ? 0 : remoteFiles[0].getSize();

            if (localFileSize <= remoteFileSize && this.ftpClient.deleteFile(fileName)) {
                System.out.println("Server file exists, deleting file, starting re-upload.");
                remoteFileSize = 0;
            }

            randomAccessFile = new RandomAccessFile(localFile, "r");
            long chunkSize = localFileSize / 100;

            this.ftpClient.enterLocalPassiveMode();
            this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            this.ftpClient.setRestartOffset(remoteFileSize);
            randomAccessFile.seek(remoteFileSize);

            outputStream = this.ftpClient.appendFileStream(fileName);
            byte[] buffer = new byte[1024];
            long bytesUploaded = 0;
            long lastPercentage = 0;

            int bytesRead;
            while ((bytesRead = randomAccessFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                bytesUploaded += bytesRead;
                long currentPercentage = bytesUploaded / chunkSize;

                if (currentPercentage != lastPercentage && currentPercentage % 10 == 0) {
                    System.out.println("Upload progress: " + currentPercentage);
                }

                lastPercentage = currentPercentage;
            }

            if (this.ftpClient.completePendingCommand()) {
                System.out.println("File uploaded successfully.");
                return true;
            } else {
                System.out.println("File upload failed.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [java.io.OutputStream, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v25 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v9, types: [java.io.OutputStream] */
    public synchronized boolean downloadFile(String localDirectory, String remoteFilePath) {
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            this.ftpClient.enterLocalPassiveMode();
            FTPFile[] remoteFiles = this.ftpClient.listFiles(remoteFilePath);
            if (remoteFiles.length == 0) {
                Loger.writeLog("FTP", "Remote file does not exist.");
                return false;
            }
            Loger.writeLog("FTP", "Remote file exists. Name: " + remoteFiles[0].getName());

            String localFilePath = localDirectory + remoteFiles[0].getName();
            long remoteFileSize = remoteFiles[0].getSize();
            String tempFilePath = localFilePath + "_tmp";

            File localFile = new File(tempFilePath);
            long localFileSize = localFile.exists() ? localFile.length() : 0;

            if (localFileSize > remoteFileSize) {
                Loger.writeLog("FTP", "Local file exists and is larger than the remote file. Deleting local file.");
                localFile.delete();
                return false;
            }

            long chunkSize = remoteFileSize / 100;
            this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            fileOutputStream = new FileOutputStream(tempFilePath, true);
            this.ftpClient.setRestartOffset(localFileSize);
            inputStream = this.ftpClient.retrieveFileStream(remoteFilePath);

            byte[] buffer = new byte[1024];
            long bytesRead = 0;
            long lastPercentage = 0;

            while (true) {
                int read = inputStream.read(buffer);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(buffer, 0, read);
                bytesRead += read;
                long currentPercentage = bytesRead / chunkSize;

                if (currentPercentage != lastPercentage && currentPercentage % 10 == 0) {
                    System.out.println("Download progress: " + currentPercentage);
                }
                lastPercentage = currentPercentage;
            }

            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

            if (localFile.length() == remoteFileSize && this.ftpClient.completePendingCommand()) {
                Loger.writeLog("FTP", "File downloaded successfully.");
                boolean renamed = localFile.renameTo(new File(localFilePath));
                return renamed;
            } else {
                Loger.writeLog("FTP", "File download failed.");
                localFile.deleteOnExit();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void closeFTP() {
        try {
            if (this.ftpClient.isConnected()) {
                this.ftpClient.disconnect();
            }
        } catch (Exception unused) {
        }
        this.ftpClient = null;
    }

    public static void downloadFtpFileByUrl(String str, String str2) {
        int indexOf = str.indexOf(":", 6);
        String substring = str.substring(6, indexOf);
        int i = indexOf + 1;
        int indexOf2 = str.indexOf("@", i);
        String substring2 = str.substring(i, indexOf2);
        int i2 = indexOf2 + 1;
        int indexOf3 = str.indexOf(":", i2);
        String substring3 = str.substring(i2, indexOf3);
        int i3 = indexOf3 + 1;
        int indexOf4 = str.indexOf(UsbFile.separator, i3);
        String substring4 = str.substring(i3, indexOf4);
        String substring5 = str.substring(indexOf4 + 1);
        if (new File(str2 + UsbFile.separator + substring5).exists()) {
            return;
        }
        try {
            saveFtpFile(substring3, Integer.parseInt(substring4), substring, substring2, str2, substring5);
        } catch (Exception unused) {
        }
    }

    public static void saveFtpFile(String host, int port, String username, String password, String remoteFilePath, String localDirectory) {
        FTPManager ftpManager = null;
        try {
            ftpManager = new FTPManager();
            if (ftpManager.connect(host, port, username, password)) {
                ftpManager.downloadFile(remoteFilePath, localDirectory);
            }
        } catch (Exception e) {
            Loger.writeException("广告", e);
        } finally {
            if (ftpManager != null) {
                ftpManager.closeFTP();
            }
        }
    }

}
