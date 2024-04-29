package com.shj.setting.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.oysb.utils.image.ImageViewHelper;
import com.shj.setting.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/* loaded from: classes2.dex */
public class GameDialog extends Dialog {
    private static final int CLOSE_TIME = 60;
    private static int[] diceId = {R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6};
    private Button bt_start;
    private Button button_close;
    private Button button_shopping;
    private Context context;
    private int delayTime;
    private List<ImageView> diceViewList;
    private Handler handler;
    private ImageView iv_dice_01;
    private ImageView iv_dice_02;
    private ImageView iv_dice_03;
    private ImageView iv_dice_04;
    private ImageView iv_dice_05;
    private ImageView iv_dice_06;
    private List<Integer> numberList;
    private Random random;
    private TextView tv_count_down;

    static /* synthetic */ int access$910(GameDialog gameDialog) {
        int i = gameDialog.delayTime;
        gameDialog.delayTime = i - 1;
        return i;
    }

    public GameDialog(Context context) {
        super(context, R.style.loading_style);
        this.numberList = new ArrayList();
        this.diceViewList = new ArrayList();
        this.handler = new Handler() { // from class: com.shj.setting.Dialog.GameDialog.10
            AnonymousClass10() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                GameDialog.access$910(GameDialog.this);
                GameDialog.this.tv_count_down.setText(String.valueOf(GameDialog.this.delayTime));
                if (GameDialog.this.delayTime > 0) {
                    GameDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
                } else {
                    try {
                        GameDialog.this.dismiss();
                    } catch (Exception unused) {
                    }
                }
            }
        };
        this.context = context;
        initView();
    }

    private void initView() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(17);
        setContentView(R.layout.layout_game);
        setCanceledOnTouchOutside(false);
        initData();
        findView();
        setListener();
    }

    private void initData() {
        this.random = new Random();
        for (int i = 0; i < 6; i++) {
            this.numberList.add(Integer.valueOf(i));
        }
    }

    private void findView() {
        this.tv_count_down = (TextView) findViewById(R.id.tv_count_down);
        this.button_close = (Button) findViewById(R.id.button_close);
        this.button_shopping = (Button) findViewById(R.id.button_shopping);
        this.iv_dice_01 = (ImageView) findViewById(R.id.iv_dice_01);
        this.iv_dice_02 = (ImageView) findViewById(R.id.iv_dice_02);
        this.iv_dice_03 = (ImageView) findViewById(R.id.iv_dice_03);
        this.iv_dice_04 = (ImageView) findViewById(R.id.iv_dice_04);
        this.iv_dice_05 = (ImageView) findViewById(R.id.iv_dice_05);
        this.iv_dice_06 = (ImageView) findViewById(R.id.iv_dice_06);
        this.diceViewList.add(this.iv_dice_01);
        this.diceViewList.add(this.iv_dice_02);
        this.diceViewList.add(this.iv_dice_03);
        this.diceViewList.add(this.iv_dice_04);
        this.diceViewList.add(this.iv_dice_05);
        this.diceViewList.add(this.iv_dice_06);
        this.bt_start = (Button) findViewById(R.id.bt_start);
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.dismiss();
        }
    }

    private void setListener() {
        this.button_close.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.1
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.dismiss();
            }
        });
        this.button_shopping.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.dismiss();
            }
        });
        this.bt_start.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.3
            AnonymousClass3() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.initDelayTime();
                Iterator it = GameDialog.this.diceViewList.iterator();
                while (it.hasNext()) {
                    GameDialog.this.randomNumber((ImageView) it.next());
                }
            }
        });
        this.iv_dice_01.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.4
            AnonymousClass4() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.initDelayTime();
                GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_01);
                GameDialog gameDialog = GameDialog.this;
                gameDialog.randomNumber(gameDialog.iv_dice_01);
            }
        });
        this.iv_dice_02.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.5
            AnonymousClass5() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.initDelayTime();
                GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_02);
                GameDialog gameDialog = GameDialog.this;
                gameDialog.randomNumber(gameDialog.iv_dice_02);
            }
        });
        this.iv_dice_03.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.6
            AnonymousClass6() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.initDelayTime();
                GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_03);
                GameDialog gameDialog = GameDialog.this;
                gameDialog.randomNumber(gameDialog.iv_dice_03);
            }
        });
        this.iv_dice_04.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.7
            AnonymousClass7() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.initDelayTime();
                GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_04);
                GameDialog gameDialog = GameDialog.this;
                gameDialog.randomNumber(gameDialog.iv_dice_04);
            }
        });
        this.iv_dice_05.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.8
            AnonymousClass8() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.initDelayTime();
                GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_05);
                GameDialog gameDialog = GameDialog.this;
                gameDialog.randomNumber(gameDialog.iv_dice_05);
            }
        });
        this.iv_dice_06.setOnClickListener(new View.OnClickListener() { // from class: com.shj.setting.Dialog.GameDialog.9
            AnonymousClass9() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                GameDialog.this.initDelayTime();
                GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_06);
                GameDialog gameDialog = GameDialog.this;
                gameDialog.randomNumber(gameDialog.iv_dice_06);
            }
        });
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.dismiss();
        }
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements View.OnClickListener {
        AnonymousClass3() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.initDelayTime();
            Iterator it = GameDialog.this.diceViewList.iterator();
            while (it.hasNext()) {
                GameDialog.this.randomNumber((ImageView) it.next());
            }
        }
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.initDelayTime();
            GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_01);
            GameDialog gameDialog = GameDialog.this;
            gameDialog.randomNumber(gameDialog.iv_dice_01);
        }
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$5 */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements View.OnClickListener {
        AnonymousClass5() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.initDelayTime();
            GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_02);
            GameDialog gameDialog = GameDialog.this;
            gameDialog.randomNumber(gameDialog.iv_dice_02);
        }
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$6 */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 implements View.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.initDelayTime();
            GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_03);
            GameDialog gameDialog = GameDialog.this;
            gameDialog.randomNumber(gameDialog.iv_dice_03);
        }
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements View.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.initDelayTime();
            GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_04);
            GameDialog gameDialog = GameDialog.this;
            gameDialog.randomNumber(gameDialog.iv_dice_04);
        }
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$8 */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements View.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.initDelayTime();
            GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_05);
            GameDialog gameDialog = GameDialog.this;
            gameDialog.randomNumber(gameDialog.iv_dice_05);
        }
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$9 */
    /* loaded from: classes2.dex */
    public class AnonymousClass9 implements View.OnClickListener {
        AnonymousClass9() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            GameDialog.this.initDelayTime();
            GameDialog.this.diceViewList.remove(GameDialog.this.iv_dice_06);
            GameDialog gameDialog = GameDialog.this;
            gameDialog.randomNumber(gameDialog.iv_dice_06);
        }
    }

    public void randomNumber(ImageView imageView) {
        synchronized (this.numberList) {
            if (this.numberList.size() > 0) {
                int nextInt = this.random.nextInt(this.numberList.size());
                int intValue = this.numberList.get(nextInt).intValue();
                this.numberList.remove(nextInt);
                playAnim(imageView, diceId[intValue]);
            }
        }
    }

    private void playAnim(ImageView imageView, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < 10; i2++) {
            arrayList.add(Integer.valueOf(diceId[this.random.nextInt(diceId.length)]));
        }
        arrayList.add(Integer.valueOf(i));
        ImageViewHelper.playImages(imageView, arrayList, 100, false);
    }

    public void setShoppingListering(View.OnClickListener onClickListener) {
        this.button_shopping.setOnClickListener(onClickListener);
    }

    /* renamed from: com.shj.setting.Dialog.GameDialog$10 */
    /* loaded from: classes2.dex */
    class AnonymousClass10 extends Handler {
        AnonymousClass10() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            GameDialog.access$910(GameDialog.this);
            GameDialog.this.tv_count_down.setText(String.valueOf(GameDialog.this.delayTime));
            if (GameDialog.this.delayTime > 0) {
                GameDialog.this.handler.sendEmptyMessageDelayed(0, 1000L);
            } else {
                try {
                    GameDialog.this.dismiss();
                } catch (Exception unused) {
                }
            }
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        this.handler.removeMessages(0);
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        initDelayTime();
        this.handler.sendEmptyMessageDelayed(0, 1000L);
    }

    public void initDelayTime() {
        this.delayTime = 60;
        this.tv_count_down.setText(String.valueOf(60));
    }
}
