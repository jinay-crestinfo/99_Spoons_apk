package com.shj.device.bluetooth;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import com.oysb.utils.ObjectHelper;
import com.shj.Shj;
import com.shj.command.Command;
import com.shj.command.CommandManager;
import com.shj.device.bluetooth.BluetoothLeService;
import java.io.ByteArrayOutputStream;
import kotlin.UByte;

/* loaded from: classes2.dex */
public class Bluetooth {
    private BluetoothLeService mBluetoothLeService;
    private String bluetoothAdress = "";
    private boolean bluetoothConnected = false;
    private ByteArrayOutputStream mBuffer = new ByteArrayOutputStream();
    private int dataLen = 0;
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.shj.device.bluetooth.Bluetooth.1
        AnonymousClass1() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Bluetooth.this.mBluetoothLeService = ((BluetoothLeService.LocalBinder) iBinder).getService();
            Bluetooth.this.mBluetoothLeService.initialize();
            Bluetooth.this.mBluetoothLeService.connect(Bluetooth.this.bluetoothAdress);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Bluetooth.this.mBluetoothLeService = null;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shj.device.bluetooth.Bluetooth.2
        AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                return;
            }
            if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Bluetooth.this.bluetoothConnected = false;
                Bluetooth.this.mBluetoothLeService.connect(Bluetooth.this.bluetoothAdress);
                return;
            }
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Bluetooth.this.bluetoothConnected = true;
                return;
            }
            if (BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)) {
                Bluetooth.this.mBluetoothLeService.connect(Bluetooth.this.bluetoothAdress);
                return;
            }
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                try {
                    byte[] byteArrayExtra = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                    ObjectHelper.hex2String(byteArrayExtra, byteArrayExtra.length);
                    if ((byteArrayExtra[0] & UByte.MAX_VALUE) != 250 || (byteArrayExtra[1] & UByte.MAX_VALUE) != 251) {
                        Bluetooth.this.mBuffer.write(byteArrayExtra);
                    } else {
                        Bluetooth.this.mBuffer.reset();
                        Bluetooth.this.dataLen = (byteArrayExtra[3] & UByte.MAX_VALUE) + 5;
                        Bluetooth.this.mBuffer.write(byteArrayExtra, 0, byteArrayExtra.length);
                    }
                    Bluetooth.this.mBuffer.flush();
                    if (Bluetooth.this.mBuffer.size() >= Bluetooth.this.dataLen) {
                        byte[] byteArray = Bluetooth.this.mBuffer.toByteArray();
                        ObjectHelper.hex2String(byteArray, byteArray.length);
                        Command parseReceiveCommand = CommandManager.parseReceiveCommand(byteArray);
                        if (parseReceiveCommand != null) {
                            parseReceiveCommand.doCommand();
                            Bluetooth.this.mBuffer.reset();
                            Bluetooth.this.dataLen = 0;
                            return;
                        }
                        return;
                    }
                    return;
                } catch (Exception unused) {
                    return;
                }
            }
            BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action);
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_WRITE_SUCCESSFUL);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED);
        return intentFilter;
    }

    public boolean attachBluetooth(String str) {
        Context context = Shj.getContext();
        context.bindService(new Intent(context, (Class<?>) BluetoothLeService.class), this.mServiceConnection, 1);
        this.bluetoothAdress = str;
        Shj.getContext().registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
        BluetoothLeService bluetoothLeService = this.mBluetoothLeService;
        if (bluetoothLeService != null) {
            bluetoothLeService.connect(this.bluetoothAdress);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.device.bluetooth.Bluetooth$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ServiceConnection {
        AnonymousClass1() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Bluetooth.this.mBluetoothLeService = ((BluetoothLeService.LocalBinder) iBinder).getService();
            Bluetooth.this.mBluetoothLeService.initialize();
            Bluetooth.this.mBluetoothLeService.connect(Bluetooth.this.bluetoothAdress);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Bluetooth.this.mBluetoothLeService = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.device.bluetooth.Bluetooth$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends BroadcastReceiver {
        AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                return;
            }
            if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Bluetooth.this.bluetoothConnected = false;
                Bluetooth.this.mBluetoothLeService.connect(Bluetooth.this.bluetoothAdress);
                return;
            }
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Bluetooth.this.bluetoothConnected = true;
                return;
            }
            if (BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)) {
                Bluetooth.this.mBluetoothLeService.connect(Bluetooth.this.bluetoothAdress);
                return;
            }
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                try {
                    byte[] byteArrayExtra = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                    ObjectHelper.hex2String(byteArrayExtra, byteArrayExtra.length);
                    if ((byteArrayExtra[0] & UByte.MAX_VALUE) != 250 || (byteArrayExtra[1] & UByte.MAX_VALUE) != 251) {
                        Bluetooth.this.mBuffer.write(byteArrayExtra);
                    } else {
                        Bluetooth.this.mBuffer.reset();
                        Bluetooth.this.dataLen = (byteArrayExtra[3] & UByte.MAX_VALUE) + 5;
                        Bluetooth.this.mBuffer.write(byteArrayExtra, 0, byteArrayExtra.length);
                    }
                    Bluetooth.this.mBuffer.flush();
                    if (Bluetooth.this.mBuffer.size() >= Bluetooth.this.dataLen) {
                        byte[] byteArray = Bluetooth.this.mBuffer.toByteArray();
                        ObjectHelper.hex2String(byteArray, byteArray.length);
                        Command parseReceiveCommand = CommandManager.parseReceiveCommand(byteArray);
                        if (parseReceiveCommand != null) {
                            parseReceiveCommand.doCommand();
                            Bluetooth.this.mBuffer.reset();
                            Bluetooth.this.dataLen = 0;
                            return;
                        }
                        return;
                    }
                    return;
                } catch (Exception unused) {
                    return;
                }
            }
            BluetoothLeService.ACTION_WRITE_SUCCESSFUL.equals(action);
        }
    }

    public void writeData(byte[] bArr) {
        if (bArr.length > 20) {
            byte[] bArr2 = new byte[20];
            byte[] bArr3 = new byte[bArr.length - 20];
            System.arraycopy(bArr, 0, bArr2, 0, 20);
            System.arraycopy(bArr, 20, bArr3, 0, bArr.length - 20);
            this.mBluetoothLeService.writeData(bArr2);
            new Handler().postDelayed(new Runnable() { // from class: com.shj.device.bluetooth.Bluetooth.3
                final /* synthetic */ byte[] val$dest2;

                AnonymousClass3(byte[] bArr32) {
                    bArr3 = bArr32;
                }

                @Override // java.lang.Runnable
                public void run() {
                    Bluetooth.this.mBluetoothLeService.writeData(bArr3);
                }
            }, 500L);
            return;
        }
        this.mBluetoothLeService.writeData(bArr);
    }

    /* renamed from: com.shj.device.bluetooth.Bluetooth$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements Runnable {
        final /* synthetic */ byte[] val$dest2;

        AnonymousClass3(byte[] bArr32) {
            bArr3 = bArr32;
        }

        @Override // java.lang.Runnable
        public void run() {
            Bluetooth.this.mBluetoothLeService.writeData(bArr3);
        }
    }

    protected void onResume() {
        Shj.getContext().registerReceiver(this.mGattUpdateReceiver, makeGattUpdateIntentFilter());
        BluetoothLeService bluetoothLeService = this.mBluetoothLeService;
        if (bluetoothLeService != null) {
            bluetoothLeService.connect(this.bluetoothAdress);
        }
    }

    protected void onPause() {
        Shj.getContext().unregisterReceiver(this.mGattUpdateReceiver);
    }

    protected void onDestroy() {
        Shj.getContext().unbindService(this.mServiceConnection);
        this.mBluetoothLeService = null;
    }
}
