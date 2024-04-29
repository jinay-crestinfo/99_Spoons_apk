package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.shj.setting.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class FileDeleteDialog extends Dialog {
    private Button bt_cancel;
    private Button bt_ok;
    private Context context;
    private List<FileData> enabledDataList;
    private ListView listView;
    private MyAdapter myAdapter;
    private String path;
    private TextView tv_no_file;

    /* loaded from: classes2.dex */
    public static class FileData {
        public boolean isEnable;
        public String name;
        public String path;
    }

    public FileDeleteDialog(Context context, String str) {
        super(context, R.style.loading_style);
        this.context = context;
        this.path = str;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_file_delete);
        this.listView = (ListView) findViewById(R.id.listView);
        this.bt_ok = (Button) findViewById(R.id.bt_ok);
        this.bt_cancel = (Button) findViewById(R.id.bt_cancel);
        this.tv_no_file = (TextView) findViewById(R.id.tv_no_file);
        setCanceledOnTouchOutside(false);
        setListener();
        getFiles();
        showList();
    }

    /* renamed from: com.shj.setting.Dialog.FileDeleteDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            for (FileData fileData : FileDeleteDialog.this.enabledDataList) {
                if (fileData.isEnable) {
                    File file = new File(fileData.path);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            FileDeleteDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.FileDeleteDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                for (FileData fileData : FileDeleteDialog.this.enabledDataList) {
                    if (fileData.isEnable) {
                        File file = new File(fileData.path);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
                FileDeleteDialog.this.dismiss();
            }
        });
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.FileDeleteDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FileDeleteDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.FileDeleteDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            FileDeleteDialog.this.dismiss();
        }
    }

    private void showList() {
        MyAdapter myAdapter = new MyAdapter(this.context);
        this.myAdapter = myAdapter;
        this.listView.setAdapter((ListAdapter) myAdapter);
        if (this.enabledDataList.size() <= 0) {
            this.tv_no_file.setVisibility(0);
        } else {
            this.tv_no_file.setVisibility(8);
        }
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
            return FileDeleteDialog.this.enabledDataList.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.layout_setting_enable_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.check_box = (CheckBox) view.findViewById(R.id.check_box);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.tv_name.setText(((FileData) FileDeleteDialog.this.enabledDataList.get(i)).name);
            viewHolder.check_box.setOnCheckedChangeListener(null);
            viewHolder.check_box.setChecked(((FileData) FileDeleteDialog.this.enabledDataList.get(i)).isEnable);
            viewHolder.check_box.setTag(Integer.valueOf(i));
            viewHolder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.Dialog.FileDeleteDialog.MyAdapter.1
                AnonymousClass1() {
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    ((FileData) FileDeleteDialog.this.enabledDataList.get(((Integer) compoundButton.getTag()).intValue())).isEnable = z;
                }
            });
            return view;
        }

        /* renamed from: com.shj.setting.Dialog.FileDeleteDialog$MyAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements CompoundButton.OnCheckedChangeListener {
            AnonymousClass1() {
            }

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ((FileData) FileDeleteDialog.this.enabledDataList.get(((Integer) compoundButton.getTag()).intValue())).isEnable = z;
            }
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public CheckBox check_box;
            public TextView tv_name;

            public ViewHolder() {
            }
        }
    }

    private void getFiles() {
        File[] listFiles = new File(this.path).listFiles();
        this.enabledDataList = new ArrayList();
        for (File file : listFiles) {
            if (file.isFile()) {
                FileData fileData = new FileData();
                fileData.name = file.getName();
                fileData.path = file.getAbsolutePath();
                fileData.isEnable = false;
                this.enabledDataList.add(fileData);
            }
        }
    }
}
