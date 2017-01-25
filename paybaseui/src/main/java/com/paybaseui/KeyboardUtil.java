package com.paybaseui;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

/**
 * 键盘帮助类
 * Created by Administrator on 2017/1/15 0015.
 */
public class KeyboardUtil {
    private int vTag;
    private Activity act;
    private KeyboardView keyboardView;
    private Keyboard keyboard;// 数字键盘
    private EditText ed;
    private EditText ed1;
    private EditText ed2;
    //输入框一共有多少个子EditText
    private int childCount;
    private PayPasswordBoxView ppbView;
    //是否密文显示
    private boolean isPossword;
    private int viewId;


    /**
     * 设置这个键盘所在的Activity
     */
    public KeyboardUtil setActivity(Context act,int viewId) {
        if(this.act!=act||this.viewId!=viewId) {
            this.viewId=viewId;
            this.act = (Activity) act;
            keyboardView = (KeyboardView) this.act.findViewById(viewId);
        }
        return this;
    }


    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                isShow(false);
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null) {
                    //点击删除时：当前ET有文本就删除，没有文本就焦距前一个ET
                    if (editable.length() > 0) {
                            ed.getText().delete(ed.getText().toString().trim().length()-1,ed.getText().toString().trim().length());
                    } else {
                        if (vTag != 0) {
                            //如果是密文用下面那个方法就获取不到文本，所以干脆全部用土方法
                            ed1.getText().delete(ed1.getText().toString().trim().length()-1,ed1.getText().toString().trim().length());
//                          ed1.getText().delete(ed1.getSelectionStart() - 1, ed1.getSelectionStart());
                            ed1.requestFocus();
                        }
                    }
                }

            }
//            else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
//                changeKey();
//                keyboardView.setKeyboard(k1);
//
//            }
//            else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
//                if (isnun) {
//                    isnun = false;
//                    keyboardView.setKeyboard(k1);
//                } else {
//                    isnun = true;
//                    keyboardView.setKeyboard(k2);
//                }
//            }
//            else if (primaryCode == 57419) { // go left
//                if (start > 0) {
//                    ed.setSelection(start - 1);
//                }
//            }
//            else if (primaryCode == 57421) { // go right
//                if (start < ed.length()) {
//                    ed.setSelection(start + 1);
//                }
//            }
            else {
                //如果点击时有文本就焦距下一个ET并增添文本,否则只增添文本不跳转
                editable.insert(ed.getText().toString().trim().length(),Character.toString((char) primaryCode));
                setInputShowType(isPossword);
                if (vTag < childCount - 1) {
                    ed2.requestFocus();
                }
            }
        }
    };

    /**
     * 键盘大小写切换
     */
//    private void changeKey() {
//        List<Key> keylist = k1.getKeys();
//        if (isupper) {//大写切换小写
//            isupper = false;
//            for (Key key : keylist) {
//                if (key.label != null && isword(key.label.toString())) {
//                    key.label = key.label.toString().toLowerCase();
//                    key.codes[0] = key.codes[0] + 32;
//                }
//            }
//        } else {//小写切换大写
//            isupper = true;
//            for (Key key : keylist) {
//                if (key.label != null && isword(key.label.toString())) {
//                    key.label = key.label.toString().toUpperCase();
//                    key.codes[0] = key.codes[0] - 32;
//                }
//            }
//        }
//    }

//    public void showKeyboard() {
//        int visibility = keyboardView.getVisibility();
//        if (visibility == View.GONE || visibility == View.INVISIBLE) {
//            keyboardView.setVisibility(View.VISIBLE);
//        }
//    }

//    public void hideKeyboard() {
//        int visibility = keyboardView.getVisibility();
//        if (visibility == View.VISIBLE) {
//            keyboardView.setVisibility(View.INVISIBLE);
//        }
//    }

//    private boolean isword(String str) {
//        String wordstr = "abcdefghijklmnopqrstuvwxyz";
//        if (wordstr.indexOf(str.toLowerCase()) > -1) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 是否显示软键盘
     */
    public void isShow(boolean isShow) {
        if(keyboardView!=null) {
            if (isShow) {
                if (keyboardView.getVisibility() != View.VISIBLE) {
                    keyboardView.setVisibility(View.VISIBLE);
                }
            } else {
                keyboardView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置面板
     * @param panelLayout 面板布局
     */
    public KeyboardUtil setPanel(int panelLayout) {
        if(keyboard==null) {
            keyboard = new Keyboard(act, panelLayout);
            keyboardView.setKeyboard(keyboard);
        }
        return this;
    }

    /**
     * 设置View并监听
     *
     * @param view       整个输入框
     * @param isPossword 是否密文显示
     * @param vTag       当前焦距ET在输入框的第几个  @return
     */
    public KeyboardUtil setEditText(PayPasswordBoxView view, boolean isPossword, int vTag) {
        if(view==null) {
            ppbView = view;
        }
        childCount = view.getChildCount();
        ed = (EditText) view.getChildAt(vTag);
        ed1 = (EditText) view.getChildAt(vTag - 1);
        ed2 = (EditText) view.getChildAt(vTag + 1);
        this.isPossword = isPossword;
        this.vTag = vTag;
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false); //设置键盘提示的view
        keyboardView.setOnKeyboardActionListener(listener);
        return this;
    }


    /**
     * 是否密文显示
     */
    public void setInputShowType( boolean isPossword) {
        if (isPossword) {
            ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            ed.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }
}
