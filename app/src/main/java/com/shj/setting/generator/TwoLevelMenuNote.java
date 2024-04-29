package com.shj.setting.generator;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.oysb.utils.CommonTool;
import com.oysb.utils.Event.BaseEvent;
import com.oysb.utils.Loger;
import com.oysb.utils.activity.ActivityHelper;
import com.shj.commandV2.MenuCommandType;
import com.shj.setting.Dialog.LoadingDialog;
import com.shj.setting.Dialog.SelectEnabledDialog;
import com.shj.setting.R;
import com.shj.setting.SettingActivity;
import com.shj.setting.event.GetMenuDateEvent;
import com.shj.setting.mainSettingItem.SettingTypeName;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.TwoLevelItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class TwoLevelMenuNote extends SettingNote {
    private static final int SHOW_CABINET_COUNT = 1;
    private static final int SHOW_TWO_MENU = 0;
    private static final int TWO_LEVEL_NUM = 4;
    private static final int TWO_LEVEL_NUM_LAND = 6;
    private long clickTime;
    private int currentIndex;
    private Handler handler;
    private boolean isShowCommandItem;
    private LinearLayout ll_menu_button;
    private LinearLayout ll_spinner;
    private LoadingDialog loadingDialog;
    private List<Button> munuButtonList;
    private int selectCabinetNumber;
    private List<SettingNote> settingNoteList;
    private Spinner spinner;
    private TwoLevelItemView twoLevelItemView;

    /* loaded from: classes2.dex */
    public static class MenuTag {
        int index;
        int type;
    }

    public TwoLevelMenuNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
        this.selectCabinetNumber = -1;
        this.currentIndex = 0;
        this.handler = new Handler() { // from class: com.shj.setting.generator.TwoLevelMenuNote.2
            AnonymousClass2() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what == 0) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                    layoutParams.height = 1;
                    View view = new View(TwoLevelMenuNote.this.context);
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(TwoLevelMenuNote.this.context.getResources().getColor(R.color.color_setting_tip));
                    TwoLevelMenuNote.this.twoLevelItemView.addContentView(view);
                    int i3 = 0;
                    for (SettingNote settingNote : TwoLevelMenuNote.this.settingNoteList) {
                        View view2 = settingNote.getView(message.arg1);
                        if (settingNote.getAbsItemView() != null) {
                            settingNote.getAbsItemView().setSaveButtonVisibility(0);
                            TwoLevelMenuNote.this.twoLevelItemView.addContentView(view2);
                            if (i3 < TwoLevelMenuNote.this.settingNoteList.size() - 1) {
                                View view3 = new View(TwoLevelMenuNote.this.context);
                                view3.setLayoutParams(layoutParams);
                                view3.setBackgroundColor(TwoLevelMenuNote.this.context.getResources().getColor(R.color.color_setting_tip));
                                TwoLevelMenuNote.this.twoLevelItemView.addContentView(view3);
                            }
                            i3++;
                        }
                    }
                    TwoLevelMenuNote.this.loadingDialog.dismiss();
                    return;
                }
                if (1 == message.what) {
                    TwoLevelMenuNote.this.setSpinnerData(message.arg1);
                }
            }
        };
    }

    public void setIsShowCommandItem(boolean z) {
        this.isShowCommandItem = z;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        List<SettingNote> list = this.settingNoteList;
        if (list != null) {
            Iterator<SettingNote> it = list.iterator();
            while (it.hasNext()) {
                it.next().saveSetting(true);
            }
        }
    }

    public void nextLevelMenuButtonClick(int i, int i2) {
        this.settingNoteList = Generator.getTwoLevelNoteList(this.context, this.mainSettingType, i2, this.mUserSettingDao, this.isShowCommandItem);
        this.twoLevelItemView.clearContentView();
        LoadingDialog loadingDialog = new LoadingDialog(this.context);
        this.loadingDialog = loadingDialog;
        loadingDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.generator.TwoLevelMenuNote.1
            final /* synthetic */ int val$cabinetNumber;

            AnonymousClass1(int i3) {
                i = i3;
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                Message message = new Message();
                message.what = 0;
                message.arg1 = i;
                TwoLevelMenuNote.this.handler.sendMessage(message);
            }
        });
        this.loadingDialog.show();
    }

    /* renamed from: com.shj.setting.generator.TwoLevelMenuNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements DialogInterface.OnShowListener {
        final /* synthetic */ int val$cabinetNumber;

        AnonymousClass1(int i3) {
            i = i3;
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            Message message = new Message();
            message.what = 0;
            message.arg1 = i;
            TwoLevelMenuNote.this.handler.sendMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.generator.TwoLevelMenuNote$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends Handler {
        AnonymousClass2() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
                layoutParams.height = 1;
                View view = new View(TwoLevelMenuNote.this.context);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(TwoLevelMenuNote.this.context.getResources().getColor(R.color.color_setting_tip));
                TwoLevelMenuNote.this.twoLevelItemView.addContentView(view);
                int i3 = 0;
                for (SettingNote settingNote : TwoLevelMenuNote.this.settingNoteList) {
                    View view2 = settingNote.getView(message.arg1);
                    if (settingNote.getAbsItemView() != null) {
                        settingNote.getAbsItemView().setSaveButtonVisibility(0);
                        TwoLevelMenuNote.this.twoLevelItemView.addContentView(view2);
                        if (i3 < TwoLevelMenuNote.this.settingNoteList.size() - 1) {
                            View view3 = new View(TwoLevelMenuNote.this.context);
                            view3.setLayoutParams(layoutParams);
                            view3.setBackgroundColor(TwoLevelMenuNote.this.context.getResources().getColor(R.color.color_setting_tip));
                            TwoLevelMenuNote.this.twoLevelItemView.addContentView(view3);
                        }
                        i3++;
                    }
                }
                TwoLevelMenuNote.this.loadingDialog.dismiss();
                return;
            }
            if (1 == message.what) {
                TwoLevelMenuNote.this.setSpinnerData(message.arg1);
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        TwoLevelItemView twoLevelItemView = new TwoLevelItemView(this.context);
        this.twoLevelItemView = twoLevelItemView;
        twoLevelItemView.setEventListener(this.eventListener);
        return this.twoLevelItemView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        EventBus.getDefault().register(this);
        queryData();
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        EventBus.getDefault().unregister(this);
    }

    private void queryData() {
        Message message = new Message();
        message.what = 1;
        message.arg1 = SettingActivity.getBasicMachineInfo().cabinetNumberList.size();
        this.handler.sendMessage(message);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof GetMenuDateEvent) {
            GetMenuDateEvent getMenuDateEvent = (GetMenuDateEvent) baseEvent;
            if (getMenuDateEvent.menuCommandType == MenuCommandType.TYPE_QUERY_CARGO) {
                this.loadingDialog.dismiss();
                setSpinnerData(((Integer) getMenuDateEvent.data).intValue());
            }
        }
    }

    public View getMenuView() {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_two_level_menu, (ViewGroup) null);
        this.ll_spinner = (LinearLayout) inflate.findViewById(R.id.ll_spinner);
        this.spinner = (Spinner) inflate.findViewById(R.id.spinner);
        this.ll_menu_button = (LinearLayout) inflate.findViewById(R.id.ll_menu_button);
        return inflate;
    }

    public void setSpinnerData(int i) {
        List<Integer> settingTypeList;
        if (this.mainSettingType == 17) {
            settingTypeList = SettingActivity.searchSettingItem.gettingTypeList();
        } else {
            settingTypeList = Generator.getSettingTypeList(this.context, this.mainSettingType, this.mUserSettingDao);
        }
        this.munuButtonList = new ArrayList();
        int i2 = ActivityHelper.isLand(this.context) ? 6 : 4;
        if (i <= 1) {
            i2++;
        }
        if (!"zh".equalsIgnoreCase(CommonTool.getLanguage(this.context))) {
            i2--;
        }
        List<Integer> commandItem = SelectEnabledDialog.getCommandItem();
        ViewGroup viewGroup = null;
        LinearLayout linearLayout = null;
        int i3 = 0;
        int i4 = 0;
        while (i3 < settingTypeList.size()) {
            if ((this.isShowCommandItem || !commandItem.contains(settingTypeList.get(i3))) && AppSetting.isSettingEnabled(this.context, settingTypeList.get(i3).intValue(), this.mUserSettingDao)) {
                if (i4 % i2 == 0) {
                    linearLayout = new LinearLayout(this.context);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                    linearLayout.setOrientation(0);
                    this.ll_menu_button.addView(linearLayout);
                }
                View inflate = LayoutInflater.from(this.context).inflate(R.layout.layout_two_level_menu_item, viewGroup);
                Button button = (Button) inflate.findViewById(R.id.bt_menu);
                this.munuButtonList.add(button);
                String settingName = SettingTypeName.getSettingName(this.context, settingTypeList.get(i3).intValue());
                button.setText(settingName);
                if (settingName != null && settingName.length() > 4) {
                    button.setTextSize(this.context.getResources().getDimensionPixelSize(R.dimen.text_xsmall));
                }
                MenuTag menuTag = new MenuTag();
                menuTag.index = i4;
                menuTag.type = settingTypeList.get(i3).intValue();
                button.setTag(menuTag);
                button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.TwoLevelMenuNote.3
                    AnonymousClass3() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (Math.abs(System.currentTimeMillis() - TwoLevelMenuNote.this.clickTime) < 1500) {
                            return;
                        }
                        TwoLevelMenuNote.this.clickTime = System.currentTimeMillis();
                        Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
                        Iterator it = TwoLevelMenuNote.this.munuButtonList.iterator();
                        while (it.hasNext()) {
                            ((Button) it.next()).setBackgroundResource(R.drawable.selector_button_blue);
                        }
                        view.setBackgroundResource(R.drawable.selector_button_blue_press);
                        MenuTag menuTag2 = (MenuTag) view.getTag();
                        TwoLevelMenuNote.this.currentIndex = menuTag2.index;
                        int i5 = menuTag2.type;
                        TwoLevelMenuNote twoLevelMenuNote = TwoLevelMenuNote.this;
                        twoLevelMenuNote.nextLevelMenuButtonClick(twoLevelMenuNote.selectCabinetNumber, i5);
                    }
                });
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                layoutParams.leftMargin = 5;
                layoutParams.topMargin = 3;
                layoutParams.bottomMargin = 3;
                linearLayout.addView(inflate, layoutParams);
                i4++;
            }
            i3++;
            viewGroup = null;
        }
        if (i <= 1) {
            this.ll_spinner.setVisibility(8);
            this.selectCabinetNumber = 0;
            this.munuButtonList.get(this.currentIndex).setSoundEffectsEnabled(false);
            this.munuButtonList.get(this.currentIndex).performClick();
            this.munuButtonList.get(this.currentIndex).setSoundEffectsEnabled(true);
            return;
        }
        this.ll_spinner.setVisibility(0);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.context, R.layout.layout_spinner_item_simple_small, SettingActivity.getBasicMachineInfo().cabinetNumberList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter((SpinnerAdapter) arrayAdapter);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shj.setting.generator.TwoLevelMenuNote.4
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            AnonymousClass4() {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i5, long j) {
                TwoLevelMenuNote.this.selectCabinetNumber = SettingActivity.getBasicMachineInfo().cabinetList.get(i5).intValue();
                if (TwoLevelMenuNote.this.currentIndex >= TwoLevelMenuNote.this.munuButtonList.size()) {
                    TwoLevelMenuNote.this.currentIndex = 0;
                }
                ((Button) TwoLevelMenuNote.this.munuButtonList.get(TwoLevelMenuNote.this.currentIndex)).setSoundEffectsEnabled(false);
                ((Button) TwoLevelMenuNote.this.munuButtonList.get(TwoLevelMenuNote.this.currentIndex)).performClick();
                ((Button) TwoLevelMenuNote.this.munuButtonList.get(TwoLevelMenuNote.this.currentIndex)).setSoundEffectsEnabled(true);
            }
        });
    }

    /* renamed from: com.shj.setting.generator.TwoLevelMenuNote$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (Math.abs(System.currentTimeMillis() - TwoLevelMenuNote.this.clickTime) < 1500) {
                return;
            }
            TwoLevelMenuNote.this.clickTime = System.currentTimeMillis();
            Loger.writeLog("SET", "点击了：" + ((Button) view).getText().toString());
            Iterator it = TwoLevelMenuNote.this.munuButtonList.iterator();
            while (it.hasNext()) {
                ((Button) it.next()).setBackgroundResource(R.drawable.selector_button_blue);
            }
            view.setBackgroundResource(R.drawable.selector_button_blue_press);
            MenuTag menuTag2 = (MenuTag) view.getTag();
            TwoLevelMenuNote.this.currentIndex = menuTag2.index;
            int i5 = menuTag2.type;
            TwoLevelMenuNote twoLevelMenuNote = TwoLevelMenuNote.this;
            twoLevelMenuNote.nextLevelMenuButtonClick(twoLevelMenuNote.selectCabinetNumber, i5);
        }
    }

    /* renamed from: com.shj.setting.generator.TwoLevelMenuNote$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements AdapterView.OnItemSelectedListener {
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        AnonymousClass4() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i5, long j) {
            TwoLevelMenuNote.this.selectCabinetNumber = SettingActivity.getBasicMachineInfo().cabinetList.get(i5).intValue();
            if (TwoLevelMenuNote.this.currentIndex >= TwoLevelMenuNote.this.munuButtonList.size()) {
                TwoLevelMenuNote.this.currentIndex = 0;
            }
            ((Button) TwoLevelMenuNote.this.munuButtonList.get(TwoLevelMenuNote.this.currentIndex)).setSoundEffectsEnabled(false);
            ((Button) TwoLevelMenuNote.this.munuButtonList.get(TwoLevelMenuNote.this.currentIndex)).performClick();
            ((Button) TwoLevelMenuNote.this.munuButtonList.get(TwoLevelMenuNote.this.currentIndex)).setSoundEffectsEnabled(true);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.twoLevelItemView;
    }
}
