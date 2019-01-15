package by.legan.spacegame.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**Класс для работы с акселерометром и по сути с другими сенсорами*/
public class AccelerometerHandler implements SensorEventListener {
	float accelX;
	float accelY;
	float accelZ;
	/**Работа с сенсором (Акселерометром)
	 * @param - context - передаем ссылку от нашего приложения на текущий Context 
	 * для дебила :) -(со стороны вызывающей стороны нужно написать this)*/
	public AccelerometerHandler(Context context) {
		SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		/**Проверяем а правда ли у нас есть Акселерометр и регистрируем в качестве слушателя экземпляр этого класса*/
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_GAME);
		 }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Здесь ничего не делаем
	}
	
	/**Вызывается каждый раз при изменении показаний сенсора*/
	@Override
	public void onSensorChanged(SensorEvent event) {
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}
	/**Возвращает координату X*/
	public float getAccelX() {
		return accelX;
	}
	
	/**Возвращает координату Y*/
	public float getAccelY() {
		return accelY;
	}
	
	/**Возвращает координату Z*/
	public float getAccelZ() {
		return accelZ;
	}
}