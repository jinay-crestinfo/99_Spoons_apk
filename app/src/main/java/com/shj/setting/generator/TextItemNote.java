package com.shj.setting.generator;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import com.oysb.utils.CommonTool;
import com.shj.setting.R;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.TextItemView;
import com.xyshj.database.setting.UserSettingDao;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/* loaded from: classes2.dex */
public class TextItemNote extends SettingNote {
    private TextItemView textItemView;

    public TextItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        if (this.settingType == 165) {
            String charSequence = this.textItemView.getTextValue().getText().toString();
            Intent intent = new Intent("android.intent.action.web");
            intent.putExtra(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL, charSequence);
            this.context.startActivity(intent);
        }
    }

    private void setText() {
        if (this.settingType != 165) {
            return;
        }
        String str = !"zh".equalsIgnoreCase(CommonTool.getLanguage(this.context)) ? "https://www.google.cn/" : "https://www.baidu.com/";
        TextView textValue = this.textItemView.getTextValue();
        textValue.setText(str);
        textValue.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.TextItemNote.1
            final /* synthetic */ String val$pageUrl;

            AnonymousClass1(String str2) {
                str = str2;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.web");
                intent.putExtra(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL, str);
                TextItemNote.this.context.startActivity(intent);
            }
        });
    }

    /* renamed from: com.shj.setting.generator.TextItemNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        final /* synthetic */ String val$pageUrl;

        AnonymousClass1(String str2) {
            str = str2;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Intent intent = new Intent("android.intent.action.web");
            intent.putExtra(IjkMediaPlayer.OnNativeInvokeListener.ARG_URL, str);
            TextItemNote.this.context.startActivity(intent);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        TextItemView.TextItemData textItemData = new TextItemView.TextItemData();
        textItemData.name = getSettingName();
        textItemData.value = "";
        TextItemView textItemView = new TextItemView(this.context, textItemData);
        this.textItemView = textItemView;
        textItemView.setEventListener(this.eventListener);
        if (this.settingType != 165) {
            this.textItemView.setAlwaysNotDisplaySaveButton();
        }
        if (this.settingType == 165) {
            this.textItemView.setSaveButtonVisibility(0);
            this.textItemView.setSaveSettingText(this.context.getResources().getString(R.string.exec_test));
        }
        setText();
        return this.textItemView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.textItemView;
    }
}
