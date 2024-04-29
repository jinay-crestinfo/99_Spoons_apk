package com.shj.device.lfpos.command;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.oysb.utils.Loger;
import java.util.Queue;

/* loaded from: classes2.dex */
public class CommandProcessor {
    static final int EVENT_DO_COMMAND = 1000;
    static CommandProcessor processor;
    CommandProcessRunnable commandProcessThread;
    Handler handler = new Handler() { // from class: com.shj.device.lfpos.command.CommandProcessor.1
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                if (message.what != 1000) {
                    return;
                }
                CommandManager.parseReceiveCommand((byte[]) message.obj).doCommand();
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException("LFPOS", e);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.device.lfpos.command.CommandProcessor$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends Handler {
        AnonymousClass1() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                if (message.what != 1000) {
                    return;
                }
                CommandManager.parseReceiveCommand((byte[]) message.obj).doCommand();
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
                Loger.writeException("LFPOS", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class CommandProcessRunnable implements Runnable {
        boolean run;

        private CommandProcessRunnable() {
            this.run = true;
        }

        /* synthetic */ CommandProcessRunnable(CommandProcessor commandProcessor, AnonymousClass1 anonymousClass1) {
            this();
        }

        public void stop() {
            this.run = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            while (this.run) {
                try {
                    Queue<byte[]> receiveCommandQueue = CommandManager.getReceiveCommandQueue();
                    synchronized (receiveCommandQueue) {
                        if (receiveCommandQueue.isEmpty()) {
                            receiveCommandQueue.wait();
                        }
                        Message obtain = Message.obtain();
                        obtain.what = 1000;
                        obtain.obj = receiveCommandQueue.poll();
                        CommandProcessor.this.handler.sendMessage(obtain);
                    }
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    Loger.safe_inner_exception_catch(e);
                } catch (Exception e2) {
                    Loger.safe_inner_exception_catch(e2);
                }
            }
        }
    }

    public static CommandProcessor getProcessor() {
        if (processor == null) {
            processor = new CommandProcessor();
        }
        return processor;
    }

    public void start(Context context) {
        CommandProcessRunnable commandProcessRunnable = this.commandProcessThread;
        if (commandProcessRunnable == null || !commandProcessRunnable.run) {
            this.commandProcessThread = new CommandProcessRunnable();
            Loger.writeLog("LFPOS", "启动命令处理器");
            new Thread(this.commandProcessThread).start();
        }
    }

    private void stop() {
        try {
            this.commandProcessThread.stop();
            this.commandProcessThread = null;
        } catch (Exception e) {
            Loger.safe_inner_exception_catch(e);
        }
    }
}
