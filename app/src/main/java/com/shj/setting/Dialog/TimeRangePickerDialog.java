package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TimePicker;
import com.shj.setting.R;
import com.shj.setting.Utils.CommonUtils;
import java.lang.reflect.Field;
import java.util.List;

/* loaded from: classes2.dex */
public class TimeRangePickerDialog extends Dialog {
    private View cancelBtn;
    private ConfirmAction confirmAction;
    private Context context;
    private String endTime;
    private SeekBar sbSound;
    private int screenWidth;
    private int sound;
    private String startTime;
    private View submitBtn;
    private TimePicker timePickerEnd;
    private TimePicker timePickerStart;

    /* loaded from: classes2.dex */
    public interface ConfirmAction {
        void onLeftClick();

        void onRightClick(String str, String str2, int i);
    }

    public TimeRangePickerDialog(Context context) {
        super(context);
        this.sound = 0;
        this.context = context;
    }

    public TimeRangePickerDialog(Context context, boolean z, DialogInterface.OnCancelListener onCancelListener) {
        super(context, z, onCancelListener);
        this.sound = 0;
        this.context = context;
    }

    public TimeRangePickerDialog(Context context, int i) {
        super(context, i);
        this.sound = 0;
        this.context = context;
    }

    public TimeRangePickerDialog(Context context, String str, int i, ConfirmAction confirmAction) {
        super(context, R.style.goods_detail_style);
        this.sound = 0;
        this.context = context;
        List<String> regEx = CommonUtils.getRegEx(str, "\\d+:\\d+");
        if (!CommonUtils.isNull(regEx) && regEx.size() >= 2) {
            this.startTime = CommonUtils.getRegEx(str, "\\d+:\\d+").get(0);
            this.endTime = CommonUtils.getRegEx(str, "\\d+:\\d+").get(1);
        }
        this.confirmAction = confirmAction;
        this.sound = i;
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels - getDensityValue(500.0f, context);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(17);
        setContentView(LayoutInflater.from(this.context).inflate(R.layout.dialog_time_range_picker, (ViewGroup) null));
        getWindow().setLayout(this.screenWidth, -2);
        initView();
        initData();
        setEvent();
    }

    private void initView() {
        this.timePickerStart = (TimePicker) findViewById(R.id.timePickerStart);
        this.timePickerEnd = (TimePicker) findViewById(R.id.timePickerEnd);
        this.sbSound = (SeekBar) findViewById(R.id.seekBar_sound);
        this.cancelBtn = findViewById(R.id.cancelBtn);
        this.submitBtn = findViewById(R.id.submitBtn);
    }

