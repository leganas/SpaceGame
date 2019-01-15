package by.legan.spacegame.StarDefender;
import by.legan.spacegame.framework.Screen;
import by.legan.spacegame.framework.impl.GLGame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**Базовый класс OpenGL 3D test*/
public class DroidInvaders extends GLGame {
	boolean firstTimeCreate = true;

	@Override
	/*Реализуем абстрактный метод (описанный в интерфейсе Game) который будет вызван в onCreate, Активности GLGame*/
	public Screen getStartScreen() {
		return new LoadingData(this);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		if (firstTimeCreate) {
			Settings.load(getFileIO());
			firstTimeCreate = false;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (Settings.soundEnabled)
			Assets.music.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (Settings.soundEnabled)
			Assets.music.pause();
	}
	
	
}

	
