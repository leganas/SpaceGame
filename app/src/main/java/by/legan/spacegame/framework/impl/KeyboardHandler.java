package by.legan.spacegame.framework.impl;

import java.util.ArrayList;
import java.util.List;

import by.legan.spacegame.framework.Pool;
import by.legan.spacegame.framework.Input.KeyEvent;
import by.legan.spacegame.framework.Pool.PoolObjectFactory;

import android.view.View;
import android.view.View.OnKeyListener;

/**Обработчик событий клавиатуры*/
public class KeyboardHandler implements OnKeyListener {
/**текущее состояние (нажата или не нажата) каждой клавиши в данном массиве. Это состояние индексируется кодом клавиши*/
	boolean[] pressedKeys = new boolean[128];
/**экземпляры наших классов KeyEvent*/
	Pool<KeyEvent> keyEventPool;
/**элемент хранит KeyEvent, которые пока не были обработаны классом Game. Каждый раз, когда мы получаем новое событие клавиатуры в потоке пользовательского интерфейса, оно добавляется в этот список.*/
	List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();
/**хранит KeyEvent, которые мы вернем при вызове KeyboardHandler.getKeyEvents()*/
	List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
/**Данный конструктор имеет только один параметр, обозначающий тот вид (View), от которого мы хотим получать клавиатурные события.*/	
	public KeyboardHandler(View view) {
		PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
			@Override
			public KeyEvent createObject() {
				return new KeyEvent();
			}
		};
		keyEventPool = new Pool<KeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}
/**вызывается каждый раз, когда View обрабатывает новое клавиатурное событие. Однако таким образом игнорируются все клавиатурные события с типом KeyEvent.ACTION_MULTIPLE. Чтобы это исправить, используем блок синхронизации*/	
	@Override
	public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
		if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE) return false;
		synchronized (this) {
			KeyEvent keyEvent = keyEventPool.newObject();
			keyEvent.keyCode = keyCode;
			keyEvent.keyChar = (char) event.getUnicodeChar();
			if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				keyEvent.type = KeyEvent.KEY_DOWN;
				if(keyCode > 0 && keyCode < 127)
					pressedKeys[keyCode] = true;
			}
			if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
				keyEvent.type = KeyEvent.KEY_UP;
				if(keyCode > 0 && keyCode < 127) pressedKeys[keyCode] = false;
			}
			keyEventsBuffer.add(keyEvent);
		}
	return false;
	}
/**реализует семантику Input.isKeyPressed(). Мы передаем в него целое число, которое соответствует коду клавиши (одной из констант KeyEvent.KEYCODE_XXX Android) и возвращаем информацию о том, нажата данная клавиша или нет. Статус клавиши можно получить из массива pressedKey, предварительно проверив границы диапазона*/	
	public boolean isKeyPressed(int keyCode) {
		if (keyCode < 0 || keyCode > 127) return false;
		return pressedKeys[keyCode];
	}
/**реализует семантику метода Input.getKeyEvents(). Мы снова запускаем блок синхронизации, так как данный метод вызывается из другого потока*/	
	public List<KeyEvent> getKeyEvents() {
		synchronized (this) {
			int len = keyEvents.size();
			for (int i = 0; i < len; i++) keyEventPool.free(keyEvents.get(i));
			keyEvents.clear();
			keyEvents.addAll(keyEventsBuffer);
			keyEventsBuffer.clear();
			return keyEvents;
		}
	}
}
