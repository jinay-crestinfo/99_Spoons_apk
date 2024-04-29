package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.shj.setting.R;
import com.shj.setting.widget.CargoGridView;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class GoodsInfoInputDialog extends Dialog {
    private Button bt_cancel;
    private Button bt_clear;
    private Button bt_close_keyboard;
    private Button bt_ok;
    private ButtonClickListener buttonClickListener;
    private Context context;
    private List<CargoGridView.IndexData> dataList;
    private boolean isAddAndReduceButtongVisible;
    private String itemName;
    private ListView listView;
    private int[] listViewLocation;
    Handler mHandler;
    private MyAdapter myAdapter;
    private String title;
    private TextView tv_title;

    /* loaded from: classes2.dex */
    public interface ButtonClickListener {
        void okClick();
    }

    public GoodsInfoInputDialog(Context context, String str, String str2, List<CargoGridView.IndexData> list) {
        super(context, R.style.loading_style);
        this.mHandler = new Handler() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.6
            AnonymousClass6() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what != 0) {
                    return;
                }
                EditText editText = (EditText) message.obj;
                editText.setSelection(editText.getText().length());
                GoodsInfoInputDialog.this.showKeyboard(editText);
            }
        };
        this.context = context;
        this.title = str;
        this.itemName = str2;
        this.dataList = list;
        initView();
    }

    public List<CargoGridView.IndexData> getDataList() {
        return this.dataList;
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_goodsinfo_input);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_clear = (Button) findViewById(R.id.bt_clear);
        this.bt_close_keyboard = (Button) findViewById(R.id.bt_close_keyboard);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_cancel = (Button) findViewById(R.id.bt_cancel);
        this.tv_title.setText(this.title);
        setCanceledOnTouchOutside(false);
        setListener();
        showList();
        setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.1
            AnonymousClass1() {
            }

            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialogInterface) {
                GoodsInfoInputDialog.this.listViewLocation = new int[2];
                GoodsInfoInputDialog.this.listView.getLocationOnScreen(GoodsInfoInputDialog.this.listViewLocation);
                new Thread(new Runnable() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.1.1
                    RunnableC00541() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        int i = 0;
                        while (i < 10) {
                            i++;
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (GoodsInfoInputDialog.this.isSoftShowing()) {
                                return;
                            }
                            View childAt = GoodsInfoInputDialog.this.listView.getChildAt(0);
                            if (childAt != null) {
                                ViewGroup viewGroup = (ViewGroup) childAt;
                                int childCount = viewGroup.getChildCount();
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= childCount) {
                                        break;
                                    }
                                    View childAt2 = viewGroup.getChildAt(i2);
                                    if (childAt2 instanceof EditText) {
                                        Message obtain = Message.obtain();
                                        obtain.obj = (EditText) childAt2;
                                        obtain.what = 0;
                                        GoodsInfoInputDialog.this.mHandler.sendMessage(obtain);
                                        break;
                                    }
                                    i2++;
                                }
                            }
                            try {
                                Thread.sleep(200L);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                            if (GoodsInfoInputDialog.this.isSoftShowing()) {
                                return;
                            }
                        }
                    }
                }).start();
            }

            /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$1$1 */
            /* loaded from: classes2.dex */
            class RunnableC00541 implements Runnable {
                RunnableC00541() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    int i = 0;
                    while (i < 10) {
                        i++;
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (GoodsInfoInputDialog.this.isSoftShowing()) {
                            return;
                        }
                        View childAt = GoodsInfoInputDialog.this.listView.getChildAt(0);
                        if (childAt != null) {
                            ViewGroup viewGroup = (ViewGroup) childAt;
                            int childCount = viewGroup.getChildCount();
                            int i2 = 0;
                            while (true) {
                                if (i2 >= childCount) {
                                    break;
                                }
                                View childAt2 = viewGroup.getChildAt(i2);
                                if (childAt2 instanceof EditText) {
                                    Message obtain = Message.obtain();
                                    obtain.obj = (EditText) childAt2;
                                    obtain.what = 0;
                                    GoodsInfoInputDialog.this.mHandler.sendMessage(obtain);
                                    break;
                                }
                                i2++;
                            }
                        }
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (GoodsInfoInputDialog.this.isSoftShowing()) {
                            return;
                        }
                    }
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements DialogInterface.OnShowListener {
        AnonymousClass1() {
        }

        @Override // android.content.DialogInterface.OnShowListener
        public void onShow(DialogInterface dialogInterface) {
            GoodsInfoInputDialog.this.listViewLocation = new int[2];
            GoodsInfoInputDialog.this.listView.getLocationOnScreen(GoodsInfoInputDialog.this.listViewLocation);
            new Thread(new Runnable() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.1.1
                RunnableC00541() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    int i = 0;
                    while (i < 10) {
                        i++;
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (GoodsInfoInputDialog.this.isSoftShowing()) {
                            return;
                        }
                        View childAt = GoodsInfoInputDialog.this.listView.getChildAt(0);
                        if (childAt != null) {
                            ViewGroup viewGroup = (ViewGroup) childAt;
                            int childCount = viewGroup.getChildCount();
                            int i2 = 0;
                            while (true) {
                                if (i2 >= childCount) {
                                    break;
                                }
                                View childAt2 = viewGroup.getChildAt(i2);
                                if (childAt2 instanceof EditText) {
                                    Message obtain = Message.obtain();
                                    obtain.obj = (EditText) childAt2;
                                    obtain.what = 0;
                                    GoodsInfoInputDialog.this.mHandler.sendMessage(obtain);
                                    break;
                                }
                                i2++;
                            }
                        }
                        try {
                            Thread.sleep(200L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (GoodsInfoInputDialog.this.isSoftShowing()) {
                            return;
                        }
                    }
                }
            }).start();
        }

        /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$1$1 */
        /* loaded from: classes2.dex */
        class RunnableC00541 implements Runnable {
            RunnableC00541() {
            }

            @Override // java.lang.Runnable
            public void run() {
                int i = 0;
                while (i < 10) {
                    i++;
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (GoodsInfoInputDialog.this.isSoftShowing()) {
                        return;
                    }
                    View childAt = GoodsInfoInputDialog.this.listView.getChildAt(0);
                    if (childAt != null) {
                        ViewGroup viewGroup = (ViewGroup) childAt;
                        int childCount = viewGroup.getChildCount();
                        int i2 = 0;
                        while (true) {
                            if (i2 >= childCount) {
                                break;
                            }
                            View childAt2 = viewGroup.getChildAt(i2);
                            if (childAt2 instanceof EditText) {
                                Message obtain = Message.obtain();
                                obtain.obj = (EditText) childAt2;
                                obtain.what = 0;
                                GoodsInfoInputDialog.this.mHandler.sendMessage(obtain);
                                break;
                            }
                            i2++;
                        }
                    }
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    if (GoodsInfoInputDialog.this.isSoftShowing()) {
                        return;
                    }
                }
            }
        }
    }

    public boolean isSoftShowing() {
        int[] iArr = new int[2];
        this.listView.getLocationOnScreen(iArr);
        return this.listViewLocation[1] != iArr[1];
    }

    public void showKeyboard(EditText editText) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            ((InputMethodManager) this.context.getSystemService("input_method")).showSoftInput(editText, 0);
        }
    }

    public void setAddAndReduceButtonVisible() {
        this.isAddAndReduceButtongVisible = true;
    }

    /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (GoodsInfoInputDialog.this.buttonClickListener != null) {
                GoodsInfoInputDialog.this.buttonClickListener.okClick();
            }
            GoodsInfoInputDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (GoodsInfoInputDialog.this.buttonClickListener != null) {
                    GoodsInfoInputDialog.this.buttonClickListener.okClick();
                }
                GoodsInfoInputDialog.this.dismiss();
            }
        });
        this.bt_clear.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Iterator it = GoodsInfoInputDialog.this.dataList.iterator();
                while (it.hasNext()) {
                    ((CargoGridView.IndexData) it.next()).inputText = "";
                }
                GoodsInfoInputDialog.this.myAdapter.notifyDataSetChanged();
            }
        });
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GoodsInfoInputDialog.this.dismiss();
            }
        });
        this.bt_close_keyboard.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    Context context = GoodsInfoInputDialog.this.context;
                    Context unused = GoodsInfoInputDialog.this.context;
                    ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(2, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Iterator it = GoodsInfoInputDialog.this.dataList.iterator();
            while (it.hasNext()) {
                ((CargoGridView.IndexData) it.next()).inputText = "";
            }
            GoodsInfoInputDialog.this.myAdapter.notifyDataSetChanged();
        }
    }

    /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GoodsInfoInputDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                Context context = GoodsInfoInputDialog.this.context;
                Context unused = GoodsInfoInputDialog.this.context;
                ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(2, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showList() {
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
    }

    public void clearText() {
        this.dataList.clear();
        this.myAdapter.notifyDataSetChanged();
    }

    /* loaded from: classes2.dex */
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return GoodsInfoInputDialog.this.dataList.size();
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.layout_goodsinfo_input_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.et_input = (EditText) view.findViewById(R.id.et_input);
                viewHolder.bt_add = (Button) view.findViewById(R.id.bt_add);
                viewHolder.bt_reduce = (Button) view.findViewById(R.id.bt_reduce);
                viewHolder.bt_sync = (Button) view.findViewById(R.id.bt_sync);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.et_input.setTag(Integer.valueOf(i));
            viewHolder.tv_name.setText(GoodsInfoInputDialog.this.itemName + String.format("%03d", Integer.valueOf(((CargoGridView.IndexData) GoodsInfoInputDialog.this.dataList.get(i)).identifier)));
            viewHolder.et_input.setText(((CargoGridView.IndexData) GoodsInfoInputDialog.this.dataList.get(i)).inputText);
            viewHolder.et_input.addTextChangedListener(new TextSwitcher(viewHolder));
            if (GoodsInfoInputDialog.this.isAddAndReduceButtongVisible) {
                viewHolder.bt_add.setVisibility(0);
                viewHolder.bt_reduce.setVisibility(0);
                viewHolder.bt_add.setOnClickListener(new MyOnClickListener(viewHolder, true));
                viewHolder.bt_reduce.setOnClickListener(new MyOnClickListener(viewHolder, false));
            }
            viewHolder.bt_sync.setTag(Integer.valueOf(i));
            viewHolder.bt_sync.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GoodsInfoInputDialog.MyAdapter.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    String str = ((CargoGridView.IndexData) GoodsInfoInputDialog.this.dataList.get(((Integer) view2.getTag()).intValue())).inputText;
                    Iterator it = GoodsInfoInputDialog.this.dataList.iterator();
                    while (it.hasNext()) {
                        ((CargoGridView.IndexData) it.next()).inputText = str;
                    }
                    MyAdapter.this.notifyDataSetChanged();
                }
            });
            return view;
        }

        /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$MyAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String str = ((CargoGridView.IndexData) GoodsInfoInputDialog.this.dataList.get(((Integer) view2.getTag()).intValue())).inputText;
                Iterator it = GoodsInfoInputDialog.this.dataList.iterator();
                while (it.hasNext()) {
                    ((CargoGridView.IndexData) it.next()).inputText = str;
                }
                MyAdapter.this.notifyDataSetChanged();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class ViewHolder {
        public Button bt_add;
        public Button bt_reduce;
        public Button bt_sync;
        public EditText et_input;
        public TextView tv_name;

        public ViewHolder() {
        }
    }

    /* loaded from: classes2.dex */
    class MyOnClickListener implements View.OnClickListener {
        private boolean isAdd;
        private ViewHolder mHolder;

        public MyOnClickListener(ViewHolder viewHolder, boolean z) {
            this.mHolder = viewHolder;
            this.isAdd = z;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                int intValue = Integer.valueOf(this.mHolder.et_input.getText().toString()).intValue();
                if (this.isAdd) {
                    this.mHolder.et_input.setText(String.valueOf(intValue + 1));
                } else if (intValue >= 1) {
                    this.mHolder.et_input.setText(String.valueOf(intValue - 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* loaded from: classes2.dex */
    class TextSwitcher implements TextWatcher {
        private ViewHolder mHolder;

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public TextSwitcher(ViewHolder viewHolder) {
            this.mHolder = viewHolder;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            ((CargoGridView.IndexData) GoodsInfoInputDialog.this.dataList.get(((Integer) this.mHolder.et_input.getTag()).intValue())).inputText = charSequence.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.shj.setting.Dialog.GoodsInfoInputDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 extends Handler {
        AnonymousClass6() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 0) {
                return;
            }
            EditText editText = (EditText) message.obj;
            editText.setSelection(editText.getText().length());
            GoodsInfoInputDialog.this.showKeyboard(editText);
        }
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }
}
