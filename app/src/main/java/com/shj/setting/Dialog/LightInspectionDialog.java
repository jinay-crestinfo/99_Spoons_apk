package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.oysb.utils.ObjectHelper;
import com.shj.OnCommandAnswerListener;
import com.shj.Shj;
import com.shj.commandV2.CommandV2_Up_SetCommand;
import com.shj.setting.R;
import com.shj.setting.Utils.ToastUitl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class LightInspectionDialog extends Dialog {
    private Button bt_ok;
    private Context context;
    private int currentLayer;
    private LayoutInflater inflater;
    private int inspectionCount;
    private List<Button> layerButtonList;
    private List<String> layerList;
    private GridView listView;
    private LinearLayout ll_top;
    private MyAdapter myAdapter;
    private byte[] stateBytes;

    public LightInspectionDialog(Context context, int i, List<String> list, int i2, byte[] bArr) {
        super(context, R.style.loading_style);
        this.layerButtonList = new ArrayList();
        this.currentLayer = i;
        this.inspectionCount = i2;
        this.layerList = list;
        this.stateBytes = bArr;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_light_inspection);
        this.ll_top = (LinearLayout) findViewById(R.id.ll_top);
        this.listView = (GridView) findViewById(R.id.listView);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        setCanceledOnTouchOutside(false);
        setListener();
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
        addLayerButton();
        setSelectState();
    }

    private void addLayerButton() {
        int dimensionPixelOffset = this.context.getResources().getDimensionPixelOffset(R.dimen.x100);
        int dimensionPixelOffset2 = this.context.getResources().getDimensionPixelOffset(R.dimen.y55);
        int color = this.context.getResources().getColor(R.color.setting_white);
        int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R.dimen.text_small);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.topMargin = 10;
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.leftMargin = 10;
        int size = this.layerList.size();
        int size2 = size % 8 != 0 ? (this.layerList.size() / ((size / 8) + 1)) + 1 : 8;
        LinearLayout linearLayout = null;
        for (int i = 0; i < size; i++) {
            if (i % size2 == 0) {
                linearLayout = new LinearLayout(this.context);
                this.ll_top.addView(linearLayout, layoutParams);
            }
            Button button = new Button(this.context);
            button.setText(this.layerList.get(i));
            button.setWidth(dimensionPixelOffset);
            button.setHeight(dimensionPixelOffset2);
            button.setBackgroundResource(R.drawable.selector_button_blue);
            button.setTextColor(color);
            button.setTextSize(dimensionPixelSize);
            button.setGravity(17);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.LightInspectionDialog.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Button button2 = (Button) view;
                    LightInspectionDialog.this.currentLayer = Integer.valueOf(button2.getText().toString()).intValue();
                    LightInspectionDialog.this.inspectionCount = 0;
                    LightInspectionDialog.this.myAdapter.notifyDataSetChanged();
                    LightInspectionDialog.this.setSelectState();
                    LightInspectionDialog.this.lightInspectionStatusQuery(button2.getText().toString());
                }
            });
            this.layerButtonList.add(button);
            linearLayout.addView(button, layoutParams2);
        }
    }

    /* renamed from: com.shj.setting.Dialog.LightInspectionDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Button button2 = (Button) view;
            LightInspectionDialog.this.currentLayer = Integer.valueOf(button2.getText().toString()).intValue();
            LightInspectionDialog.this.inspectionCount = 0;
            LightInspectionDialog.this.myAdapter.notifyDataSetChanged();
            LightInspectionDialog.this.setSelectState();
            LightInspectionDialog.this.lightInspectionStatusQuery(button2.getText().toString());
        }
    }

    public void lightInspectionStatusQuery(String str) {
        CommandV2_Up_SetCommand commandV2_Up_SetCommand = new CommandV2_Up_SetCommand();
        if (str != null) {
            commandV2_Up_SetCommand.readLightInspectionStatus(Integer.valueOf(str).intValue());
            Shj.getInstance(this.context);
            Shj.postSetCommand(commandV2_Up_SetCommand, new OnCommandAnswerListener() { // from class: com.shj.setting.Dialog.LightInspectionDialog.2
                @Override // com.shj.OnCommandAnswerListener
                public void onCommandSetAnswer(boolean z) {
                }

                AnonymousClass2() {
                }

                @Override // com.shj.OnCommandAnswerListener
                public void onCommandReadAnswer(byte[] bArr) {
                    if (bArr == null || bArr.length <= 0) {
                        ToastUitl.showShort(LightInspectionDialog.this.context, R.string.qurey_fail);
                        return;
                    }
                    if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                        ToastUitl.showShort(LightInspectionDialog.this.context, R.string.qurey_fail);
                        return;
                    }
                    ObjectHelper.intFromBytes(bArr, 1, 1);
                    LightInspectionDialog.this.inspectionCount = ObjectHelper.intFromBytes(bArr, 2, 1);
                    LightInspectionDialog.this.stateBytes = ObjectHelper.bytesFromBytes(bArr, 3, 33);
                    LightInspectionDialog.this.myAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /* renamed from: com.shj.setting.Dialog.LightInspectionDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements OnCommandAnswerListener {
        @Override // com.shj.OnCommandAnswerListener
        public void onCommandSetAnswer(boolean z) {
        }

        AnonymousClass2() {
        }

        @Override // com.shj.OnCommandAnswerListener
        public void onCommandReadAnswer(byte[] bArr) {
            if (bArr == null || bArr.length <= 0) {
                ToastUitl.showShort(LightInspectionDialog.this.context, R.string.qurey_fail);
                return;
            }
            if (ObjectHelper.intFromBytes(bArr, 0, 1) != 0) {
                ToastUitl.showShort(LightInspectionDialog.this.context, R.string.qurey_fail);
                return;
            }
            ObjectHelper.intFromBytes(bArr, 1, 1);
            LightInspectionDialog.this.inspectionCount = ObjectHelper.intFromBytes(bArr, 2, 1);
            LightInspectionDialog.this.stateBytes = ObjectHelper.bytesFromBytes(bArr, 3, 33);
            LightInspectionDialog.this.myAdapter.notifyDataSetChanged();
        }
    }

    public void setSelectState() {
        Iterator<Button> it = this.layerButtonList.iterator();
        while (it.hasNext()) {
            it.next().setSelected(false);
        }
        for (Button button : this.layerButtonList) {
            if (String.valueOf(this.currentLayer).equalsIgnoreCase(button.getText().toString())) {
                button.setSelected(true);
                return;
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.LightInspectionDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LightInspectionDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.LightInspectionDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LightInspectionDialog.this.dismiss();
            }
        });
    }

    /* loaded from: classes2.dex */
    public class MyAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public MyAdapter(Context context) {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (LightInspectionDialog.this.stateBytes == null) {
                return 0;
            }
            return LightInspectionDialog.this.stateBytes.length;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LightInspectionDialog.this.inflater.inflate(R.layout.layout_light_inspection_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_index = (TextView) view.findViewById(R.id.tv_index);
                viewHolder.iv_01 = (TextView) view.findViewById(R.id.iv_01);
                viewHolder.iv_02 = (TextView) view.findViewById(R.id.iv_02);
                viewHolder.iv_03 = (TextView) view.findViewById(R.id.iv_03);
                viewHolder.iv_04 = (TextView) view.findViewById(R.id.iv_04);
                viewHolder.iv_05 = (TextView) view.findViewById(R.id.iv_05);
                viewHolder.iv_06 = (TextView) view.findViewById(R.id.iv_06);
                viewHolder.iv_07 = (TextView) view.findViewById(R.id.iv_07);
                viewHolder.iv_08 = (TextView) view.findViewById(R.id.iv_08);
                viewHolder.ivList = new ArrayList();
                viewHolder.ivList.add(viewHolder.iv_01);
                viewHolder.ivList.add(viewHolder.iv_02);
                viewHolder.ivList.add(viewHolder.iv_03);
                viewHolder.ivList.add(viewHolder.iv_04);
                viewHolder.ivList.add(viewHolder.iv_05);
                viewHolder.ivList.add(viewHolder.iv_06);
                viewHolder.ivList.add(viewHolder.iv_07);
                viewHolder.ivList.add(viewHolder.iv_08);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            byte b = LightInspectionDialog.this.stateBytes[i];
            for (int i2 = 0; i2 < 8; i2++) {
                if (i2 < LightInspectionDialog.this.inspectionCount) {
                    viewHolder.ivList.get(i2).setVisibility(0);
                    if (((b >> i2) & 1) == 1) {
                        viewHolder.ivList.get(i2).setText("1");
                        viewHolder.ivList.get(i2).setTextColor(LightInspectionDialog.this.context.getResources().getColor(R.color.setting_white));
                        viewHolder.ivList.get(i2).setBackgroundResource(R.drawable.inspection_state_shape);
                    } else {
                        viewHolder.ivList.get(i2).setText("0");
                        viewHolder.ivList.get(i2).setBackgroundResource(R.drawable.inspection_state_shape_green);
                        viewHolder.ivList.get(i2).setTextColor(LightInspectionDialog.this.context.getResources().getColor(R.color.setting_white));
                    }
                } else {
                    viewHolder.ivList.get(i2).setVisibility(8);
                }
            }
            viewHolder.tv_index.setText(String.valueOf(i + 1));
            return view;
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public List<TextView> ivList;
            public TextView iv_01;
            public TextView iv_02;
            public TextView iv_03;
            public TextView iv_04;
            public TextView iv_05;
            public TextView iv_06;
            public TextView iv_07;
            public TextView iv_08;
            public TextView tv_index;

            public ViewHolder() {
            }
        }
    }
}
