package com.paybaseui;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/1/20.
 */

public class PayPasswordBoxView extends LinearLayout {

    private final String nameSpace = "http://schemas.android.com/apk/res/com.testpaypasswordui";
    private int panel;
    //子Et样式，默认
    private int sonBackground=R.drawable.login_button_style;
    private KeyboardUtil kUtil;
    private InputFilter[] iFilter;
    private boolean isPossword = false;
    private int quantity = 0;
    private int aboutSpacing;
    private int wh;
    //当前焦距的ET的Tag
    private int vTag;
    //当前选中的ET
    private EditText eText;
    //前一个ET
    private EditText eText1;
    //绑定的键盘ViewId
    private int keyboardViewId;

    public PayPasswordBoxView(Context context) {
        this(context, null);
    }

    public PayPasswordBoxView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayPasswordBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iFilter = new InputFilter[]{new InputFilter.LengthFilter(1)};//约束只能输入一个字符
        kUtil = new KeyboardUtil();
        /**
         *获得自定义属性
         */
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String theAttrs = attrs.getAttributeName(i);
            if (theAttrs.equals("quantity")) {
                //子View数量；默认0
                quantity = attrs.getAttributeIntValue(i, 0);
            } else if (theAttrs.equals("aboutSpacing")) {
                //子View的左右间距;单位Px;默认0
                aboutSpacing = attrs.getAttributeIntValue(i, 0);
            } else if (theAttrs.equals("wh")) {
                //子View的宽高;单位Px;默认0
                wh = attrs.getAttributeIntValue(i,0);
            } else if (theAttrs.equals("sonBackground")) {
                //子View的背景;默认
                sonBackground = attrs.getAttributeResourceValue(i, R.drawable.login_button_style);
            } else if (theAttrs.equals("isPossword")) {
                //子View的输入类型是否是密码;默认flase
                isPossword = attrs.getAttributeBooleanValue(i, false);
            } else if (theAttrs.equals("panel")) {
                //键盘的面板样式;默认0
                panel = attrs.getAttributeResourceValue(i, 0);
            }else if(theAttrs.equals("keyboardViewId")){
                keyboardViewId=attrs.getAttributeResourceValue(i,0);
            }


        }
        /**
         * 初始化PayPasswordBoxView
         */
        iniView(context);
    }

    public void iniView(final Context context) {
        //增添子Et
        for (int i = 0; i < quantity; i++) {
            EditText et = new EditText(context);
            et.setFilters(iFilter);
            et.setGravity(Gravity.CENTER);
            et.setPadding(0, 0, 0, 0);//消除默认内边距
            et.setTag(i);
            //这里就直接指定子Et的文字大小了
            et.setTextSize(28);
            et.setInputType(InputType.TYPE_NULL);//不让弹出系统输入法
            //这个监听的目的：点击已经焦距的ET不会触发焦距监听也就不会显示键盘，这样可以防止键盘不显示，
            et.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    kUtil.isShow(true);
                }
            });
            et.setBackgroundResource(sonBackground);
            //初始化时，让每个ET都不获取焦点
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            this.requestFocus();
            //获取焦距监听
            et.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        vTag = (int) v.getTag();
                        //显示软键盘(必须在jianticontext，因为这时context才加载完全，否则context拿不到它里面的键盘View)
                        showKey(context,keyboardViewId,panel);
                        eText = (EditText) getChildAt(vTag);//当前ET
                        //点击的非第一个EditText，然后根据它前面的那个ET是否有Text来决定此次光标停在哪
                        if (vTag != 0) {
                            eText1 = (EditText) getChildAt(vTag - 1);//前一个ET
                            if (eText1.getText().toString().trim().equals("")) {
                                //前一个ET没有Text，那么前一个ET就获得焦点，然后它又会执行这个监听，又去判断它前面的一个ET是否有Text,直到判断到第0个为止
                                eText1.requestFocus();
                            } else {
                                //前一个ET有Text，再判断现在的ET是否有Text，如果没有就焦距当前，有就焦距下一个ET
                                if (vTag < quantity - 1) {
                                    if (!eText.getText().toString().trim().equals("")) {
                                        getChildAt(vTag + 1).requestFocus();
                                    }
                                }
                            }
                        } else {
                            //点击第0个ET
                            if (!eText.getText().toString().trim().equals("")) {
                                getChildAt(vTag + 1).requestFocus();
                            }
                        }
                    }
                }
            });
            LayoutParams params = new LayoutParams(wh, wh);//自定义宽高
            params.setMargins(aboutSpacing, 0, aboutSpacing, 0);//外边距（这里只增添了左右）
            et.setLayoutParams(params);
            this.setGravity(Gravity.CENTER);
            this.addView(et);
        }
    }


    /**
     * 把所有子Et上的文本拼接返回
     */
    public String getText() {
        EditText et = null;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < quantity; i++) {
            et = (EditText) this.getChildAt(i);
            sb.append(et.getText().toString());
        }
        return sb.toString();
    }


    /**
     * 设置子ET的数量
     *
     * @param quantity
     */
    public PayPasswordBoxView setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    /**
     * 设置子Et的宽高
     *
     * @param wh
     */
    public PayPasswordBoxView setWh(int wh) {
        this.wh = wh;
        return this;
    }

    /**
     * 设置子Et的背景
     *
     * @param sonBackground
     */
    public PayPasswordBoxView setSonBackground(int sonBackground) {
        this.sonBackground = sonBackground;
        return this;
    }


    /**
     * 子Et的左右外边距
     *
     * @param aboutSpacing
     */
    public PayPasswordBoxView setAboutSpacing(int aboutSpacing) {
        this.aboutSpacing = aboutSpacing;
        return this;
    }

    /**
     * 设置是否密文显示
     *
     * @param isPossword
     */
    public PayPasswordBoxView setPossword(boolean isPossword) {
        this.isPossword = isPossword;
        return this;
    }

    /**
     * 设置输入法的面板
     *
     * @param panel xml里
     */
    public PayPasswordBoxView setPanel(int panel) {
        this.panel = panel;
        return this;
    }
    /**
     * 设置绑定的键盘View的Id
     */
    public PayPasswordBoxView setKeyboardViewId(int id){
        this.keyboardViewId=id;
        return this;
    }

    /**
     * 显示软件盘
     * @param context           Activity
     * @param keyboardViewId    软键盘控件的Id
     * @param panel             软键盘面板
     */
    public void showKey(Context context,int keyboardViewId,int panel) {

        if(context!=null&&panel!=0&&keyboardViewId!=0) {
            /**
             * 光设置context不行，还要设一个软键盘控件的Id
             *以实现把输入框控件和软件盘控件相绑定
             */
            kUtil.setActivity(context,keyboardViewId)
                    .setPanel(panel)
                    .setEditText(PayPasswordBoxView.this, isPossword, vTag)
                    .isShow(true);
        }
    }



}
