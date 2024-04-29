package com.shj.setting.generator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.SystemTimeItemView;
import com.xyshj.database.setting.UserSettingDao;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes2.dex */
public class SystemTimeItemNote extends SettingNote {
    private LoadingDialog loadingDialog;
    int mDay;
    int mMonth;
    int mYear;
    private SystemTimeItemView systemTimeItemView;

    public SystemTimeItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        String date = this.systemTimeItemView.getDate();
        String time = this.systemTimeItemView.getTime();
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            return;
        }
        String[] split = date.split("-");
        String[] split2 = time.split(":");
        if (split.length == 3 && split2.length == 2) {
            int intValue = Integer.valueOf(split[0]).intValue();
            int intValue2 = Integer.valueOf(split[1]).intValue();
            int intValue3 = Integer.valueOf(split[2]).intValue();
            int intValue4 = Integer.valueOf(split2[0]).intValue();
            int intValue5 = Integer.valueOf(split2[1]).intValue();
            LoadingDialog loadingDialog = new LoadingDialog(this.context, R.string.saveing);
            this.loadingDialog = loadingDialog;
            loadingDialog.show();
            Date date2 = new Date();
            date2.setYear(intValue);
            date2.setMonth(intValue2 - 1);
            date2.setDate(intValue3);
            date2.setHours(intValue4);
            date2.setMinutes(intValue5);
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setTime(true, date2);
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SystemTimeItemNote.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z2) {
                    ToastUitl.showSetCompeleteTip(SystemTimeItemNote.this.context, z2, SystemTimeItemNote.this.getSettingName());
                    SystemTimeItemNote.this.loadingDialog.dismiss();
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.SystemTimeItemNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showSetCompeleteTip(SystemTimeItemNote.this.context, z2, SystemTimeItemNote.this.getSettingName());
            SystemTimeItemNote.this.loadingDialog.dismiss();
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        super.onAttached();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void querySettingData() {
        super.querySettingData();
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        commandV2_Up_SetCommand.setTime(false, null);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.SystemTimeItemNote.2
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass2() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr != null && bArr.length >= 7) {
                    int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                    int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                    int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 2, 1);
                    int intFromBytes4 = ObjectHelper.intFromBytes(bArr, 3, 1);
                    int intFromBytes5 = ObjectHelper.intFromBytes(bArr, 4, 1);
                    int intFromBytes6 = ObjectHelper.intFromBytes(bArr, 5, 1);
                    ObjectHelper.intFromBytes(bArr, 6, 1);
                    String format = String.format("%d-%02d-%02d", Integer.valueOf((intFromBytes * 100) + intFromBytes2), Integer.valueOf(intFromBytes3), Integer.valueOf(intFromBytes4));
                    Log.i("querySettingData", "date=" + format);
                    SystemTimeItemNote.this.systemTimeItemView.setDate(format);
                    SystemTimeItemNote.this.systemTimeItemView.setTime(String.format("%02d:%02d", Integer.valueOf(intFromBytes5), Integer.valueOf(intFromBytes6)));
                    return;
                }
                ToastUitl.showShort(SystemTimeItemNote.this.context, R.string.qurey_fail);
            }
        });
    }

    /* renamed from: com.shj.setting.generator.SystemTimeItemNote$2 */
    /* loaded from: classes2.dex */
    class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr != null && bArr.length >= 7) {
                int intFromBytes = ObjectHelper.intFromBytes(bArr, 0, 1);
                int intFromBytes2 = ObjectHelper.intFromBytes(bArr, 1, 1);
                int intFromBytes3 = ObjectHelper.intFromBytes(bArr, 2, 1);
                int intFromBytes4 = ObjectHelper.intFromBytes(bArr, 3, 1);
                int intFromBytes5 = ObjectHelper.intFromBytes(bArr, 4, 1);
                int intFromBytes6 = ObjectHelper.intFromBytes(bArr, 5, 1);
                ObjectHelper.intFromBytes(bArr, 6, 1);
                String format = String.format("%d-%02d-%02d", Integer.valueOf((intFromBytes * 100) + intFromBytes2), Integer.valueOf(intFromBytes3), Integer.valueOf(intFromBytes4));
                Log.i("querySettingData", "date=" + format);
                SystemTimeItemNote.this.systemTimeItemView.setDate(format);
                SystemTimeItemNote.this.systemTimeItemView.setTime(String.format("%02d:%02d", Integer.valueOf(intFromBytes5), Integer.valueOf(intFromBytes6)));
                return;
            }
            ToastUitl.showShort(SystemTimeItemNote.this.context, R.string.qurey_fail);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        SystemTimeItemView systemTimeItemView = new SystemTimeItemView(this.context);
        this.systemTimeItemView = systemTimeItemView;
        systemTimeItemView.setTitle(getSettingName());
        this.systemTimeItemView.setTitleVisibility(0);
        this.systemTimeItemView.setEventListener(this.eventListener);
        this.systemTimeItemView.setQueryButtonVIsibility(0);
        setListener();
        return this.systemTimeItemView;
    }

    private void setListener() {
        setDailySalesListener();
    }

    /* renamed from: com.shj.setting.generator.SystemTimeItemNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements SystemTimeItemView.ClickEventListener {
        AnonymousClass3() {
        }

        @Override // com.shj.setting.widget.SystemTimeItemView.ClickEventListener
        public void timeClick(View view) {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(SystemTimeItemNote.this.context, new TimePickerDialog.OnTimeSetListener() { // from class: com.shj.setting.generator.SystemTimeItemNote.3.1
                AnonymousClass1() {
                }

                @Override // android.app.TimePickerDialog.OnTimeSetListener
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    SystemTimeItemNote.this.systemTimeItemView.setTime(String.format("%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
                }
            }, calendar.get(11), calendar.get(12), true).show();
        }

        /* renamed from: com.shj.setting.generator.SystemTimeItemNote$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements TimePickerDialog.OnTimeSetListener {
            AnonymousClass1() {
            }

            @Override // android.app.TimePickerDialog.OnTimeSetListener
            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                SystemTimeItemNote.this.systemTimeItemView.setTime(String.format("%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
            }
        }

        @Override // com.shj.setting.widget.SystemTimeItemView.ClickEventListener
        public void dateClick(View view) {
            Calendar calendar = Calendar.getInstance();
            SystemTimeItemNote.this.mYear = calendar.get(1);
            SystemTimeItemNote.this.mMonth = calendar.get(2);
            SystemTimeItemNote.this.mDay = calendar.get(5);
            DatePickerDialog datePickerDialog = new DatePickerDialog(SystemTimeItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.SystemTimeItemNote.3.2
                AnonymousClass2() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    SystemTimeItemNote.this.systemTimeItemView.setDate(String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
                }
            }, SystemTimeItemNote.this.mYear, SystemTimeItemNote.this.mMonth, SystemTimeItemNote.this.mDay);
            datePickerDialog.show();
            int i = Build.VERSION.SDK_INT;
            if (i < 11) {
                ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
            } else if (i > 14) {
                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
            }
        }

        /* renamed from: com.shj.setting.generator.SystemTimeItemNote$3$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DatePickerDialog.OnDateSetListener {
            AnonymousClass2() {
            }

            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                SystemTimeItemNote.this.systemTimeItemView.setDate(String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
            }
        }
    }

    private void setDailySalesListener() {
        this.systemTimeItemView.setClickEventListener(new SystemTimeItemView.ClickEventListener() { // from class: com.shj.setting.generator.SystemTimeItemNote.3
            AnonymousClass3() {
            }

            @Override // com.shj.setting.widget.SystemTimeItemView.ClickEventListener
            public void timeClick(View view) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(SystemTimeItemNote.this.context, new TimePickerDialog.OnTimeSetListener() { // from class: com.shj.setting.generator.SystemTimeItemNote.3.1
                    AnonymousClass1() {
                    }

                    @Override // android.app.TimePickerDialog.OnTimeSetListener
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        SystemTimeItemNote.this.systemTimeItemView.setTime(String.format("%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
                    }
                }, calendar.get(11), calendar.get(12), true).show();
            }

            /* renamed from: com.shj.setting.generator.SystemTimeItemNote$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements TimePickerDialog.OnTimeSetListener {
                AnonymousClass1() {
                }

                @Override // android.app.TimePickerDialog.OnTimeSetListener
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    SystemTimeItemNote.this.systemTimeItemView.setTime(String.format("%02d:%02d", Integer.valueOf(i), Integer.valueOf(i2)));
                }
            }

            @Override // com.shj.setting.widget.SystemTimeItemView.ClickEventListener
            public void dateClick(View view) {
                Calendar calendar = Calendar.getInstance();
                SystemTimeItemNote.this.mYear = calendar.get(1);
                SystemTimeItemNote.this.mMonth = calendar.get(2);
                SystemTimeItemNote.this.mDay = calendar.get(5);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SystemTimeItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.SystemTimeItemNote.3.2
                    AnonymousClass2() {
                    }

                    @Override // android.app.DatePickerDialog.OnDateSetListener
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        SystemTimeItemNote.this.systemTimeItemView.setDate(String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
                    }
                }, SystemTimeItemNote.this.mYear, SystemTimeItemNote.this.mMonth, SystemTimeItemNote.this.mDay);
                datePickerDialog.show();
                int i = Build.VERSION.SDK_INT;
                if (i < 11) {
                    ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
                } else if (i > 14) {
                    ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
                }
            }

            /* renamed from: com.shj.setting.generator.SystemTimeItemNote$3$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements DatePickerDialog.OnDateSetListener {
                AnonymousClass2() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    SystemTimeItemNote.this.systemTimeItemView.setDate(String.format("%d-%02d-%02d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
                }
            }
        });
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.systemTimeItemView;
    }
}
