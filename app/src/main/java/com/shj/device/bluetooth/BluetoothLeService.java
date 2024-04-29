package com.shj.device.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.iflytek.cloud.SpeechConstant;
import java.util.List;
import java.util.UUID;

/* loaded from: classes2.dex */
public class BluetoothLeService extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_GATT_SERVICES_NO_DISCOVERED = "com.example.bluetooth.le.GATT_SERVICES_NO_DISCOVERED";
    public static final String ACTION_WRITE_SUCCESSFUL = "com.example.bluetooth.le.WRITE_SUCCESSFUL";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final String TAG = "BluetoothLeService";
    public static final UUID UUID_BLE_SPP_NOTIFY = UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic);
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;
    private int mConnectionState = 0;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() { // from class: com.shj.device.bluetooth.BluetoothLeService.1
        AnonymousClass1() {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 != 2) {
                if (i2 == 0) {
                    BluetoothLeService.this.mConnectionState = 0;
                    Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
                    BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_DISCONNECTED);
                    return;
                }
                return;
            }
            BluetoothLeService.this.mConnectionState = 2;
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_CONNECTED);
            Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
            Log.i(BluetoothLeService.TAG, "Attempting to start service discovery:" + BluetoothLeService.this.mBluetoothGatt.discoverServices());
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            if (i != 0) {
                Log.w(BluetoothLeService.TAG, "onServicesDiscovered received: " + i);
                return;
            }
            BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(BleSppGattAttributes.BLE_SPP_Service));
            if (service != null) {
                BluetoothLeService.this.mNotifyCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic));
                BluetoothLeService.this.mWriteCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Write_Characteristic));
            }
            if (BluetoothLeService.this.mNotifyCharacteristic != null) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
                BluetoothLeService bluetoothLeService = BluetoothLeService.this;
                bluetoothLeService.setCharacteristicNotification(bluetoothLeService.mNotifyCharacteristic, true);
            }
            if (BluetoothLeService.this.mWriteCharacteristic == null) {
                BluetoothLeService.this.mWriteCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic));
            }
            if (service == null) {
                Log.v("log", "service is null");
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_WRITE_SUCCESSFUL);
                Log.v("log", "Write OK");
            }
        }
    };
    private final IBinder mBinder = new LocalBinder();

    /* renamed from: com.shj.device.bluetooth.BluetoothLeService$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 extends BluetoothGattCallback {
        AnonymousClass1() {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 != 2) {
                if (i2 == 0) {
                    BluetoothLeService.this.mConnectionState = 0;
                    Log.i(BluetoothLeService.TAG, "Disconnected from GATT server.");
                    BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_DISCONNECTED);
                    return;
                }
                return;
            }
            BluetoothLeService.this.mConnectionState = 2;
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_CONNECTED);
            Log.i(BluetoothLeService.TAG, "Connected to GATT server.");
            Log.i(BluetoothLeService.TAG, "Attempting to start service discovery:" + BluetoothLeService.this.mBluetoothGatt.discoverServices());
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            if (i != 0) {
                Log.w(BluetoothLeService.TAG, "onServicesDiscovered received: " + i);
                return;
            }
            BluetoothGattService service = bluetoothGatt.getService(UUID.fromString(BleSppGattAttributes.BLE_SPP_Service));
            if (service != null) {
                BluetoothLeService.this.mNotifyCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic));
                BluetoothLeService.this.mWriteCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Write_Characteristic));
            }
            if (BluetoothLeService.this.mNotifyCharacteristic != null) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
                BluetoothLeService bluetoothLeService = BluetoothLeService.this;
                bluetoothLeService.setCharacteristicNotification(bluetoothLeService.mNotifyCharacteristic, true);
            }
            if (BluetoothLeService.this.mWriteCharacteristic == null) {
                BluetoothLeService.this.mWriteCharacteristic = service.getCharacteristic(UUID.fromString(BleSppGattAttributes.BLE_SPP_Notify_Characteristic));
            }
            if (service == null) {
                Log.v("log", "service is null");
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_GATT_SERVICES_NO_DISCOVERED);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_DATA_AVAILABLE, bluetoothGattCharacteristic);
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
            if (i == 0) {
                BluetoothLeService.this.broadcastUpdate(BluetoothLeService.ACTION_WRITE_SUCCESSFUL);
                Log.v("log", "Write OK");
            }
        }
    }

    public void broadcastUpdate(String str) {
        sendBroadcast(new Intent(str));
    }

    public void broadcastUpdate(String str, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value;
        Intent intent = new Intent(str);
        if (UUID_BLE_SPP_NOTIFY.equals(bluetoothGattCharacteristic.getUuid()) && (value = bluetoothGattCharacteristic.getValue()) != null && value.length > 0) {
            intent.putExtra(EXTRA_DATA, value);
        }
        sendBroadcast(intent);
    }

    /* loaded from: classes2.dex */
    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    public boolean initialize() {
        if (this.mBluetoothManager == null) {
            BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(SpeechConstant.BLUETOOTH);
            this.mBluetoothManager = bluetoothManager;
            if (bluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        BluetoothAdapter adapter = this.mBluetoothManager.getAdapter();
        this.mBluetoothAdapter = adapter;
        if (adapter != null) {
            return true;
        }
        Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
        return false;
    }

    public boolean connect(String str) {
        if (this.mBluetoothAdapter == null || str == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        String str2 = this.mBluetoothDeviceAddress;
        if (str2 != null && str.equals(str2) && this.mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (!this.mBluetoothGatt.connect()) {
                return false;
            }
            this.mConnectionState = 1;
            return true;
        }
        BluetoothDevice remoteDevice = this.mBluetoothAdapter.getRemoteDevice(str);
        if (remoteDevice == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        this.mBluetoothGatt = remoteDevice.connectGatt(this, false, this.mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        this.mBluetoothDeviceAddress = str;
        this.mConnectionState = 1;
        return true;
    }

    public void disconnect() {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            bluetoothGatt.disconnect();
        }
    }

    public void close() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        this.mBluetoothGatt = null;
    }

    public void readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
        } else {
            bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
        }
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic bluetoothGattCharacteristic, boolean z) {
        BluetoothGatt bluetoothGatt;
        if (this.mBluetoothAdapter == null || (bluetoothGatt = this.mBluetoothGatt) == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, z);
        if (UUID_BLE_SPP_NOTIFY.equals(bluetoothGattCharacteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(BleSppGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            this.mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void writeData(byte[] bArr) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = this.mWriteCharacteristic;
        if (bluetoothGattCharacteristic == null || bArr == null) {
            return;
        }
        bluetoothGattCharacteristic.setValue(bArr);
        this.mBluetoothGatt.writeCharacteristic(this.mWriteCharacteristic);
    }

    public List<BluetoothGattService> getSupportedGattServices() {
        BluetoothGatt bluetoothGatt = this.mBluetoothGatt;
        if (bluetoothGatt == null) {
            return null;
        }
        return bluetoothGatt.getServices();
    }
}
