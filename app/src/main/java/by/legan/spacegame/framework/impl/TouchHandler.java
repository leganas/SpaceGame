package by.legan.spacegame.framework.impl;

import java.util.List;

import by.legan.spacegame.framework.Input.TouchEvent;

import android.view.View.OnTouchListener;

/**Чтобы мы могли использовать два класса обработчиков, заменяя один другим, нам необходимо определить общий интерфейс
 *  Методы интерфейса соответствуют методам интерфейса Input*/
public interface TouchHandler extends OnTouchListener {
	public boolean isTouchDown(int pointer);
	public int getTouchX(int pointer);
	public int getTouchY(int pointer);
	public List<TouchEvent> getTouchEvents();
}