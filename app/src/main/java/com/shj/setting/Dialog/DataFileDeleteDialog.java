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
import com.oysb.utils.io.file.SDFileUtils;
import com.shj.setting.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class DataFileDeleteDialog extends Dialog {
    private Button bt_cancel;
    private Button bt_ok;
    private Context context;
    private List<FileData> enabledDataList;
    private ListView listView;
    private MyAdapter myAdapter;
    private TextView tv_no_file;

    /* loaded from: classes2.dex */
    public static class FileData {
        public boolean isEnable;
        public String name;
        public List<String> pathList;
    }

    public DataFileDeleteDialog(Context context) {
        super(context, R.style.loading_style);
        this.context = context;
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

    /* renamed from: com.shj.setting.Dialog.DataFileDeleteDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            for (FileData fileData : DataFileDeleteDialog.this.enabledDataList) {
                if (fileData.isEnable) {
                    Iterator<String> it = fileData.pathList.iterator();
                    while (it.hasNext()) {
                        DataFileDeleteDialog.this.deleteFile(new File(it.next()));
                    }
                }
            }
            DataFileDeleteDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.bt_ok.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.DataFileDeleteDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                for (FileData fileData : DataFileDeleteDialog.this.enabledDataList) {
                    if (fileData.isEnable) {
                        Iterator<String> it = fileData.pathList.iterator();
                        while (it.hasNext()) {
                            DataFileDeleteDialog.this.deleteFile(new File(it.next()));
                        }
                    }
                }
                DataFileDeleteDialog.this.dismiss();
            }
        });
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.DataFileDeleteDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DataFileDeleteDialog.this.dismiss();
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.DataFileDeleteDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DataFileDeleteDialog.this.dismiss();
        }
    }

    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    deleteFile(file2);
                }
                return;
            }
            file.delete();
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
            return DataFileDeleteDialog.this.enabledDataList.size();
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
            viewHolder.tv_name.setText(((FileData) DataFileDeleteDialog.this.enabledDataList.get(i)).name);
            viewHolder.check_box.setOnCheckedChangeListener(null);
            viewHolder.check_box.setChecked(((FileData) DataFileDeleteDialog.this.enabledDataList.get(i)).isEnable);
            viewHolder.check_box.setTag(Integer.valueOf(i));
            viewHolder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shj.setting.Dialog.DataFileDeleteDialog.MyAdapter.1
                AnonymousClass1() {
                }

                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    ((FileData) DataFileDeleteDialog.this.enabledDataList.get(((Integer) compoundButton.getTag()).intValue())).isEnable = z;
                }
            });
            return view;
        }

        /* renamed from: com.shj.setting.Dialog.DataFileDeleteDialog$MyAdapter$1 */
        /* loaded from: classes2.dex */
        class AnonymousClass1 implements CompoundButton.OnCheckedChangeListener {
            AnonymousClass1() {
            }

            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ((FileData) DataFileDeleteDialog.this.enabledDataList.get(((Integer) compoundButton.getTag()).intValue())).isEnable = z;
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
        this.enabledDataList = new ArrayList();
        FileData fileData = new FileData();
        fileData.isEnable = false;
        fileData.name = "商品图片";
        fileData.pathList = new ArrayList();
        fileData.pathList.add(SDFileUtils.SDCardRoot + "xyShj/goodsImagesInfo.dat");
        fileData.pathList.add(SDFileUtils.SDCardRoot + "xyShj/images");
        this.enabledDataList.add(fileData);
        FileData fileData2 = new FileData();
        fileData2.isEnable = false;
        fileData2.name = "设置数据";
        fileData2.pathList = new ArrayList();
        fileData2.pathList.add(SDFileUtils.SDCardRoot + "xyShj/setting.db");
        fileData2.pathList.add(SDFileUtils.SDCardRoot + "xyShj/setting.db-journal");
        this.enabledDataList.add(fileData2);
        FileData fileData3 = new FileData();
        fileData3.isEnable = false;
        fileData3.name = "上报数据";
        fileData3.pathList = new ArrayList();
        fileData3.pathList.add(SDFileUtils.SDCardRoot + "xyShj/report.db");
        fileData3.pathList.add(SDFileUtils.SDCardRoot + "xyShj/report.db-journal");
        this.enabledDataList.add(fileData3);
        FileData fileData4 = new FileData();
        fileData4.isEnable = false;
        fileData4.name = "日志信息";
        fileData4.pathList = new ArrayList();
        fileData4.pathList.add(SDFileUtils.SDCardRoot + "xyShj/log");
        this.enabledDataList.add(fileData4);
        FileData fileData5 = new FileData();
        fileData5.isEnable = false;
        fileData5.name = "广告文件";
        fileData5.pathList = new ArrayList();
        fileData5.pathList.add(SDFileUtils.SDCardRoot + "xyShj/avFies");
        this.enabledDataList.add(fileData5);
        FileData fileData6 = new FileData();
        fileData6.isEnable = false;
        fileData6.name = "缓存数据";
        fileData6.pathList = new ArrayList();
        fileData6.pathList.add(SDFileUtils.SDCardRoot + "xyShj/cache.data");
        this.enabledDataList.add(fileData6);
        FileData fileData7 = new FileData();
        fileData7.isEnable = false;
        fileData7.name = "apk文件";
        fileData7.pathList = new ArrayList();
        fileData7.pathList.add(SDFileUtils.SDCardRoot + "xyShj/backUpApp");
        fileData7.pathList.add(SDFileUtils.SDCardRoot + "xyShj/download");
        fileData7.pathList.add(SDFileUtils.SDCardRoot + "xyShj/updata");
        this.enabledDataList.add(fileData7);
        FileData fileData8 = new FileData();
        fileData8.isEnable = false;
        fileData8.name = "全部数据";
        fileData8.pathList = new ArrayList();
        fileData8.pathList.add(SDFileUtils.SDCardRoot + "xyShj");
        this.enabledDataList.add(fileData8);
    }
}
