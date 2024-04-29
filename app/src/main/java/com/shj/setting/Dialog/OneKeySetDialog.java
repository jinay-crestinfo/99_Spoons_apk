package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.shj.OnCommandAnswerListener;
import com.shj.OnShjGoodsSetResultListener;
import com.shj.ShelfInfo;
import com.shj.Shj;
import com.shj.ShjGoodsSetting;
import com.shj.biz.ReportManager;
import com.shj.biz.ShjManager;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.UpdataGoodsInfoUIEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

/* loaded from: classes2.dex */
public class OneKeySetDialog extends Dialog {
    private Button bt_ok;
    private Context context;
    private List<Integer> jghList;
    private List<Button> layerButtonList;
    private LinearLayout ll_top;

    public OneKeySetDialog(Context context, List<Integer> list) {
        super(context, R.style.loading_style);
        this.layerButtonList = new ArrayList();
        if (list.size() > 1) {
            list.add(0, 99);
        }
        this.jghList = list;
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_onekeyset_dialog);
        this.ll_top = (LinearLayout) findViewById(R.id.ll_top);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        setCanceledOnTouchOutside(false);
        setListener();
        addLayerButton();
    }

    private void addLayerButton() {
        int dimensionPixelOffset = this.context.getResources().getDimensionPixelOffset(R.dimen.x130);
        int dimensionPixelOffset2 = this.context.getResources().getDimensionPixelOffset(R.dimen.y72);
        int color = this.context.getResources().getColor(R.color.setting_white);
        int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = 10;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = 10;
        int size = this.jghList.size();
        int size2 = size % 5 != 0 ? (this.jghList.size() / ((size / 5) + 1)) + 1 : 5;
        LinearLayout linearLayout = null;
        for (int i = 0; i < size; i++) {
            if (i % size2 == 0) {
                linearLayout = new LinearLayout(this.context);
                this.ll_top.addView(linearLayout, layoutParams);
            }
            Button button = new Button(this.context);
            button.setText(getJghName(this.jghList.get(i).intValue()));
            button.setTag(this.jghList.get(i));
            button.setWidth(dimensionPixelOffset);
            button.setHeight(dimensionPixelOffset2);
            button.setBackgroundResource(R.drawable.selector_button_blue);
            button.setTextColor(color);
            button.setTextSize(dimensionPixelSize);
            button.setGravity(17);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.OneKeySetDialog.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    LoadingDialog loadingDialog = new LoadingDialog(OneKeySetDialog.this.context, OneKeySetDialog.this.context.getString(R.string.on_setting));
                    loadingDialog.show();
                    int intValue = ((Integer) view.getTag()).intValue();
                    if (Shj.isDebug()) {
                        try {
                            if (intValue == 99) {
                                for (Integer num : Shj.getShelves()) {
                                    Shj.onUpdateGoodsCount(num, Shj.getShelfInfo(num).getCapacity());
                                }
                            } else {
                                for (Integer num2 : Shj.getMachine(intValue, false).getShelves()) {
                                    Shj.onUpdateGoodsCount(num2, Shj.getShelfInfo(num2).getCapacity());
                                }
                            }
                        } catch (Exception unused) {
                        }
                        loadingDialog.dismiss();
                        return;
                    }
                    CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
                    commandV2_Up_SetCommand.oneKeySetFullGoodsByJgh(intValue);
                    Shj.getInstance(OneKeySetDialog.this.context);
                    Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.OneKeySetDialog.1.1
                        final /* synthetic */ int val$jgh;
                        final /* synthetic */ LoadingDialog val$loadingDialog;

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandReadAnswer(byte[] bArr) {
                        }

                        C00571(int intValue2, LoadingDialog loadingDialog2) {
                            intValue = intValue2;
                            loadingDialog = loadingDialog2;
                        }

                        @Override // com.shj.OnCommandAnswerListener
                        public void onCommandSetAnswer(boolean z) {
                            if (z) {
                                ToastUitl.showLong(OneKeySetDialog.this.context, R.string.setting_success);
                                EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                            } else {
                                OneKeySetDialog.this.fullLoadGoods(intValue);
                            }
                            loadingDialog.dismiss();
                        }
                    });
                }

                /* renamed from: com.shj.setting.Dialog.OneKeySetDialog$1$1 */
                /* loaded from: classes2.dex */
                class C00571 implements OnCommandAnswerListener {
                    final /* synthetic */ int val$jgh;
                    final /* synthetic */ LoadingDialog val$loadingDialog;

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandReadAnswer(byte[] bArr) {
                    }

                    C00571(int intValue2, LoadingDialog loadingDialog2) {
                        intValue = intValue2;
                        loadingDialog = loadingDialog2;
                    }

                    @Override // com.shj.OnCommandAnswerListener
                    public void onCommandSetAnswer(boolean z) {
                        if (z) {
                            ToastUitl.showLong(OneKeySetDialog.this.context, R.string.setting_success);
                            EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                        } else {
                            OneKeySetDialog.this.fullLoadGoods(intValue);
                        }
                        loadingDialog.dismiss();
                    }
                }
            });
            this.layerButtonList.add(button);
            linearLayout.addView(button, layoutParams2);
        }
    }

    /* renamed from: com.shj.setting.Dialog.OneKeySetDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LoadingDialog loadingDialog2 = new LoadingDialog(OneKeySetDialog.this.context, OneKeySetDialog.this.context.getString(R.string.on_setting));
            loadingDialog2.show();
            int intValue2 = ((Integer) view.getTag()).intValue();
            if (Shj.isDebug()) {
                try {
                    if (intValue2 == 99) {
                        for (Integer num : Shj.getShelves()) {
                            Shj.onUpdateGoodsCount(num, Shj.getShelfInfo(num).getCapacity());
                        }
                    } else {
                        for (Integer num2 : Shj.getMachine(intValue2, false).getShelves()) {
                            Shj.onUpdateGoodsCount(num2, Shj.getShelfInfo(num2).getCapacity());
                        }
                    }
                } catch (Exception unused) {
                }
                loadingDialog2.dismiss();
                return;
            }
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.oneKeySetFullGoodsByJgh(intValue2);
            Shj.getInstance(OneKeySetDialog.this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.OneKeySetDialog.1.1
                final /* synthetic */ int val$jgh;
                final /* synthetic */ LoadingDialog val$loadingDialog;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                C00571(int intValue22, LoadingDialog loadingDialog22) {
                    intValue = intValue22;
                    loadingDialog = loadingDialog22;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    if (z) {
                        ToastUitl.showLong(OneKeySetDialog.this.context, R.string.setting_success);
                        EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                    } else {
                        OneKeySetDialog.this.fullLoadGoods(intValue);
                    }
                    loadingDialog.dismiss();
                }
            });
        }

        /* renamed from: com.shj.setting.Dialog.OneKeySetDialog$1$1 */
        /* loaded from: classes2.dex */
        class C00571 implements OnCommandAnswerListener {
            final /* synthetic */ int val$jgh;
            final /* synthetic */ LoadingDialog val$loadingDialog;

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            C00571(int intValue22, LoadingDialog loadingDialog22) {
                intValue = intValue22;
                loadingDialog = loadingDialog22;
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z) {
                if (z) {
                    ToastUitl.showLong(OneKeySetDialog.this.context, R.string.setting_success);
                    EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                } else {
                    OneKeySetDialog.this.fullLoadGoods(intValue);
                }
                loadingDialog.dismiss();
            }
        }
    }

    private String getJghName(int i) {
        if (i == 99) {
            return this.context.getResources().getString(R.string.all_machine);
        }
        if (i == 0) {
            return this.context.getResources().getString(R.string.host);
        }
        return this.context.getResources().getString(R.string.auxiliary_engine) + i;
    }

    /* renamed from: com.shj.setting.Dialog.OneKeySetDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            OneKeySetDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.OneKeySetDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OneKeySetDialog.this.dismiss();
            }
        });
    }

    public void fullLoadGoods(int i) {
        if (i == 99) {
            Context context = this.context;
            LoadingDialog loadingDialog = new LoadingDialog(context, context.getString(R.string.on_setting));
            loadingDialog.show();
            ReportManager.reportSetFull();
            CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
            commandV2_Up_SetCommand.setOneKeyFullGoods();
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.OneKeySetDialog.3
                final /* synthetic */ LoadingDialog val$loadingDialog;

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                }

                AnonymousClass3(LoadingDialog loadingDialog2) {
                    loadingDialog = loadingDialog2;
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                    if (!z) {
                        ToastUitl.showShort(OneKeySetDialog.this.context, R.string.error);
                    } else {
                        Shj.onFullGoodsCount();
                        EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                        ToastUitl.showShort(OneKeySetDialog.this.context, R.string.success);
                    }
                    loadingDialog.dismiss();
                }
            });
            return;
        }
        setShelfGoodsCount(SettingActivity.getBasicMachineInfo().shelvesMap.get(Integer.valueOf(i)));
    }

    /* renamed from: com.shj.setting.Dialog.OneKeySetDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements OnCommandAnswerListener {
        final /* synthetic */ LoadingDialog val$loadingDialog;

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass3(LoadingDialog loadingDialog2) {
            loadingDialog = loadingDialog2;
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
            if (!z) {
                ToastUitl.showShort(OneKeySetDialog.this.context, R.string.error);
            } else {
                Shj.onFullGoodsCount();
                EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                ToastUitl.showShort(OneKeySetDialog.this.context, R.string.success);
            }
            loadingDialog.dismiss();
        }
    }

    private void setShelfGoodsCount(List<Integer> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        MutilTextTipDialog mutilTextTipDialog = new MutilTextTipDialog(this.context);
        mutilTextTipDialog.show();
        ShjGoodsSetting shjGoodsSetting = new ShjGoodsSetting();
        shjGoodsSetting.count = list.size();
        shjGoodsSetting.onShjGoodsSetResultListener = new OnShjGoodsSetResultListener() { // from class: com.shj.setting.Dialog.OneKeySetDialog.4
            final /* synthetic */ MutilTextTipDialog val$mutilTextTipDialog;

            AnonymousClass4(MutilTextTipDialog mutilTextTipDialog2) {
                mutilTextTipDialog = mutilTextTipDialog2;
            }

            @Override // com.shj.OnShjGoodsSetResultListener
            public void result(int i, Object obj, boolean z) {
                String string = OneKeySetDialog.this.context.getString(R.string.lab_shelf);
                String string2 = OneKeySetDialog.this.context.getString(R.string.lab_stock);
                mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i)) + "  " + string2 + ":" + ((Integer) obj).intValue());
                if (z) {
                    EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                    mutilTextTipDialog.addTextShow(OneKeySetDialog.this.context.getString(R.string.setting_success));
                }
            }
        };
        Shj.addGoodsSetCommand(19, shjGoodsSetting);
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            ShelfInfo shelfInfo = Shj.getShelfInfo(Integer.valueOf(intValue));
            if (shelfInfo != null) {
                ShjManager.setShelfGoodsCount(intValue, shelfInfo.getCapacity().intValue());
            } else {
                shjGoodsSetting.count--;
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.OneKeySetDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements OnShjGoodsSetResultListener {
        final /* synthetic */ MutilTextTipDialog val$mutilTextTipDialog;

        AnonymousClass4(MutilTextTipDialog mutilTextTipDialog2) {
            mutilTextTipDialog = mutilTextTipDialog2;
        }

        @Override // com.shj.OnShjGoodsSetResultListener
        public void result(int i, Object obj, boolean z) {
            String string = OneKeySetDialog.this.context.getString(R.string.lab_shelf);
            String string2 = OneKeySetDialog.this.context.getString(R.string.lab_stock);
            mutilTextTipDialog.addTextShow(string + ":" + String.format("%03d", Integer.valueOf(i)) + "  " + string2 + ":" + ((Integer) obj).intValue());
            if (z) {
                EventBus.getDefault().post(new UpdataGoodsInfoUIEvent());
                mutilTextTipDialog.addTextShow(OneKeySetDialog.this.context.getString(R.string.setting_success));
            }
        }
    }
}
