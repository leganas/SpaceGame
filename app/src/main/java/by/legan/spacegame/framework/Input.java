package by.legan.spacegame.framework;
import java.util.List;
/** 
 * Интерфейс, позволяющий опрашивать события от сенсорного экрана, акселерометра и клавиатуры, а также дающий доступ к обработчикам событий от дисплея и клавиатуры*/
public interface Input {
	/**Класс определяет константы, кодирующие тип KeyEvent,
	 * Экземпляр KeyEvent хранит его тип, код клавиши и Юникод-код (если тип события KEY_UP)*/
	public static class KeyEvent {
		public static final int KEY_DOWN = 0;
		public static final int KEY_UP = 1;
		public int type;
		public int keyCode;
		public char keyChar;
	}
	/**Класс определяет константы, кодирующие тип TouchEvent,
	 * тип TouchEvent, хранит позицию пальца относительно исходного элемента интерфейса, и ID указателя, выданный данному пальцу драйвером сенсорного экрана. Этот ID будет храниться до тех пор, пока палец будет касаться дисплея. При этом первый коснувшийся экрана палец получает ID, равный 0, следующий — 1 и т. д.*/
	public static class TouchEvent {
		public static final int TOUCH_DOWN = 0;
		public static final int TOUCH_UP = 1;
		public static final int TOUCH_DRAGGED = 2;
		public int type;
		public int x, y;
		public int pointer;
	}
	/**получает keyCode и возвращает результат — нажата соответствующая кнопка в данный момент или нет*/
	public boolean isKeyPressed(int keyCode);
	/**возвращают состояние переданного им указателя*/
	public boolean isTouchDown(int pointer);
	/**возвращают координату X переданного указателя*/
	public int getTouchX(int pointer);
	/**возвращают координату X переданного указателя*/
	public int getTouchY(int pointer);
	/**возвращают значения ускорения акселерометра для оси X.*/
	public float getAccelX();
	/**возвращают значения ускорения акселерометра для оси Y.*/
	public float getAccelY();
	/**возвращают значения ускорения акселерометра для оси Z.*/
	public float getAccelZ();
	/**Опрос нажатий на клавиш, возвращает List<KeyEvent>*/
	public List<KeyEvent> getKeyEvents();
	/**Опрос нажатий на экран, возвращает List<TouchEvent>*/
	public List<TouchEvent> getTouchEvents();
}

