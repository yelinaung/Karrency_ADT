/**
 * Modified by Ye Lin Aung
 * android.R.drawable.presence_offline is replaced with ic_action_remove icon
 * 15/04/2014
 */

/**
 * Copyright 2014 Alex Yanchenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yelinaung.karrency.app.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import com.yelinaung.karrency.app.util.TextWatcherAdapter;
import com.yelinaung.karrency.app.R;

/**
 * To change clear icon, set
 *
 * <pre>
 * android:drawableRight="@drawable/custom_icon"
 * </pre>
 */
public class ClearableEditText extends EditText
    implements OnTouchListener, OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {

  public interface Listener {
    void didClearText();
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  private Drawable xD;
  private Listener listener;

  public ClearableEditText(Context context) {
    super(context);
    init();
  }

  public ClearableEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  @Override
  public void setOnTouchListener(OnTouchListener l) {
    this.l = l;
  }

  @Override
  public void setOnFocusChangeListener(OnFocusChangeListener f) {
    this.f = f;
  }

  private OnTouchListener l;
  private OnFocusChangeListener f;

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    if (getCompoundDrawables()[2] != null) {
      boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD.getIntrinsicWidth());
      if (tappedX) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
          setText("");
          if (listener != null) {
            listener.didClearText();
          }
        }
        return true;
      }
    }
    if (l != null) {
      return l.onTouch(v, event);
    }
    return false;
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {
    if (hasFocus) {
      setClearIconVisible(isNotEmpty(getText()));
    } else {
      setClearIconVisible(false);
    }
    if (f != null) {
      f.onFocusChange(v, hasFocus);
    }
  }

  @Override
  public void onTextChanged(EditText view, String text) {
    if (isFocused()) {
      setClearIconVisible(isNotEmpty(text));
    }
  }

  private void init() {
    xD = getCompoundDrawables()[2];
    if (xD == null) {
      xD = getResources().getDrawable(R.drawable.ic_action_remove);
    }
    xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
    setClearIconVisible(false);
    super.setOnTouchListener(this);
    super.setOnFocusChangeListener(this);
    addTextChangedListener(new TextWatcherAdapter(this, this));
  }

  protected void setClearIconVisible(boolean visible) {
    Drawable x = visible ? xD : null;
    setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], x,
        getCompoundDrawables()[3]);
  }

  public static boolean isNotEmpty(CharSequence str) {
    return !isEmpty(str);
  }

  public static boolean isEmpty(CharSequence str) {
    return str == null || str.length() == 0;
  }
}