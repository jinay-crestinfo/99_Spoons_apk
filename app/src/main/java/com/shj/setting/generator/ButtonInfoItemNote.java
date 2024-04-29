package com.shj.setting.generator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import com.oysb.utils.date.DateUtil;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.Utils.SalesUtils;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.ButtonInfoItemView;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSettingDao;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class ButtonInfoItemNote extends SettingNote {
    private ButtonInfoItemView buttonInfoItemView;
    int mDay;
    int mMonth;
    int mYear;

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public ButtonInfoItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    private String getInitText() {
        long currentTimeMillis = System.currentTimeMillis();
        switch (this.settingType) {
            case SettingType.DAILY_SALES /* 190 */:
                return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(currentTimeMillis));
            case SettingType.MONTHLY_SALES /* 191 */:
                return new SimpleDateFormat(DateUtil.YM_DASH, Locale.getDefault()).format(new Date(currentTimeMillis));
            case 192:
                return new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date(currentTimeMillis));
            default:
                return "";
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        ButtonInfoItemView buttonInfoItemView = new ButtonInfoItemView(this.context, getSettingName(), getInitText());
        this.buttonInfoItemView = buttonInfoItemView;
        buttonInfoItemView.setTitle(getSettingName());
        this.buttonInfoItemView.setEventListener(this.eventListener);
        this.buttonInfoItemView.setAlwaysNotDisplaySaveButton();
        setListener();
        return this.buttonInfoItemView;
    }

    private void setListener() {
        switch (this.settingType) {
            case SettingType.DAILY_SALES /* 190 */:
                setDailySalesListener();
                return;
            case SettingType.MONTHLY_SALES /* 191 */:
                setMonthlySalesListener();
                return;
            case 192:
                setAnnualSalesListener();
                return;
            default:
                return;
        }
    }

    /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements ButtonInfoItemView.ClickEventListener {
        AnonymousClass1() {
        }

        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void buttonClick(View view) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            String[] split = ButtonInfoItemNote.this.buttonInfoItemView.getTextString().split("-");
            commandV2_Up_SetCommand.setSales_Day(false, Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue(), Integer.valueOf(split[2]).intValue());
            Shj.getInstance(ButtonInfoItemNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.1.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                C00611() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    EventBus.getDefault().post(new GetMenuDateEvent(67, SalesUtils.getSalesDescribe(bArr)));
                }
            });
        }

        /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$1$1 */
        /* loaded from: classes2.dex */
        class C00611 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            C00611() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                EventBus.getDefault().post(new GetMenuDateEvent(67, SalesUtils.getSalesDescribe(bArr)));
            }
        }

        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void TextClick(View view) {
            Calendar calendar = Calendar.getInstance();
            ButtonInfoItemNote.this.mYear = calendar.get(1);
            ButtonInfoItemNote.this.mMonth = calendar.get(2);
            ButtonInfoItemNote.this.mDay = calendar.get(5);
            DatePickerDialog datePickerDialog = new DatePickerDialog(ButtonInfoItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.1.2
                AnonymousClass2() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
                }
            }, ButtonInfoItemNote.this.mYear, ButtonInfoItemNote.this.mMonth, ButtonInfoItemNote.this.mDay);
            datePickerDialog.show();
            int i = Build.VERSION.SDK_INT;
            if (i < 11) {
                ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
            } else if (i > 14) {
                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
            }
        }

        /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$1$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DatePickerDialog.OnDateSetListener {
            AnonymousClass2() {
            }

            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
            }
        }
    }

    private void setDailySalesListener() {
        this.buttonInfoItemView.setClickEventListener(new ButtonInfoItemView.ClickEventListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.1
            AnonymousClass1() {
            }

            @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
            public void buttonClick(View view) {
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                String[] split = ButtonInfoItemNote.this.buttonInfoItemView.getTextString().split("-");
                commandV2_Up_SetCommand.setSales_Day(false, Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue(), Integer.valueOf(split[2]).intValue());
                Shj.getInstance(ButtonInfoItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.1.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    C00611() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        EventBus.getDefault().post(new GetMenuDateEvent(67, SalesUtils.getSalesDescribe(bArr)));
                    }
                });
            }

            /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$1$1 */
            /* loaded from: classes2.dex */
            class C00611 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                C00611() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    EventBus.getDefault().post(new GetMenuDateEvent(67, SalesUtils.getSalesDescribe(bArr)));
                }
            }

            @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
            public void TextClick(View view) {
                Calendar calendar = Calendar.getInstance();
                ButtonInfoItemNote.this.mYear = calendar.get(1);
                ButtonInfoItemNote.this.mMonth = calendar.get(2);
                ButtonInfoItemNote.this.mDay = calendar.get(5);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ButtonInfoItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.1.2
                    AnonymousClass2() {
                    }

                    @Override // android.app.DatePickerDialog.OnDateSetListener
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
                    }
                }, ButtonInfoItemNote.this.mYear, ButtonInfoItemNote.this.mMonth, ButtonInfoItemNote.this.mDay);
                datePickerDialog.show();
                int i = Build.VERSION.SDK_INT;
                if (i < 11) {
                    ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
                } else if (i > 14) {
                    ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
                }
            }

            /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$1$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements DatePickerDialog.OnDateSetListener {
                AnonymousClass2() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1), Integer.valueOf(i3)));
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements ButtonInfoItemView.ClickEventListener {
        AnonymousClass2() {
        }

        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void buttonClick(View view) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            String[] split = ButtonInfoItemNote.this.buttonInfoItemView.getTextString().split("-");
            commandV2_Up_SetCommand.setSales_Month(false, Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue());
            Shj.getInstance(ButtonInfoItemNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.2.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    EventBus.getDefault().post(new GetMenuDateEvent(68, SalesUtils.getSalesDescribe(bArr)));
                }
            });
        }

        /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                EventBus.getDefault().post(new GetMenuDateEvent(68, SalesUtils.getSalesDescribe(bArr)));
            }
        }

        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void TextClick(View view) {
            Calendar calendar = Calendar.getInstance();
            ButtonInfoItemNote.this.mYear = calendar.get(1);
            ButtonInfoItemNote.this.mMonth = calendar.get(2);
            ButtonInfoItemNote.this.mDay = calendar.get(5);
            DatePickerDialog datePickerDialog = new DatePickerDialog(ButtonInfoItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.2.2
                C00622() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1)));
                }
            }, ButtonInfoItemNote.this.mYear, ButtonInfoItemNote.this.mMonth, ButtonInfoItemNote.this.mDay);
            datePickerDialog.show();
            int i = Build.VERSION.SDK_INT;
            if (i < 11) {
                ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1).setVisibility(8);
                ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
            } else if (i > 14) {
                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(8);
                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
            }
        }

        /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$2$2 */
        /* loaded from: classes2.dex */
        class C00622 implements DatePickerDialog.OnDateSetListener {
            C00622() {
            }

            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1)));
            }
        }
    }

    private void setMonthlySalesListener() {
        this.buttonInfoItemView.setClickEventListener(new ButtonInfoItemView.ClickEventListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.2
            AnonymousClass2() {
            }

            @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
            public void buttonClick(View view) {
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                String[] split = ButtonInfoItemNote.this.buttonInfoItemView.getTextString().split("-");
                commandV2_Up_SetCommand.setSales_Month(false, Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue());
                Shj.getInstance(ButtonInfoItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.2.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    AnonymousClass1() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        EventBus.getDefault().post(new GetMenuDateEvent(68, SalesUtils.getSalesDescribe(bArr)));
                    }
                });
            }

            /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    EventBus.getDefault().post(new GetMenuDateEvent(68, SalesUtils.getSalesDescribe(bArr)));
                }
            }

            @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
            public void TextClick(View view) {
                Calendar calendar = Calendar.getInstance();
                ButtonInfoItemNote.this.mYear = calendar.get(1);
                ButtonInfoItemNote.this.mMonth = calendar.get(2);
                ButtonInfoItemNote.this.mDay = calendar.get(5);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ButtonInfoItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.2.2
                    C00622() {
                    }

                    @Override // android.app.DatePickerDialog.OnDateSetListener
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1)));
                    }
                }, ButtonInfoItemNote.this.mYear, ButtonInfoItemNote.this.mMonth, ButtonInfoItemNote.this.mDay);
                datePickerDialog.show();
                int i = Build.VERSION.SDK_INT;
                if (i < 11) {
                    ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1).setVisibility(8);
                    ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
                } else if (i > 14) {
                    ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(8);
                    ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
                }
            }

            /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$2$2 */
            /* loaded from: classes2.dex */
            class C00622 implements DatePickerDialog.OnDateSetListener {
                C00622() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d-%d", Integer.valueOf(i), Integer.valueOf(i2 + 1)));
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements ButtonInfoItemView.ClickEventListener {
        AnonymousClass3() {
        }

        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void buttonClick(View view) {
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setSales_Year(false, Integer.valueOf(ButtonInfoItemNote.this.buttonInfoItemView.getTextString()).intValue());
            Shj.getInstance(ButtonInfoItemNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.3.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    EventBus.getDefault().post(new GetMenuDateEvent(69, SalesUtils.getSalesDescribe(bArr)));
                }
            });
        }

        /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$3$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                EventBus.getDefault().post(new GetMenuDateEvent(69, SalesUtils.getSalesDescribe(bArr)));
            }
        }

        @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
        public void TextClick(View view) {
            Calendar calendar = Calendar.getInstance();
            ButtonInfoItemNote.this.mYear = calendar.get(1);
            ButtonInfoItemNote.this.mMonth = calendar.get(2);
            ButtonInfoItemNote.this.mDay = calendar.get(5);
            DatePickerDialog datePickerDialog = new DatePickerDialog(ButtonInfoItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.3.2
                AnonymousClass2() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d", Integer.valueOf(i)));
                }
            }, ButtonInfoItemNote.this.mYear, ButtonInfoItemNote.this.mMonth, ButtonInfoItemNote.this.mDay);
            datePickerDialog.show();
            int i = Build.VERSION.SDK_INT;
            if (i < 11) {
                ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1).setVisibility(8);
                ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(2).setVisibility(8);
                ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
            } else if (i > 14) {
                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(8);
                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(8);
                ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
            }
        }

        /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$3$2 */
        /* loaded from: classes2.dex */
        class AnonymousClass2 implements DatePickerDialog.OnDateSetListener {
            AnonymousClass2() {
            }

            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d", Integer.valueOf(i)));
            }
        }
    }

    private void setAnnualSalesListener() {
        this.buttonInfoItemView.setClickEventListener(new ButtonInfoItemView.ClickEventListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.3
            AnonymousClass3() {
            }

            @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
            public void buttonClick(View view) {
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.setSales_Year(false, Integer.valueOf(ButtonInfoItemNote.this.buttonInfoItemView.getTextString()).intValue());
                Shj.getInstance(ButtonInfoItemNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.3.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    AnonymousClass1() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        EventBus.getDefault().post(new GetMenuDateEvent(69, SalesUtils.getSalesDescribe(bArr)));
                    }
                });
            }

            /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$3$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    EventBus.getDefault().post(new GetMenuDateEvent(69, SalesUtils.getSalesDescribe(bArr)));
                }
            }

            @Override // com.shj.setting.widget.ButtonInfoItemView.ClickEventListener
            public void TextClick(View view) {
                Calendar calendar = Calendar.getInstance();
                ButtonInfoItemNote.this.mYear = calendar.get(1);
                ButtonInfoItemNote.this.mMonth = calendar.get(2);
                ButtonInfoItemNote.this.mDay = calendar.get(5);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ButtonInfoItemNote.this.context, new DatePickerDialog.OnDateSetListener() { // from class: com.shj.setting.generator.ButtonInfoItemNote.3.2
                    AnonymousClass2() {
                    }

                    @Override // android.app.DatePickerDialog.OnDateSetListener
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d", Integer.valueOf(i)));
                    }
                }, ButtonInfoItemNote.this.mYear, ButtonInfoItemNote.this.mMonth, ButtonInfoItemNote.this.mDay);
                datePickerDialog.show();
                int i = Build.VERSION.SDK_INT;
                if (i < 11) {
                    ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1).setVisibility(8);
                    ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(2).setVisibility(8);
                    ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(1)).setVisibility(8);
                } else if (i > 14) {
                    ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(8);
                    ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(8);
                    ((ViewGroup) ((ViewGroup) datePickerDialog.getDatePicker().getChildAt(0)).getChildAt(1)).setVisibility(8);
                }
            }

            /* renamed from: com.shj.setting.generator.ButtonInfoItemNote$3$2 */
            /* loaded from: classes2.dex */
            class AnonymousClass2 implements DatePickerDialog.OnDateSetListener {
                AnonymousClass2() {
                }

                @Override // android.app.DatePickerDialog.OnDateSetListener
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    ButtonInfoItemNote.this.buttonInfoItemView.setTextString(String.format("%d", Integer.valueOf(i)));
                }
            }
        });
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.buttonInfoItemView;
    }
}
