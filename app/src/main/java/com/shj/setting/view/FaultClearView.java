package com.shj.setting.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes2.dex */
public class FaultClearView extends LinearLayout {
    private Button bt_clear;
    private Context context;
    private List<FaultInfo> faultInfoList;
    private TextView tv_value;

    /* loaded from: classes2.dex */
    public static class FaultInfo {
        public static final int TYPE_GEAE = 1;
        public static final int TYPE_LIFT = 2;
        public static final int TYPE_SEIZINGUP = 0;
        public String info;
        public boolean isHaveFault;
        public int type;
    }

    public FaultClearView(Context context) {
        super(context);
        this.faultInfoList = new ArrayList();
        this.context = context;
        initView();
        setListener();
        queryFaultInfo();
    }

    private void initView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_fault_clear, this);
        this.tv_value = (TextView) inflate.findViewById(R.id.tv_value);
        this.bt_clear = (Button) inflate.findViewById(R.id.bt_clear);
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
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.view.FaultClearView.1
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr.toString().equals("0")) {
                    ToastUitl.showShort(FaultClearView.this.context, R.string.qurey_fail);
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                FaultClearView.this.addFaultInfo(2, intFromBytes > 0, "" + FaultClearView.this.context.getString(R.string.number_lift_failures) + intFromBytes);
            }
        });
    }

    /* renamed from: com.shj.setting.view.FaultClearView$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr.toString().equals("0")) {
                ToastUitl.showShort(FaultClearView.this.context, R.string.qurey_fail);
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            FaultClearView.this.addFaultInfo(2, intFromBytes > 0, "" + FaultClearView.this.context.getString(R.string.number_lift_failures) + intFromBytes);
        }
    }

    private void queryFaultGear() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearEnginError(false);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.view.FaultClearView.2
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass2() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                String str;
                if (bArr.toString().equals("0")) {
                    ToastUitl.showShort(FaultClearView.this.context, R.string.qurey_fail);
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                String str2 = "" + FaultClearView.this.context.getString(R.string.gear_box_fault_cargo);
                if (intFromBytes == 0) {
                    str = str2 + FaultClearView.this.context.getString(R.string.no_have);
                } else {
                    for (int i = 0; i < intFromBytes; i++) {
                        str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i * 2) + 1, 2));
                        if (i != intFromBytes - 1) {
                            str2 = str2 + ",";
                        }
                    }
                    str = str2;
                }
                FaultClearView.this.addFaultInfo(1, intFromBytes > 0, str + StringUtils.LF);
            }
        });
    }

    /* renamed from: com.shj.setting.view.FaultClearView$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            String str;
            if (bArr.toString().equals("0")) {
                ToastUitl.showShort(FaultClearView.this.context, R.string.qurey_fail);
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            String str2 = "" + FaultClearView.this.context.getString(R.string.gear_box_fault_cargo);
            if (intFromBytes == 0) {
                str = str2 + FaultClearView.this.context.getString(R.string.no_have);
            } else {
                for (int i = 0; i < intFromBytes; i++) {
                    str2 = str2 + String.valueOf(ObjectHelper.intFromBytes(bArr, (i * 2) + 1, 2));
                    if (i != intFromBytes - 1) {
                        str2 = str2 + ",";
                    }
                }
                str = str2;
            }
            FaultClearView.this.addFaultInfo(1, intFromBytes > 0, str + StringUtils.LF);
        }
    }

    private void querySeizingUpShelf() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearBlocks(false);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.view.FaultClearView.3
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass3() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                String str;
                if (bArr.toString().equals("0")) {
                    ToastUitl.showShort(FaultClearView.this.context, R.string.qurey_fail);
                    return;
                }
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                String str2 = "" + FaultClearView.this.context.getString(R.string.number_of_cargo_lanes);
                if (intFromBytes == 0) {
                    str2 = str2 + ":" + FaultClearView.this.context.getString(R.string.no_have);
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
                String str4 = str3 + FaultClearView.this.context.getString(R.string.number_of_failure_channels);
                if (intFromBytes2 == 0) {
                    str = str4 + FaultClearView.this.context.getString(R.string.no_have);
                } else {
                    for (int i3 = 0; i3 < intFromBytes2; i3++) {
                        str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i2 + 2 + (i3 * 2), 2));
                        if (i3 != intFromBytes - 1) {
                            str4 = str4 + ",";
                        }
                    }
                    str = str4;
                }
                FaultClearView.this.addFaultInfo(0, intFromBytes > 0 && intFromBytes2 > 0, str + StringUtils.LF);
            }
        });
    }

    /* renamed from: com.shj.setting.view.FaultClearView$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass3() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            String str;
            if (bArr.toString().equals("0")) {
                ToastUitl.showShort(FaultClearView.this.context, R.string.qurey_fail);
                return;
            }
            int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
            String str2 = "" + FaultClearView.this.context.getString(R.string.number_of_cargo_lanes);
            if (intFromBytes == 0) {
                str2 = str2 + ":" + FaultClearView.this.context.getString(R.string.no_have);
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
            String str4 = str3 + FaultClearView.this.context.getString(R.string.number_of_failure_channels);
            if (intFromBytes2 == 0) {
                str = str4 + FaultClearView.this.context.getString(R.string.no_have);
            } else {
                for (int i3 = 0; i3 < intFromBytes2; i3++) {
                    str4 = str4 + String.valueOf(ObjectHelper.intFromBytes(bArr, i2 + 2 + (i3 * 2), 2));
                    if (i3 != intFromBytes - 1) {
                        str4 = str4 + ",";
                    }
                }
                str = str4;
            }
            FaultClearView.this.addFaultInfo(0, intFromBytes > 0 && intFromBytes2 > 0, str + StringUtils.LF);
        }
    }

    private void clearSeizingUpShelf() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearBlocks(true);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.view.FaultClearView.4
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass4() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (!z) {
                    ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.card_cargo_clearance) + FaultClearView.this.context.getString(R.string.fail));
                    return;
                }
                String str = ("" + FaultClearView.this.context.getString(R.string.number_of_cargo_lanes) + 0 + StringUtils.LF) + FaultClearView.this.context.getString(R.string.number_of_failure_channels) + 0;
                FaultClearView.this.addFaultInfo(0, false, str + StringUtils.LF);
                ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.card_cargo_clearance) + FaultClearView.this.context.getString(R.string.success));
            }
        });
    }

    /* renamed from: com.shj.setting.view.FaultClearView$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass4() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (!z) {
                ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.card_cargo_clearance) + FaultClearView.this.context.getString(R.string.fail));
                return;
            }
            String str = ("" + FaultClearView.this.context.getString(R.string.number_of_cargo_lanes) + 0 + StringUtils.LF) + FaultClearView.this.context.getString(R.string.number_of_failure_channels) + 0;
            FaultClearView.this.addFaultInfo(0, false, str + StringUtils.LF);
            ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.card_cargo_clearance) + FaultClearView.this.context.getString(R.string.success));
        }
    }

    private void clearGearFault() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearEnginError(true);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.view.FaultClearView.5
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass5() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (!z) {
                    ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearView.this.context.getString(R.string.fail));
                    return;
                }
                String str = "" + FaultClearView.this.context.getString(R.string.gear_box_fault_cargo) + 0;
                FaultClearView.this.addFaultInfo(1, false, str + StringUtils.LF);
                ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearView.this.context.getString(R.string.success));
            }
        });
    }

    /* renamed from: com.shj.setting.view.FaultClearView$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass5() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (!z) {
                ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearView.this.context.getString(R.string.fail));
                return;
            }
            String str = "" + FaultClearView.this.context.getString(R.string.gear_box_fault_cargo) + 0;
            FaultClearView.this.addFaultInfo(1, false, str + StringUtils.LF);
            ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.gear_box_fault_clearing) + FaultClearView.this.context.getString(R.string.success));
        }
    }

    private void clearLiftFault() {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setClearLiftError(true);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.view.FaultClearView.6
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass6() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (!z) {
                    ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.lift_fault_clearance) + FaultClearView.this.context.getString(R.string.fail));
                    return;
                }
                String str = "" + FaultClearView.this.context.getString(R.string.number_lift_failures) + 0;
                FaultClearView.this.addFaultInfo(2, false, str + StringUtils.LF);
                ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.lift_fault_clearance) + FaultClearView.this.context.getString(R.string.success));
            }
        });
    }

    /* renamed from: com.shj.setting.view.FaultClearView$6 */
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
                ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.lift_fault_clearance) + FaultClearView.this.context.getString(R.string.fail));
                return;
            }
            String str = "" + FaultClearView.this.context.getString(R.string.number_lift_failures) + 0;
            FaultClearView.this.addFaultInfo(2, false, str + StringUtils.LF);
            ToastUitl.showShort(FaultClearView.this.context, FaultClearView.this.context.getString(R.string.lift_fault_clearance) + FaultClearView.this.context.getString(R.string.success));
        }
    }

    /* renamed from: com.shj.setting.view.FaultClearView$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            FaultClearView.this.clearFaultInfo();
        }
    }

    private void setListener() {
        this.bt_clear.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.view.FaultClearView.7
            AnonymousClass7() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                FaultClearView.this.clearFaultInfo();
            }
        });
    }

    public void clearFaultInfo() {
        for (FaultInfo faultInfo : this.faultInfoList) {
            if (faultInfo.isHaveFault) {
                if (faultInfo.type == 0) {
                    clearSeizingUpShelf();
                } else if (1 == faultInfo.type) {
                    clearGearFault();
                } else if (2 == faultInfo.type) {
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
            this.tv_value.setText(str2);
            if (z3) {
                this.bt_clear.setVisibility(0);
            }
        }
    }
}
