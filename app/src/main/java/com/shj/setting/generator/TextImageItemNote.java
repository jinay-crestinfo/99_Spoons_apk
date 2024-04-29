package com.shj.setting.generator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import com.oysb.utils.Event.BaseEvent;
import com.shj.setting.R;
import com.shj.setting.Utils.Base64Util;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.event.SelectPicEvent;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.TextImageItemView;
import com.xyshj.database.setting.AppSetting;
import com.xyshj.database.setting.SettingType;
import com.xyshj.database.setting.UserSetting;
import com.xyshj.database.setting.UserSettingDao;
import java.io.File;
import java.io.FileInputStream;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/* loaded from: classes.dex */
public class TextImageItemNote extends SettingNote {
    private TextImageItemView textImageItemView;

    public TextImageItemNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        int i = this.settingType;
        if (i == 108) {
            String settingKey = SettingType.getSettingKey(this.settingType);
            String picPath = this.textImageItemView.getPicPath();
            if (TextUtils.isEmpty(settingKey) || TextUtils.isEmpty(picPath)) {
                return;
            }
            this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey, picPath));
            if (z) {
                return;
            }
            ToastUitl.showSaveSuccessTip(this.context);
            return;
        }
        if (i != 348) {
            return;
        }
        String settingKey2 = SettingType.getSettingKey(this.settingType);
        Bitmap bitmap = this.textImageItemView.getBitmap();
        if (TextUtils.isEmpty(settingKey2) || bitmap == null) {
            return;
        }
        this.mUserSettingDao.insert(new UserSetting(this.settingType, SettingType.getParentId(this.settingType), settingKey2, Base64Util.bitmapToBase64(bitmap)));
        if (z) {
            return;
        }
        ToastUitl.showSaveSuccessTip(this.context);
    }

    private void setPic() {
        UserSetting userSettingFormKey;
        String value;
        int i = this.settingType;
        if (i != 108) {
            if (i != 348) {
                return;
            }
            String qrcodeFloatViewImage = AppSetting.getQrcodeFloatViewImage(this.context, this.mUserSettingDao);
            if (TextUtils.isEmpty(qrcodeFloatViewImage)) {
                return;
            }
            this.textImageItemView.setBitmap(Base64Util.base64ToBitmap(qrcodeFloatViewImage));
            return;
        }
        String settingKey = SettingType.getSettingKey(this.settingType);
        if (TextUtils.isEmpty(settingKey) || (userSettingFormKey = this.mUserSettingDao.getUserSettingFormKey(settingKey)) == null || (value = userSettingFormKey.getValue()) == null || !new File(value).exists()) {
            return;
        }
        this.textImageItemView.setPicPath(value);
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        TextImageItemView textImageItemView = new TextImageItemView(this.context, getSettingName());
        this.textImageItemView = textImageItemView;
        textImageItemView.setTitleVisibility(8);
        this.textImageItemView.setEventListener(this.eventListener);
        setImageClickListener();
        setPic();
        return this.textImageItemView;
    }

    private void setImageClickListener() {
        int i = this.settingType;
        if (i == 108 || i == 348) {
            this.textImageItemView.setImageClickListener(new View.OnClickListener() { // from class: com.shj.setting.generator.TextImageItemNote.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("image/*");
                    intent.addCategory("android.intent.category.OPENABLE");
                    if (TextImageItemNote.this.context instanceof Activity) {
                        ((Activity) TextImageItemNote.this.context).startActivityForResult(intent, TextImageItemNote.this.settingType);
                    }
                }
            });
        }
    }

    /* renamed from: com.shj.setting.generator.TextImageItemNote$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            intent.addCategory("android.intent.category.OPENABLE");
            if (TextImageItemNote.this.context instanceof Activity) {
                ((Activity) TextImageItemNote.this.context).startActivityForResult(intent, TextImageItemNote.this.settingType);
            }
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onAttached() {
        super.onAttached();
        EventBus.getDefault().register(this);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void onDetached() {
        super.onDetached();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof SelectPicEvent) {
            SelectPicEvent selectPicEvent = (SelectPicEvent) baseEvent;
            if (this.settingType == 348 && selectPicEvent.getPath() != null) {
                File file = new File(selectPicEvent.getPath());
                if (file.exists()) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        int available = fileInputStream.available();
                        fileInputStream.close();
                        if (available > 204800) {
                            ToastUitl.showShort(this.context, R.string.file_is_too_large);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.textImageItemView.setPicPath(selectPicEvent.getPath());
            saveSetting(false);
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.textImageItemView;
    }
}
