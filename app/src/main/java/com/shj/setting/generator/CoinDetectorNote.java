package com.shj.setting.generator;

import android.content.Context;
import android.view.View;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import com.shj.setting.widget.AbsItemView;
import com.shj.setting.widget.CoinDetectorView;
import com.xyshj.database.setting.UserSettingDao;

/* loaded from: classes2.dex */
public class CoinDetectorNote extends SettingNote {
    private CoinDetectorView coinDetectorView;

    public CoinDetectorNote(Context context, int i, int i2, UserSettingDao userSettingDao) {
        super(context, i, i2, userSettingDao);
    }

    @Override // com.shj.setting.generator.SettingNote
    public void saveSetting(boolean z) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        int coinChangerType = this.coinDetectorView.getCoinChangerType();
        int coinCount = this.coinDetectorView.getCoinCount();
        if (coinCount == -1) {
            ToastUitl.showShort(this.context, R.string.not_input_tip);
            return;
        }
        int coinType = this.coinDetectorView.getCoinType();
        if (coinChangerType == 1) {
            coinType = 0;
        }
        commandV2_Up_SetCommand.TestChargeCoin(true, coinChangerType, coinCount, coinType);
        Shj.getInstance(this.context);
        Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.generator.CoinDetectorNote.1
            @Override // com.shj.OnCommandAnswerListener
            public void onCommandReadAnswer(byte[] bArr) {
            }

            AnonymousClass1() {
            }

            @Override // com.shj.OnCommandAnswerListener
            public void onCommandSetAnswer(boolean z2) {
                ToastUitl.showCompeleteTip(CoinDetectorNote.this.context, z2, CoinDetectorNote.this.getSettingName());
            }
        });
    }

    /* renamed from: com.shj.setting.generator.CoinDetectorNote$1 */
    /* loaded from: classes2.dex */
    class AnonymousClass1 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
        }

        AnonymousClass1() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z2) {
            ToastUitl.showCompeleteTip(CoinDetectorNote.this.context, z2, CoinDetectorNote.this.getSettingName());
        }
    }

    @Override // com.shj.setting.generator.SettingNote
    public View getView(int i) {
        CoinDetectorView coinDetectorView = new CoinDetectorView(this.context);
        this.coinDetectorView = coinDetectorView;
        coinDetectorView.setEventListener(this.eventListener);
        this.coinDetectorView.setSaveButtonVisibility(0);
        this.coinDetectorView.setSaveSettingText(this.context.getString(R.string.coin_detector));
        return this.coinDetectorView;
    }

    @Override // com.shj.setting.generator.SettingNote
    public AbsItemView getAbsItemView() {
        return this.coinDetectorView;
    }
}
