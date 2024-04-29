package com.shj.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.serialport.SerialPort;
import android.support.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.ala.ALAdevicestatusrw.ALA_Device_rkWriteSnApi;
import com.baidu.platform.comapi.d;
import com.google.android.exoplayer.text.ttml.TtmlNode;
import com.shj.setting.Dialog.TipDialog;
import com.shj.setting.Utils.SystemPropertiesInvoke;
import com.xyshj.database.setting.AppSetting;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.SocketClient;

/* loaded from: classes2.dex */
public class SnWriteActivity extends Activity {
    private static String[] NUMBER = {"0", "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5", "6", "7", "8", "9", "a", "b", "c", d.a, "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", TtmlNode.TAG_P, "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "B", "C", "D", ExifInterface.LONGITUDE_EAST, "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", ExifInterface.LATITUDE_SOUTH, ExifInterface.GPS_DIRECTION_TRUE, "U", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, ExifInterface.LONGITUDE_WEST, "X", "Y", "Z"};
    private Button bt_back;
    private Button bt_baoking_write;
    private Button bt_create;
    private Button bt_create_radowm;
    private Button bt_create_sn;
    private Button bt_gyhj_write;
    private Button bt_help;
    private Button bt_qz_write;
    private Button bt_save;
    private Button bt_sx_write;
    private Button bt_tq_write;
    private String content = "";
    private EditText et_baoking_sn;
    private EditText et_gyhj_sn;
    private EditText et_machine_id;
    private EditText et_qz_sn;
    private EditText et_sx_sn;
    private EditText et_tq_sn;
    private ImageView iv_baoken_correct;
    private ImageView iv_gyhj_correct;
    private ImageView iv_qz_correct;
    private ImageView iv_sx_correct;
    private ImageView iv_tq_correct;
    private Spinner spinner;
    private TextView tv_content;
    private TextView tv_radown;
    private TextView tv_sn;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_sn_write);
        this.tv_sn = (TextView) findViewById(R.id.tv_sn);
        this.bt_create = (Button) findViewById(R.id.bt_create);
        this.bt_save = (Button) findViewById(R.id.bt_save);
        this.tv_content = (TextView) findViewById(R.id.tv_content);
        this.bt_baoking_write = (Button) findViewById(R.id.bt_baoking_write);
        this.et_baoking_sn = (EditText) findViewById(R.id.et_baoking_sn);
        this.et_gyhj_sn = (EditText) findViewById(R.id.et_gyhj_sn);
        this.bt_gyhj_write = (Button) findViewById(R.id.bt_gyhj_write);
        this.bt_help = (Button) findViewById(R.id.bt_help);
        this.et_sx_sn = (EditText) findViewById(R.id.et_sx_sn);
        this.bt_sx_write = (Button) findViewById(R.id.bt_sx_write);
        this.et_qz_sn = (EditText) findViewById(R.id.et_qz_sn);
        this.bt_qz_write = (Button) findViewById(R.id.bt_qz_write);
        this.et_tq_sn = (EditText) findViewById(R.id.et_tq_sn);
        this.bt_tq_write = (Button) findViewById(R.id.bt_tq_write);
        this.bt_back = (Button) findViewById(R.id.bt_back);
        this.iv_baoken_correct = (ImageView) findViewById(R.id.iv_baoken_correct);
        this.iv_gyhj_correct = (ImageView) findViewById(R.id.iv_gyhj_correct);
        this.iv_qz_correct = (ImageView) findViewById(R.id.iv_qz_correct);
        this.iv_sx_correct = (ImageView) findViewById(R.id.iv_sx_correct);
        this.iv_tq_correct = (ImageView) findViewById(R.id.iv_tq_correct);
        this.et_machine_id = (EditText) findViewById(R.id.et_machine_id);
        this.tv_radown = (TextView) findViewById(R.id.tv_radown);
        this.bt_create_radowm = (Button) findViewById(R.id.bt_create_radowm);
        this.bt_create_sn = (Button) findViewById(R.id.bt_create_sn);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList arrayList = new ArrayList();
        arrayList.add("D1");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.layout_spinner_item_simple, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter((SpinnerAdapter) arrayAdapter);
        String machineId = AppSetting.getMachineId(this, null);
        if (!TextUtils.isEmpty(machineId)) {
            this.et_machine_id.setText(machineId);
        }
        this.bt_create_radowm.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SnWriteActivity.this.tv_radown.setText(SnWriteActivity.this.randomNumber());
            }
        });
        this.bt_create.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SnWriteActivity.this.randomCreate();
            }
        });
        this.bt_help.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
            }

            AnonymousClass3() {
            }
        });
        this.bt_save.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SnWriteActivity.this.save();
            }
        });
        this.tv_sn.setText("本机序列号：" + Build.SERIAL);
        this.bt_baoking_write.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_baoking_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeBaoKingSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.iv_baoken_correct.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.6
            AnonymousClass6() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_baoking_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeBaoKingSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.bt_gyhj_write.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.7
            AnonymousClass7() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_gyhj_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeGYHJSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.iv_gyhj_correct.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.8
            AnonymousClass8() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_gyhj_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeGYHJSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.bt_sx_write.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.9
            AnonymousClass9() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_sx_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeSXSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.iv_sx_correct.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.10
            AnonymousClass10() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_sx_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeSXSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.bt_qz_write.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.11
            AnonymousClass11() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_qz_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeQzSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.iv_qz_correct.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.12
            AnonymousClass12() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_qz_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.witeQzSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.bt_tq_write.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.13
            AnonymousClass13() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_tq_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.writeTianQiSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.iv_tq_correct.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.14
            AnonymousClass14() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String trim = SnWriteActivity.this.et_tq_sn.getText().toString().trim();
                if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else {
                    SnWriteActivity.this.writeTianQiSn(trim);
                    SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
                }
            }
        });
        this.bt_create_sn.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.15
            AnonymousClass15() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int selectedItemPosition = SnWriteActivity.this.spinner.getSelectedItemPosition();
                String str = selectedItemPosition == 0 ? "XUAD1" : selectedItemPosition == 1 ? "XUAD8" : "XUAX8";
                String obj = SnWriteActivity.this.et_machine_id.getText().toString();
                if (TextUtils.isEmpty(obj) || obj.length() != 10) {
                    Toast makeText = Toast.makeText(SnWriteActivity.this, "请输入正确的机器号", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                    return;
                }
                String str2 = str + obj;
                String charSequence = SnWriteActivity.this.tv_radown.getText().toString();
                if (TextUtils.isEmpty(charSequence) || charSequence.length() != 8) {
                    Toast makeText2 = Toast.makeText(SnWriteActivity.this, "请生成随机数", 1);
                    makeText2.setGravity(17, 0, 0);
                    makeText2.show();
                    return;
                }
                String str3 = str2 + charSequence;
                SnWriteActivity.this.et_baoking_sn.setText(str3);
                SnWriteActivity.this.et_gyhj_sn.setText(str3);
                SnWriteActivity.this.et_sx_sn.setText(str3);
                SnWriteActivity.this.et_qz_sn.setText(str3);
                SnWriteActivity.this.et_tq_sn.setText(str3);
            }
        });
        this.bt_back.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.SnWriteActivity.16
            AnonymousClass16() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SnWriteActivity.this.finish();
            }
        });
        showCorrectImage();
        this.bt_create_radowm.performClick();
    }

    /* renamed from: com.shj.setting.SnWriteActivity$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SnWriteActivity.this.tv_radown.setText(SnWriteActivity.this.randomNumber());
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SnWriteActivity.this.randomCreate();
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$3 */
    /* loaded from: classes2.dex */
    class AnonymousClass3 implements View.OnClickListener {
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
        }

        AnonymousClass3() {
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$4 */
    /* loaded from: classes2.dex */
    class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SnWriteActivity.this.save();
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$5 */
    /* loaded from: classes2.dex */
    class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_baoking_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeBaoKingSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$6 */
    /* loaded from: classes2.dex */
    class AnonymousClass6 implements View.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_baoking_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeBaoKingSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$7 */
    /* loaded from: classes2.dex */
    class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_gyhj_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeGYHJSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$8 */
    /* loaded from: classes2.dex */
    class AnonymousClass8 implements View.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_gyhj_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeGYHJSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$9 */
    /* loaded from: classes2.dex */
    class AnonymousClass9 implements View.OnClickListener {
        AnonymousClass9() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_sx_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeSXSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$10 */
    /* loaded from: classes2.dex */
    class AnonymousClass10 implements View.OnClickListener {
        AnonymousClass10() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_sx_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeSXSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$11 */
    /* loaded from: classes2.dex */
    class AnonymousClass11 implements View.OnClickListener {
        AnonymousClass11() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_qz_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeQzSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$12 */
    /* loaded from: classes2.dex */
    class AnonymousClass12 implements View.OnClickListener {
        AnonymousClass12() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_qz_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.witeQzSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$13 */
    /* loaded from: classes2.dex */
    class AnonymousClass13 implements View.OnClickListener {
        AnonymousClass13() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_tq_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.writeTianQiSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$14 */
    /* loaded from: classes2.dex */
    class AnonymousClass14 implements View.OnClickListener {
        AnonymousClass14() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            String trim = SnWriteActivity.this.et_tq_sn.getText().toString().trim();
            if (TextUtils.isEmpty(trim) || trim.length() != 23) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "输入序列号长度错误", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            } else {
                SnWriteActivity.this.writeTianQiSn(trim);
                SnWriteActivity.this.tv_sn.setText("本机序列号：" + Build.SERIAL);
            }
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$15 */
    /* loaded from: classes2.dex */
    class AnonymousClass15 implements View.OnClickListener {
        AnonymousClass15() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int selectedItemPosition = SnWriteActivity.this.spinner.getSelectedItemPosition();
            String str = selectedItemPosition == 0 ? "XUAD1" : selectedItemPosition == 1 ? "XUAD8" : "XUAX8";
            String obj = SnWriteActivity.this.et_machine_id.getText().toString();
            if (TextUtils.isEmpty(obj) || obj.length() != 10) {
                Toast makeText = Toast.makeText(SnWriteActivity.this, "请输入正确的机器号", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
                return;
            }
            String str2 = str + obj;
            String charSequence = SnWriteActivity.this.tv_radown.getText().toString();
            if (TextUtils.isEmpty(charSequence) || charSequence.length() != 8) {
                Toast makeText2 = Toast.makeText(SnWriteActivity.this, "请生成随机数", 1);
                makeText2.setGravity(17, 0, 0);
                makeText2.show();
                return;
            }
            String str3 = str2 + charSequence;
            SnWriteActivity.this.et_baoking_sn.setText(str3);
            SnWriteActivity.this.et_gyhj_sn.setText(str3);
            SnWriteActivity.this.et_sx_sn.setText(str3);
            SnWriteActivity.this.et_qz_sn.setText(str3);
            SnWriteActivity.this.et_tq_sn.setText(str3);
        }
    }

    /* renamed from: com.shj.setting.SnWriteActivity$16 */
    /* loaded from: classes2.dex */
    class AnonymousClass16 implements View.OnClickListener {
        AnonymousClass16() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SnWriteActivity.this.finish();
        }
    }

    private void showCorrectImage() {
        String str = Build.DISPLAY;
        if (str.startsWith("F493")) {
            this.iv_sx_correct.setVisibility(0);
            return;
        }
        if (str.startsWith("ALARK")) {
            this.iv_qz_correct.setVisibility(0);
            return;
        }
        if (str.startsWith("UBIOT") || str.startsWith("msm")) {
            this.iv_gyhj_correct.setVisibility(0);
        } else if (str.startsWith("BK")) {
            this.iv_baoken_correct.setVisibility(0);
        }
    }

    public void witeGYHJSn(String str) {
        execShellCmd("am broadcast -a com.android.serial --es set " + str);
        if (this.iv_gyhj_correct.getVisibility() == 0) {
            showRebootDialog();
        }
    }

    public static void execShellCmd(String str) {
        String str2;
        File file = new File(SerialPort.DEFAULT_SU_PATH);
        File file2 = new File("/system/xbin/ubiot");
        if (file.exists()) {
            str2 = "su";
        } else if (!file2.exists()) {
            return;
        } else {
            str2 = "ubiot";
        }
        try {
            OutputStream outputStream = Runtime.getRuntime().exec(str2).getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(str);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void witeQzSn(String str) {
        try {
            new ALA_Device_rkWriteSnApi().ALA_WriteSn_Rockchip(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.iv_qz_correct.getVisibility() == 0) {
            showRebootDialog();
        }
    }

    public void witeSXSn(String str) {
        SystemPropertiesInvoke.setValue("persist.device.sn", str);
        execRootCmd("sync");
        if (this.iv_sx_correct.getVisibility() == 0) {
            showRebootDialog();
        }
    }

    public void witeBaoKingSn(String str) {
        String str2 = Build.DISPLAY;
        if (str2.contains("BKP910") || str2.startsWith("BZ_B1")) {
            Intent intent = new Intent("com.baoking.buildserial");
            intent.putExtra("serial", str);
            sendBroadcast(intent);
        } else {
            String execRootCmd = execRootCmd("getprop ro.wechatpay.sn");
            if (execRootCmd.isEmpty() || !execRootCmd.trim().equals(str)) {
                execRootCmd("mount -o rw,remount /system");
                execRootCmd("sed -i '/ro.wechatpay.sn/d' /system/build.prop");
                execRootCmd("echo ro.wechatpay.sn=" + str + " >>  /system/build.prop");
            }
        }
        if (this.iv_baoken_correct.getVisibility() == 0) {
            showRebootDialog();
        }
    }

    public void writeTianQiSn(String str) {
        sendBroadcast(new Intent("com.tchip.intent.action.command").putExtra("command", "serialno").putExtra("value", str));
        if (this.iv_tq_correct.getVisibility() == 0) {
            showRebootDialog();
        }
    }

    private void showRebootDialog() {
        new TipDialog(this, "请过两分钟后，断电重启，再核对下序列号是否写成功！", "确定").show();
    }

    public String execRootCmd(String str) {
        String str2 = "";
        DataOutputStream dataOutputStream = null;
        try {
            try {
                try {
                    Process exec = Runtime.getRuntime().exec("su");
                    DataOutputStream dataOutputStream2 = new DataOutputStream(exec.getOutputStream());
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(exec.getInputStream())));
                        Log.i("baoking", "execRootCmd: " + str);
                        dataOutputStream2.writeBytes(str + StringUtils.LF);
                        dataOutputStream2.flush();
                        dataOutputStream2.writeBytes("exit\n");
                        dataOutputStream2.flush();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            Log.i("baoking", "execRootCmd read" + readLine);
                            str2 = str2 + readLine + StringUtils.LF;
                        }
                        exec.waitFor();
                        dataOutputStream2.close();
                    } catch (Exception e) {
                        e = e;
                        dataOutputStream = dataOutputStream2;
                        e.printStackTrace();
                        if (dataOutputStream != null) {
                            dataOutputStream.close();
                        }
                        return str2;
                    } catch (Throwable th) {
                        th = th;
                        dataOutputStream = dataOutputStream2;
                        if (dataOutputStream != null) {
                            try {
                                dataOutputStream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (IOException e4) {
            e4.printStackTrace();
        }
        return str2;
    }

    public void randomCreate() {
        Random random = new Random();
        String str = "";
        for (int i = 0; i < 8; i++) {
            str = str + NUMBER[random.nextInt(NUMBER.length)];
        }
        String str2 = this.content + str + ",";
        this.content = str2;
        this.tv_content.setText(str2);
    }

    public String randomNumber() {
        Random random = new Random();
        String str = "";
        for (int i = 0; i < 8; i++) {
            str = str + NUMBER[random.nextInt(NUMBER.length)];
        }
        return str;
    }

    public void save() {
        String str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        File file = new File(str + "随机数.txt");
        if (file.exists()) {
            file.delete();
        }
        String str2 = this.content;
        if (str2 != null) {
            writeTxtToFile(str2, str, "随机数.txt");
            Toast makeText = Toast.makeText(this, "保存完成：sdcard\\随机数.txt", 1);
            makeText.setGravity(17, 0, 0);
            makeText.show();
        }
    }

    public static void writeTxtToFile(String str, String str2, String str3) {
        makeFilePath(str2, str3);
        String str4 = str2 + str3;
        String str5 = str + SocketClient.NETASCII_EOL;
        try {
            File file = new File(str4);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + str4);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(str5.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    public static File makeFilePath(String str, String str2) {
        makeRootDirectory(str);
        File file = null;
        try {
            File file2 = new File(str + str2);
            try {
                if (file2.exists()) {
                    return file2;
                }
                file2.createNewFile();
                return file2;
            } catch (Exception e) {
                e = e;
                file = file2;
                e.printStackTrace();
                return file;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public static void makeRootDirectory(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                return;
            }
            file.mkdir();
        } catch (Exception unused) {
        }
    }
}
