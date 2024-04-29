package com.oysb.utils.debug;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.oysb.utils.CommonTool;
import com.oysb.utils.Loger;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class OysbDebug {
    static DebugItem current;
    static WeakReference<LinearLayout> wkButtons;
    static WeakReference<LinearLayout> wkContentView;
    static WeakReference<ListView> wkListView;
    static ArrayList<DebugItem> items = new ArrayList<>();
    public static boolean inited = false;
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message message) {
            try {
                ListAdapter adapter = (ListAdapter) OysbDebug.wkListView.get().getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Loger.safe_inner_exception_catch(e);
            }
            super.handleMessage(message);
        }
    };

    static class ListAdapter extends BaseAdapter {
        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getCount() {
            return (OysbDebug.current != null) ? OysbDebug.current.texts.size() : 0;
        }

        @Override
        public Object getItem(int i) {
            return (OysbDebug.current != null) ? OysbDebug.current.texts.get(i) : null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                Context context = wkListView.get().getContext();
                textView = new TextView(context);
                textView.setTextColor(-16777216); // Set text color to black
            } else {
                textView = (TextView) convertView;
            }

            Object item = getItem(position);
            String text = (item != null) ? item.toString() : "";
            textView.setText(text);
            return textView;
        }
    }

    public static class DebugItem {
        ArrayList<String> texts = new ArrayList<>();
        String title;

        public DebugItem(String str) {
            this.title = str;
        }
    }

    public static void init(Context context) {
        if (inited) {
            return;
        }
        LinearLayout linearLayout = new LinearLayout(context);
        wkContentView = new WeakReference<>(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(-1);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, CommonTool.dip2px(context, 40.0f));
        LinearLayout linearLayout2 = new LinearLayout(context);
        wkButtons = new WeakReference<>(linearLayout2);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setLayoutParams(layoutParams);
        linearLayout.addView(linearLayout2);

        Iterator<String> it = Loger.getLogSetting().keySet().iterator();
        while (it.hasNext()) {
            items.add(new DebugItem(it.next()));
        }
        items.add(new DebugItem("其它"));

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(CommonTool.dip2px(context, 80.0f), -1);
        Iterator<DebugItem> it2 = items.iterator();
        while (it2.hasNext()) {
            DebugItem next = it2.next();
            Button button = new Button(context);
            button.setLayoutParams(layoutParams2);
            button.setText(next.title);
            linearLayout2.addView(button);
            button.setTag(next);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OysbDebug.current = (DebugItem) ((Button) view).getTag();
                    ListAdapter adapter = (ListAdapter) OysbDebug.wkListView.get().getAdapter();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        ViewGroup.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(-1, CommonTool.dip2px(context, 1.0f));
        View view = new View(context);
        view.setLayoutParams(layoutParams3);
        view.setBackgroundColor(-12303292);
        linearLayout.addView(view);

        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, 0);
        layoutParams4.weight = 1.0f;
        ListView listView = new ListView(context);
        wkListView = new WeakReference<>(listView);
        listView.setLayoutParams(layoutParams4);
        linearLayout.addView(listView);
        listView.setAdapter(new ListAdapter());
        inited = true;
    }

    public static View getDebugView() {
        return wkContentView.get();
    }

    public static void addDebugInfo(String str, String str2) {
        Iterator<DebugItem> it = items.iterator();
        while (it.hasNext()) {
            DebugItem next = it.next();
            if (next.title.equalsIgnoreCase(str)) {
                next.texts.add(0, str2);
                if (next.texts.size() > 300) {
                    next.texts.remove(next.texts.size() - 1);
                }
            }
            if (next == current) {
                handler.sendEmptyMessage(1000);
            }
        }
    }
}
