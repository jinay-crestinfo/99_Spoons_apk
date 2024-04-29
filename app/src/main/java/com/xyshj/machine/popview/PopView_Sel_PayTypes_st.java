package com.xyshj.machine.popview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.oysb.utils.Loger;
import com.oysb.utils.view.BasePopView;
import com.shj.biz.ShjManager;
import com.shj.biz.order.OrderPayType;
import com.xyshj.app.ShjAppBase;
import com.xyshj.machine.R;
import java.util.List;

/* loaded from: classes2.dex */
public class PopView_Sel_PayTypes_st extends BasePopView implements View.OnClickListener {
    @Override // com.oysb.utils.view.BasePopView
    protected void registActions(List<String> list) {
    }

    @Override // com.oysb.utils.view.BasePopView
    protected View createView(LayoutInflater layoutInflater) {
        int dimensionPixelSize = getParent().getContext().getResources().getDimensionPixelSize(R.dimen.px80);
        RelativeLayout relativeLayout = new RelativeLayout(getParent().getContext());
        relativeLayout.setBackgroundColor(Color.parseColor("#88888888"));
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        relativeLayout.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        View inflate = layoutInflater.inflate(R.layout.st_popview_paytypes_v2, (ViewGroup) null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(13);
        inflate.setLayoutParams(layoutParams);
        relativeLayout.addView(inflate);
        inflate.findViewById(R.id.closeBtn).setOnClickListener(this);
        inflate.findViewById(R.id.pay_card).setOnClickListener(this);
        inflate.findViewById(R.id.pay_cash).setOnClickListener(this);
        inflate.findViewById(R.id.pay_qrcode).setOnClickListener(this);
        return relativeLayout;
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onAction(String str, Bundle bundle) {
        Loger.writeLog("SHJ", "ACIONT : " + str + " isShowing:" + isShowing());
        if (isShowing() && str.hashCode() == 0) {
            str.equals("");
        }
    }

    @Override // com.oysb.utils.view.BFPopView
    protected void onMessage(Message message) {
        int i = message.what;
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewWillShow() {
        super.onViewWillShow();
        if (!ShjManager.getOrderPayTypes().contains(OrderPayType.WEIXIN) && !ShjManager.getOrderPayTypes().contains(OrderPayType.ZFB) && !ShjManager.getOrderPayTypes().contains(OrderPayType.JD)) {
            findViewById(R.id.pay_qrcode).setVisibility(8);
        }
        if (ShjManager.getOrderPayTypes().contains(OrderPayType.ICCard) || ShjManager.getOrderPayTypes().contains(OrderPayType.ECard)) {
            return;
        }
        findViewById(R.id.pay_card).setVisibility(8);
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidShow() {
        super.onViewDidShow();
    }

    @Override // com.oysb.utils.view.BFPopView
    public void onViewDidClose() {
        super.onViewDidClose();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_card /* 2131231172 */:
                ShjAppBase.sysApp.sendBroadcast(new Intent(PopView_PaySummary_st.PAY_CARD_ST));
                break;
            case R.id.pay_cash /* 2131231173 */:
                ShjAppBase.sysApp.sendBroadcast(new Intent(PopView_PaySummary_st.PAY_CASH_ST));
                break;
            case R.id.pay_qrcode /* 2131231177 */:
                ShjAppBase.sysApp.sendBroadcast(new Intent(PopView_PaySummary_st.PAY_QRCODE_ST));
                break;
        }
        close();
    }
}
