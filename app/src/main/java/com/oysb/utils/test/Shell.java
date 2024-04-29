package com.oysb.utils.test;

import android.app.Activity;
import android.util.DisplayMetrics;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes2.dex */
public class Shell {
    public static int SCREEN_MAX_WIDTH = 1920;
    public static int SCREEN_MAX_HEIGHT = 1080;
    public static int SCREEN_HEIGHT = 0;
    public static int SCREEN_WIDTH = 0;
    public static int aplf = 9;
    static Queue<Command> cmdQueue = new LinkedList<>();
    static ShellRunnable runnable = null;
    static InputStream inputStream = null;
    static OnCommandResultListener resultL = null;
    static int orientation = 0;
    static boolean reverse = false;
    static boolean isReady = false;
    public static int SCREEN_0035MAX = 1080;
    public static int SCREEN_0036MAX = 1920;



    public static void stop() {
    }

    static {
        try {
            Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setReady(boolean ready) {
        isReady = ready;
    }

    public static boolean isReady() {
        return isReady;
    }

    public static void start(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        activity.getResources().getConfiguration();
        orientation = CommonTool.getDisplayRotation(activity);
        ShellRunnable shellRunnable = runnable;
        if (shellRunnable == null || !shellRunnable.running) {
            runnable = new ShellRunnable();
            new Thread(runnable).start();
            cmdQueue.add(new InitCmd());
        }
    }

    public static int getCommandCount() {
        return cmdQueue.size();
    }

    public static void setAplf(int i) {
        aplf = i;
    }

    public static void setReverse(boolean reverse) {
        reverse = reverse;
    }

    public static void clear() {
        synchronized (cmdQueue) {
            cmdQueue.clear();
        }
    }

    public static void addCommand(Command command) {
        if (isReady) {
            try {
                synchronized (cmdQueue) {
                    cmdQueue.add(command);
                    cmdQueue.notifyAll();
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static int getX(int i) {
        if (reverse) {
            return (SCREEN_MAX_WIDTH * i) / SCREEN_WIDTH;
        }
        return (SCREEN_MAX_WIDTH * (SCREEN_WIDTH - i)) / SCREEN_WIDTH;
    }

    public static int getY(int i) {
        return (SCREEN_MAX_HEIGHT * i) / SCREEN_HEIGHT;
    }

    public static void execShellCmd(Command command, OnCommandResultListener onCommandResultListener) {
        try {
            resetReader();
            Loger.writeLog("TEST", "run start cmd:" + command.toString());
            for (CommandItem commandItem : command.items) {
                Loger.writeLog("TEST", "run cmditem:" + commandItem.toString());
                Process exec = Runtime.getRuntime().exec("su");
                inputStream = exec.getInputStream();
                OutputStream outputStream = exec.getOutputStream();
                outputStream.write(commandItem.cmd.getBytes());
                outputStream.flush();
                outputStream.close();
                if (onCommandResultListener != null) {
                    Thread.sleep(1000L);
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    inputStream.close();
                    inputStream = null;
                    onCommandResultListener.onResult(new String(bytes, "UTF-8"));
                }
                if (exec != null) {
                    exec.destroy();
                }
            }
            Loger.writeLog("TEST", "run end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void resetReader() {
        ReadRunnable readRunnable = new ReadRunnable();
        readRunnable.stop();
    }

    private static void readResult(Process process, OnCommandResultListener onCommandResultListener) {
        try {
            inputStream = process.getInputStream();
            resultL = onCommandResultListener;
            ReadRunnable readRunnable = new ReadRunnable();
            Thread thread = new Thread(readRunnable);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ShellRunnable implements Runnable {
        private volatile boolean running;

        public ShellRunnable() {
            this.running = true;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Command command = cmdQueue.poll();
                    if (command != null) {
                        execShellCmd(command, command.resultListener);
                        if (command.getWait() > 0) {
                            Thread.sleep(command.getWait());
                        }
                        synchronized (cmdQueue) {
                            if (cmdQueue.isEmpty()) {
                                cmdQueue.wait();
                            }
                        }
                    } else {
                        Thread.sleep(1000L);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    Loger.safe_inner_exception_catch(e);
                    Loger.writeException("TEST", e);
                }
            }
        }
    }

    static class ReadRunnable implements Runnable {
        private volatile boolean running;

        public ReadRunnable() {
            this.running = true;
        }

        public void stop() {
            this.running = false;
        }

        @Override
        public void run() {
            try {
                if (inputStream == null) {
                    stop();
                    return;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while (running && (line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                if (resultL != null) {
                    resultL.onResult(stringBuilder.toString());
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException("TEST", e);
            }
        }
    }
}
