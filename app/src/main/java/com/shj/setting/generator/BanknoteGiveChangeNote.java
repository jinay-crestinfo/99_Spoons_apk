package com.shj.setting.generator;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.oysb.utils.Loger;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.BanknoteGiveChangeView;
import com.shj.setting.widget.ButtonInputInfoItemView;
import com.shj.setting.widget.TextItemView;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class BanknoteGiveChangeNote extends SettingNote {
    private BanknoteGiveChangeView banknoteGiveChangeView;
    private int changeCount;

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
    }

    public BanknoteGiveChangeNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        TextItemView.TextItemData textItemData = new TextItemView.TextItemData();
        textItemData.name = this.context.getString(R.string.banknote_give_change_tip);
        ButtonInputInfoItemView.ButtonInputInfoData buttonInputInfoData = new ButtonInputInfoItemView.ButtonInputInfoData();
        buttonInputInfoData.name = this.context.getString(R.string.banknote_give_change);
        buttonInputInfoData.tipInfo = this.context.getString(R.string.banknote_give_change_input_tip);
        BanknoteGiveChangeView banknoteGiveChangeView = new BanknoteGiveChangeView(this.context, textItemData, buttonInputInfoData);
        this.banknoteGiveChangeView = banknoteGiveChangeView;
        banknoteGiveChangeView.setTitleVisibility(8);
        this.banknoteGiveChangeView.setEventListener(this.eventListener);
        this.banknoteGiveChangeView.setEditeTypeInterger();
        setListenr();
        return this.banknoteGiveChangeView;
    }

    /* renamed from: com.shj.setting.generator.BanknoteGiveChangeNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            String inputString = BanknoteGiveChangeNote.this.banknoteGiveChangeView.getInputString();
            if (TextUtils.isEmpty(inputString)) {
                ToastUitl.showNotInputTip(BanknoteGiveChangeNote.this.context);
                return;
            }
            int intValue = Integer.valueOf(inputString).intValue();
            if (intValue <= 0 || intValue > BanknoteGiveChangeNote.this.changeCount) {
                ToastUitl.showNotInputTip(BanknoteGiveChangeNote.this.context);
                return;
            }
            commandV2_Up_SetCommand.changePaperMoney(true, intValue);
            Shj.getInstance(BanknoteGiveChangeNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.BanknoteGiveChangeNote.1.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                C00601() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    ToastUitl.showCompeleteTip(BanknoteGiveChangeNote.this.context, z, BanknoteGiveChangeNote.this.getSettingName());
                }
            });
        }

        /* renamed from: com.shj.setting.generator.BanknoteGiveChangeNote$1$1 */
        /* loaded from: classes2.dex */
        class C00601 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            C00601() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                ToastUitl.showCompeleteTip(BanknoteGiveChangeNote.this.context, z, BanknoteGiveChangeNote.this.getSettingName());
            }
        }
    }

    private void setListenr() {
        this.banknoteGiveChangeView.setButtonOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.BanknoteGiveChangeNote.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                String inputString = BanknoteGiveChangeNote.this.banknoteGiveChangeView.getInputString();
                if (TextUtils.isEmpty(inputString)) {
                    ToastUitl.showNotInputTip(BanknoteGiveChangeNote.this.context);
                    return;
                }
                int intValue = Integer.valueOf(inputString).intValue();
                if (intValue <= 0 || intValue > BanknoteGiveChangeNote.this.changeCount) {
                    ToastUitl.showNotInputTip(BanknoteGiveChangeNote.this.context);
                    return;
                }
                commandV2_Up_SetCommand.changePaperMoney(true, intValue);
                Shj.getInstance(BanknoteGiveChangeNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.BanknoteGiveChangeNote.1.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    C00601() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                        ToastUitl.showCompeleteTip(BanknoteGiveChangeNote.this.context, z, BanknoteGiveChangeNote.this.getSettingName());
                    }
                });
            }

            /* renamed from: com.shj.setting.generator.BanknoteGiveChangeNote$1$1 */
            /* loaded from: classes2.dex */
            class C00601 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                C00601() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    ToastUitl.showCompeleteTip(BanknoteGiveChangeNote.this.context, z, BanknoteGiveChangeNote.this.getSettingName());
                }
            }
        });
        this.banknoteGiveChangeView.setQueryItemOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.BanknoteGiveChangeNote.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                commandV2_Up_SetCommand.changePaperMoney(false, 0);
                Shj.getInstance(BanknoteGiveChangeNote.this.context);
                Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.BanknoteGiveChangeNote.2.1
                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                    }

                    AnonymousClass1() {
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                        if (bArr == null || bArr.length <= 0) {
                            return;
                        }
                        BanknoteGiveChangeNote.this.changeCount = ObjectHelper.intFromBytes(bArr, 0, 1);
                        BanknoteGiveChangeNote.this.banknoteGiveChangeView.setTextItemText(String.valueOf(BanknoteGiveChangeNote.this.changeCount));
                    }
                });
            }

            /* renamed from: com.shj.setting.generator.BanknoteGiveChangeNote$2$1 */
            /* loaded from: classes2.dex */
            class AnonymousClass1 implements OnCommandAnswerListener {
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr == null || bArr.length <= 0) {
                        return;
                    }
                    BanknoteGiveChangeNote.this.changeCount = ObjectHelper.intFromBytes(bArr, 0, 1);
                    BanknoteGiveChangeNote.this.banknoteGiveChangeView.setTextItemText(String.valueOf(BanknoteGiveChangeNote.this.changeCount));
                }
            }
        });
    }

    /* renamed from: com.shj.setting.generator.BanknoteGiveChangeNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.changePaperMoney(false, 0);
            Shj.getInstance(BanknoteGiveChangeNote.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.BanknoteGiveChangeNote.2.1
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass1() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr == null || bArr.length <= 0) {
                        return;
                    }
                    BanknoteGiveChangeNote.this.changeCount = ObjectHelper.intFromBytes(bArr, 0, 1);
                    BanknoteGiveChangeNote.this.banknoteGiveChangeView.setTextItemText(String.valueOf(BanknoteGiveChangeNote.this.changeCount));
                }
            });
        }

        /* renamed from: com.shj.setting.generator.BanknoteGiveChangeNote$2$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements OnCommandAnswerListener {
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
                if (bArr == null || bArr.length <= 0) {
                    return;
                }
                BanknoteGiveChangeNote.this.changeCount = ObjectHelper.intFromBytes(bArr, 0, 1);
                BanknoteGiveChangeNote.this.banknoteGiveChangeView.setTextItemText(String.valueOf(BanknoteGiveChangeNote.this.changeCount));
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.banknoteGiveChangeView;
    }
}