    private void initData() {
        this.timePickerStart.setIs24HourView(true);
        this.timePickerEnd.setIs24HourView(true);
        this.timePickerStart.setDescendantFocusability(393216);
        this.timePickerEnd.setDescendantFocusability(393216);
        this.sbSound.setMax(100);
        this.sbSound.setProgress(this.sound);
        setTimePickerDividerColor(this.timePickerStart);
        setTimePickerDividerColor(this.timePickerEnd);
        if (!CommonUtils.isNull(this.startTime) && !CommonUtils.isNull(this.endTime)) {
            if (Build.VERSION.SDK_INT >= 23) {
                TimePicker timePicker = this.timePickerStart;
                String str = this.startTime;
                timePicker.setHour(Integer.parseInt(str.substring(0, str.indexOf(":"))));
                TimePicker timePicker2 = this.timePickerStart;
                String str2 = this.startTime;
                timePicker2.setMinute(Integer.parseInt(str2.substring(str2.indexOf(":") + 1)));
                TimePicker timePicker3 = this.timePickerEnd;
                String str3 = this.endTime;
                timePicker3.setHour(Integer.parseInt(str3.substring(0, str3.indexOf(":"))));
                TimePicker timePicker4 = this.timePickerEnd;
                String str4 = this.endTime;
                timePicker4.setMinute(Integer.parseInt(str4.substring(str4.indexOf(":") + 1)));
            } else {
                TimePicker timePicker5 = this.timePickerStart;
                String str5 = this.startTime;
                timePicker5.setCurrentHour(Integer.valueOf(Integer.parseInt(str5.substring(0, str5.indexOf(":")))));
                TimePicker timePicker6 = this.timePickerStart;
                String str6 = this.startTime;
                timePicker6.setCurrentMinute(Integer.valueOf(Integer.parseInt(str6.substring(str6.indexOf(":") + 1))));
                TimePicker timePicker7 = this.timePickerEnd;
                String str7 = this.endTime;
                timePicker7.setCurrentHour(Integer.valueOf(Integer.parseInt(str7.substring(0, str7.indexOf(":")))));
                TimePicker timePicker8 = this.timePickerEnd;
                String str8 = this.endTime;
                timePicker8.setCurrentMinute(Integer.valueOf(Integer.parseInt(str8.substring(str8.indexOf(":") + 1))));
            }
        }
        this.timePickerStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() { // from class: com.shj.setting.Dialog.TimeRangePickerDialog.1
            AnonymousClass1() {
            }

            @Override // android.widget.TimePicker.OnTimeChangedListener
            public void onTimeChanged(TimePicker timePicker9, int i, int i2) {
                StringBuilder sb;
                String str9;
                if (i < 10) {
                    sb = new StringBuilder();
                    sb.append("0");
                } else {
                    sb = new StringBuilder();
                    sb.append("");
                }
                sb.append(i);
                String sb2 = sb.toString();
                if (i2 < 10) {
                    str9 = "0" + i2;
                } else {
                    str9 = "" + i2;
                }
                TimeRangePickerDialog.this.startTime = sb2 + ":" + str9;
            }
        });
        this.timePickerEnd.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() { // from class: com.shj.setting.Dialog.TimeRangePickerDialog.2
            AnonymousClass2() {
            }

            @Override // android.widget.TimePicker.OnTimeChangedListener
            public void onTimeChanged(TimePicker timePicker9, int i, int i2) {
                StringBuilder sb;
                String str9;
                if (i < 10) {
                    sb = new StringBuilder();
                    sb.append("0");
                } else {
                    sb = new StringBuilder();
                    sb.append("");
                }
                sb.append(i);
                String sb2 = sb.toString();
                if (i2 < 10) {
                    str9 = "0" + i2;
                } else {
                    str9 = "" + i2;
                }
                TimeRangePickerDialog.this.endTime = sb2 + ":" + str9;
            }
        });
        this.sbSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.shj.setting.Dialog.TimeRangePickerDialog.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            AnonymousClass3() {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                TimeRangePickerDialog.this.sound = i;
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.TimeRangePickerDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements TimePicker.OnTimeChangedListener {
        AnonymousClass1() {
        }

        @Override // android.widget.TimePicker.OnTimeChangedListener
        public void onTimeChanged(TimePicker timePicker9, int i, int i2) {
            StringBuilder sb;
            String str9;
            if (i < 10) {
                sb = new StringBuilder();
                sb.append("0");
            } else {
                sb = new StringBuilder();
                sb.append("");
            }
            sb.append(i);
            String sb2 = sb.toString();
            if (i2 < 10) {
                str9 = "0" + i2;
            } else {
                str9 = "" + i2;
            }
            TimeRangePickerDialog.this.startTime = sb2 + ":" + str9;
        }
    }

    /* renamed from: com.shj.setting.Dialog.TimeRangePickerDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements TimePicker.OnTimeChangedListener {
        AnonymousClass2() {
        }

        @Override // android.widget.TimePicker.OnTimeChangedListener
        public void onTimeChanged(TimePicker timePicker9, int i, int i2) {
            StringBuilder sb;
            String str9;
            if (i < 10) {
                sb = new StringBuilder();
                sb.append("0");
            } else {
                sb = new StringBuilder();
                sb.append("");
            }
            sb.append(i);
            String sb2 = sb.toString();
            if (i2 < 10) {
                str9 = "0" + i2;
            } else {
                str9 = "" + i2;
            }
            TimeRangePickerDialog.this.endTime = sb2 + ":" + str9;
        }
    }

    /* renamed from: com.shj.setting.Dialog.TimeRangePickerDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements SeekBar.OnSeekBarChangeListener {
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        AnonymousClass3() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            TimeRangePickerDialog.this.sound = i;
        }
    }

    /* renamed from: com.shj.setting.Dialog.TimeRangePickerDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TimeRangePickerDialog.this.confirmAction.onLeftClick();
            TimeRangePickerDialog.this.dismiss();
        }
    }

    private void setEvent() {
        this.cancelBtn.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.TimeRangePickerDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TimeRangePickerDialog.this.confirmAction.onLeftClick();
                TimeRangePickerDialog.this.dismiss();
            }
        });
        this.submitBtn.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.TimeRangePickerDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TimeRangePickerDialog.this.confirmAction.onRightClick(TimeRangePickerDialog.this.startTime, TimeRangePickerDialog.this.endTime, TimeRangePickerDialog.this.sound);
                TimeRangePickerDialog.this.dismiss();
            }
        });
        setCanceledOnTouchOutside(true);
    }

    /* renamed from: com.shj.setting.Dialog.TimeRangePickerDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TimeRangePickerDialog.this.confirmAction.onRightClick(TimeRangePickerDialog.this.startTime, TimeRangePickerDialog.this.endTime, TimeRangePickerDialog.this.sound);
            TimeRangePickerDialog.this.dismiss();
        }
    }

    private void setTimePickerDividerColor(TimePicker timePicker) {
        LinearLayout linearLayout = (LinearLayout) ((LinearLayout) timePicker.getChildAt(0)).getChildAt(1);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof NumberPicker) {
                Field[] declaredFields = NumberPicker.class.getDeclaredFields();
                setPickerMargin((NumberPicker) linearLayout.getChildAt(i));
                int length = declaredFields.length;
                int i2 = 0;
                while (true) {
                    if (i2 < length) {
                        Field field = declaredFields[i2];
                        if (field.getName().equals("mSelectionDivider")) {
                            field.setAccessible(true);
                            try {
                                field.set(linearLayout.getChildAt(i), new ColorDrawable());
                                break;
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e2) {
                                e2.printStackTrace();
                            } catch (IllegalArgumentException e3) {
                                e3.printStackTrace();
                            }
                        } else {
                            i2++;
                        }
                    }
                }
            }
        }
    }

    private void setPickerMargin(NumberPicker numberPicker) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) numberPicker.getLayoutParams();
        layoutParams.setMargins(-getDensityValue(16.0f, this.context), 0, -getDensityValue(16.0f, this.context), 0);
        if (Build.VERSION.SDK_INT > 17) {
            layoutParams.setMarginStart(-getDensityValue(16.0f, this.context));
            layoutParams.setMarginEnd(-getDensityValue(16.0f, this.context));
        }
    }

    public static int getDensityValue(float f, Context context) {
        return (int) Math.ceil(f * context.getResources().getDisplayMetrics().density);
    }
}
