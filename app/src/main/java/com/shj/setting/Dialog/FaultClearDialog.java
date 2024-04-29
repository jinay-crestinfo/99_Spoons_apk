package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.biz.ShjManager;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.UpdataGoodsInfoUIEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class FaultClearDialog extends Dialog {
    private Button bt_cancel;
    private Button bt_ok;
    private int clearCommondCount;
    private Context context;
    private List<FaultInfo> faultInfoList;
    private TextView tv_msg;

    /* loaded from: classes2.dex */
    public static class FaultInfo {
        public static final int TYPE_GEAE = 1;
        public static final int TYPE_LIFT = 2;
        public static final int TYPE_SEIZINGUP = 0;
        public String info;
        public boolean isHaveFault;
        public int type;
    }

    static /* synthetic */ int access$310(FaultClearDialog faultClearDialog) {
        int i = faultClearDialog.clearCommondCount;
        faultClearDialog.clearCommondCount = i - 1;
        return i;
    }

    public FaultClearDialog(Context context) {
        super(context, R.style.loading_style);
        this.faultInfoList = new ArrayList();
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_fault_clear_dialog);
        this.bt_cancel = (Button) findViewById(R.id.button01);
        this.bt_ok = (Button) findViewById(R.id.button02);
        this.tv_msg = (TextView) findViewById(R.id.tv_msg);
        setCanceledOnTouchOutside(false);
        setListener();
        queryFaultInfo();
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FaultClearDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FaultClearDialog.this.dismiss();
            }
        });
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了一键清除");
                FaultClearDialog.this.clearFaultInfo();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了一键清除");
            FaultClearDialog.this.clearFaultInfo();
        }
    }

    private void queryFaultInfo() {
        querySeizingUpShelf();
        queryFaultGear();
        queryLiftFault();
    }

    private void queryLiftFault() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearLiftError(false);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.3
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass3() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr.toString().equals("0")) {
                    ToastUitl.showShort(FaultClearDialog.this.context, R.string.qurey_fail);
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                FaultClearDialog.this.addFaultInfo(2, intFromBytes > 0, "" + FaultClearDialog.this.context.getString(R.string.number_lift_failures) + intFromBytes);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass3() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr.toString().equals("0")) {
                ToastUitl.showShort(FaultClearDialog.this.context, R.string.qurey_fail);
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            FaultClearDialog.this.addFaultInfo(2, intFromBytes > 0, "" + FaultClearDialog.this.context.getString(R.string.number_lift_failures) + intFromBytes);
        }
    }

    private void queryFaultGear() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearEnginError(false);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.4
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass4() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                String str;
                if (bArr.toString().equals("0")) {
                    ToastUitl.showShort(FaultClearDialog.this.context, R.string.qurey_fail);
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                String str2 = "" + FaultClearDialog.this.context.getString(R.string.gear_box_fault_cargo);
                if (intFromBytes == 0) {
                    str = str2 + FaultClearDialog.this.context.getString(R.string.no_have);
                } else {
                    for (int i = 0; i < intFromBytes; i++) {
                        str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i * 2) + 1, 2));
                        if (i != intFromBytes - 1) {
                            str2 = str2 + ",";
                        }
                    }
                    str = str2;
                }
                FaultClearDialog.this.addFaultInfo(1, intFromBytes > 0, str + StringUtils.LF);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass4() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            String str;
            if (bArr.toString().equals("0")) {
                ToastUitl.showShort(FaultClearDialog.this.context, R.string.qurey_fail);
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            String str2 = "" + FaultClearDialog.this.context.getString(R.string.gear_box_fault_cargo);
            if (intFromBytes == 0) {
                str = str2 + FaultClearDialog.this.context.getString(R.string.no_have);
            } else {
                for (int i = 0; i < intFromBytes; i++) {
                    str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i * 2) + 1, 2));
                    if (i != intFromBytes - 1) {
                        str2 = str2 + ",";
                    }
                }
                str = str2;
            }
            FaultClearDialog.this.addFaultInfo(1, intFromBytes > 0, str + StringUtils.LF);
        }
    }

    private void querySeizingUpShelf() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearBlocks(false);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.5
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass5() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                String str;
                if (bArr.toString().equals("0")) {
                    ToastUitl.showShort(FaultClearDialog.this.context, R.string.qurey_fail);
                    return;
                }
                boolean z = true;
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                String str2 = "" + FaultClearDialog.this.context.getString(R.string.number_of_cargo_lanes);
                if (intFromBytes == 0) {
                    str2 = str2 + ":" + FaultClearDialog.this.context.getString(R.string.no_have);
                } else {
                    for (int i = 0; i < intFromBytes; i++) {
                        str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i * 2) + 1, 2));
                        if (i != intFromBytes - 1) {
                            str2 = str2 + ",";
                        }
                    }
                }
                String str3 = str2 + StringUtils.LF;
                int i2 = intFromBytes * 2;
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, i2 + 1, 1);
                String str4 = str3 + FaultClearDialog.this.context.getString(R.string.number_of_failure_channels);
                if (intFromBytes2 == 0) {
                    str = str4 + FaultClearDialog.this.context.getString(R.string.no_have);
                } else {
                    for (int i3 = 0; i3 < intFromBytes2; i3++) {
                        str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i2 + 2 + (i3 * 2), 2));
                        if (i3 != intFromBytes - 1) {
                            str4 = str4 + ",";
                        }
                    }
                    str = str4;
                }
                FaultClearDialog faultClearDialog = FaultClearDialog.this;
                if (intFromBytes <= 0 && intFromBytes2 <= 0) {
                    z = false;
                }
                faultClearDialog.addFaultInfo(0, z, str + StringUtils.LF);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass5() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            String str;
            if (bArr.toString().equals("0")) {
                ToastUitl.showShort(FaultClearDialog.this.context, R.string.qurey_fail);
                return;
            }
            boolean z = true;
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            String str2 = "" + FaultClearDialog.this.context.getString(R.string.number_of_cargo_lanes);
            if (intFromBytes == 0) {
                str2 = str2 + ":" + FaultClearDialog.this.context.getString(R.string.no_have);
            } else {
                for (int i = 0; i < intFromBytes; i++) {
                    str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i * 2) + 1, 2));
                    if (i != intFromBytes - 1) {
                        str2 = str2 + ",";
                    }
                }
            }
            String str3 = str2 + StringUtils.LF;
            int i2 = intFromBytes * 2;
            int intFromBytes2 = ObjectHelper.intFromBytes(bArr, i2 + 1, 1);
            String str4 = str3 + FaultClearDialog.this.context.getString(R.string.number_of_failure_channels);
            if (intFromBytes2 == 0) {
                str = str4 + FaultClearDialog.this.context.getString(R.string.no_have);
            } else {
                for (int i3 = 0; i3 < intFromBytes2; i3++) {
                    str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i2 + 2 + (i3 * 2), 2));
                    if (i3 != intFromBytes - 1) {
                        str4 = str4 + ",";
                    }
                }
                str = str4;
            }
            FaultClearDialog faultClearDialog = FaultClearDialog.this;
            if (intFromBytes <= 0 && intFromBytes2 <= 0) {
                z = false;
            }
            faultClearDialog.addFaultInfo(0, z, str + StringUtils.LF);
        }
    }

    private void clearSeizingUpShelf() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearBlocks(true);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.6
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass6() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (!z) {
                    ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.card_cargo_clearance) + FaultClearDialog.this.context.getString(R.string.fail));
                } else {
                    String str = ("" + FaultClearDialog.this.context.getString(R.string.number_of_cargo_lanes) + 0 + StringUtils.LF) + FaultClearDialog.this.context.getString(R.string.number_of_failure_channels) + 0;
                    FaultClearDialog.this.addFaultInfo(0, false, str + StringUtils.LF);
                    ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.card_cargo_clearance) + FaultClearDialog.this.context.getString(R.string.success));
                    try {
                        ShjManager.clearShelfBlock();
                        Iterator<Integer> it = Shj.getShelves().iterator();
                        while (it.hasNext()) {
                            try {
                                Shj.onUpdateShelfState(it.next().intValue(), 0);
                            } catch (Exception unused) {
                            }
                        }
                    } catch (Exception unused2) {
                    }
                }
                FaultClearDialog.access$310(FaultClearDialog.this);
                FaultClearDialog.this.frashView();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass6() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (!z) {
                ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.card_cargo_clearance) + FaultClearDialog.this.context.getString(R.string.fail));
            } else {
                String str = ("" + FaultClearDialog.this.context.getString(R.string.number_of_cargo_lanes) + 0 + StringUtils.LF) + FaultClearDialog.this.context.getString(R.string.number_of_failure_channels) + 0;
                FaultClearDialog.this.addFaultInfo(0, false, str + StringUtils.LF);
                ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.card_cargo_clearance) + FaultClearDialog.this.context.getString(R.string.success));
                try {
                    ShjManager.clearShelfBlock();
                    Iterator<Integer> it = Shj.getShelves().iterator();
                    while (it.hasNext()) {
                        try {
                            Shj.onUpdateShelfState(it.next().intValue(), 0);
                        } catch (Exception unused) {
                        }
                    }
                } catch (Exception unused2) {
                }
            }
            FaultClearDialog.access$310(FaultClearDialog.this);
            FaultClearDialog.this.frashView();
        }
    }

    private void clearGearFault() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearEnginError(true);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.7
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass7() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (!z) {
                    ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearDialog.this.context.getString(R.string.fail));
                } else {
                    String str = "" + FaultClearDialog.this.context.getString(R.string.gear_box_fault_cargo) + 0;
                    FaultClearDialog.this.addFaultInfo(1, false, str + StringUtils.LF);
                    ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearDialog.this.context.getString(R.string.success));
                }
                FaultClearDialog.access$310(FaultClearDialog.this);
                FaultClearDialog.this.frashView();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass7() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (!z) {
                ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearDialog.this.context.getString(R.string.fail));
            } else {
                String str = "" + FaultClearDialog.this.context.getString(R.string.gear_box_fault_cargo) + 0;
                FaultClearDialog.this.addFaultInfo(1, false, str + StringUtils.LF);
                ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearDialog.this.context.getString(R.string.success));
            }
            FaultClearDialog.access$310(FaultClearDialog.this);
            FaultClearDialog.this.frashView();
        }
    }

    private void clearLiftFault() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearLiftError(true);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.FaultClearDialog.8
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass8() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (!z) {
                    ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.lift_fault_clearance) + FaultClearDialog.this.context.getString(R.string.fail));
                } else {
                    String str = "" + FaultClearDialog.this.context.getString(R.string.number_lift_failures) + 0;
                    FaultClearDialog.this.addFaultInfo(2, false, str + StringUtils.LF);
                    ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.lift_fault_clearance) + FaultClearDialog.this.context.getString(R.string.success));
                }
                FaultClearDialog.access$310(FaultClearDialog.this);
                FaultClearDialog.this.frashView();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FaultClearDialog$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass8() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (!z) {
                ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.lift_fault_clearance) + FaultClearDialog.this.context.getString(R.string.fail));
            } else {
                String str = "" + FaultClearDialog.this.context.getString(R.string.number_lift_failures) + 0;
                FaultClearDialog.this.addFaultInfo(2, false, str + StringUtils.LF);
                ToastUitl.showShort(FaultClearDialog.this.context, FaultClearDialog.this.context.getString(R.string.lift_fault_clearance) + FaultClearDialog.this.context.getString(R.string.success));
            }
            FaultClearDialog.access$310(FaultClearDialog.this);
            FaultClearDialog.this.frashView();
        }
    }

    public void frashView() {
        if (this.clearCommondCount == 0) {
            EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
        }
    }

    public void clearFaultInfo() {
        this.clearCommondCount = 0;
        for (FaultInfo faultInfo : this.faultInfoList) {
            if (faultInfo.isHaveFault) {
                if (faultInfo.type == 0) {
                    this.clearCommondCount++;
                    clearSeizingUpShelf();
                } else if (1 == faultInfo.type) {
                    this.clearCommondCount++;
                    clearGearFault();
                } else if (2 == faultInfo.type) {
                    this.clearCommondCount++;
                    clearLiftFault();
                }
            }
        }
    }

    public void addFaultInfo(int i, boolean z, String str) {
        boolean z2;
        synchronized (this.faultInfoList) {
            Iterator<FaultInfo> it = this.faultInfoList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z2 = false;
                    break;
                }
                FaultInfo next = it.next();
                if (i == next.type) {
                    next.isHaveFault = z;
                    next.info = str;
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                FaultInfo faultInfo = new FaultInfo();
                faultInfo.type = i;
                faultInfo.isHaveFault = z;
                faultInfo.info = str;
                this.faultInfoList.add(faultInfo);
            }
            String str2 = "";
            boolean z3 = false;
            for (FaultInfo faultInfo2 : this.faultInfoList) {
                str2 = str2 + faultInfo2.info;
                if (faultInfo2.isHaveFault) {
                    z3 = true;
                }
            }
            this.tv_msg.setText(str2);
            if (z3) {
                this.bt_ok.setVisibility(0);
            }
        }
    }
}
