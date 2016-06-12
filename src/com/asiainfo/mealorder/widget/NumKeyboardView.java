package com.asiainfo.mealorder.widget;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asiainfo.mealorder.R;

/**
 * 注,此自定义键盘,需要在每个调用页面xml文件中,注入ID控件布局
 * Created by gjr on 2016/5/9 10:31.
 * mail : gjr9596@gmail.com
 */
public class NumKeyboardView {
    private final int MAX_NUM=20;
    private Activity act;
    private KeyboardView keyboardView;
    private Keyboard nunkey;
    private EditText ed;
    private OnConfirmListener mOnClickListener;
    private Button confirm;
    private Button delete;
    private View mView;

    public NumKeyboardView(Activity act,EditText edit,View view) {
        this.act = act;
        this.ed = edit;
        this.mView=view;
        nunkey = new Keyboard(act, R.xml.numkey);
        keyboardView = (KeyboardView) mView.findViewById(R.id.num_keyboardview);
        confirm=(Button)mView.findViewById(R.id.num_keyboard_search);
        delete=(Button)mView.findViewById(R.id.num_keyboard_delete);
        keyboardView.setKeyboard(nunkey);
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
        initListener();
    }

    public void initListener(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editable = ed.getText();
                if (editable != null && editable.length() > 0) {
                    if (mOnClickListener!=null)mOnClickListener.onConfirm(editable.toString());
                }else {
                    Toast.makeText(act,"输入结果为空!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editable = ed.getText();
                Selection.setSelection(editable, editable.length());//光标置尾
                int start = ed.getSelectionStart();
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }
        });
    }

    /**
     * 同页面edit焦点切换,时更新ed
     * @param edit
     */
    public void setEditText(EditText edit) {
        this.ed=edit;
    }

    /**
     * 设置确定按钮
     * @param pListener
     */
    public void setOnConfirmListener(OnConfirmListener pListener) {
        mOnClickListener = pListener;
    }

    /**
     * 设置确定按钮文字
     * @param info
     */
    public void setConfirmText(String info) {
        if (confirm!=null)confirm.setText(info);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
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
            Selection.setSelection(editable, editable.length());//光标置尾
            int start = ed.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_DONE) {// 完成,搜索确认
//                hideKeyboard();
                if (editable != null && editable.length() > 0) {
                    if (mOnClickListener!=null)mOnClickListener.onConfirm(editable.toString());
                }else {
                    Toast.makeText(act,"输入结果为空!",Toast.LENGTH_SHORT).show();
                }
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if(primaryCode == Keyboard.KEYCODE_CANCEL){
                if (editable != null && editable.length() > 0) {
                    editable.clear();
                }
            } else {
                if (editable != null && editable.length() > MAX_NUM) {
                    Toast.makeText(act,"超过20位,输入无效!",Toast.LENGTH_SHORT).show();
                    return;
                }
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };

    public void showKeyboard() {
        int visibility = mView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = mView.getVisibility();
        if (visibility == View.VISIBLE) {
            mView.setVisibility(View.INVISIBLE);
        }
    }

    public interface OnConfirmListener {
        public void onConfirm(String s);
    }
}
