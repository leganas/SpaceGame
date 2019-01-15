package by.legan.spacegame.framework.impl;

import by.legan.spacegame.framework.Audio;
import by.legan.spacegame.framework.FileIO;
import by.legan.spacegame.framework.Game;
import by.legan.spacegame.framework.Graphics;
import by.legan.spacegame.framework.Input;
import by.legan.spacegame.framework.Screen;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

/**Класс реализующий концепцию описанную интерфейсом Game 
 * инкапсулирует в себе все необходимые классы для Игры использующей для отрисовки стандартный Canvas*/
public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;
	
	/*Инициализация игры при старте, задание параметров Активности*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 480 : 320;
		int frameBufferHeight = isLandscape ? 320 : 480;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,frameBufferHeight, Config.RGB_565);
		float scaleX = (float) frameBufferWidth	/ getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) frameBufferHeight/ getWindowManager().getDefaultDisplay().getHeight();
		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"GLGame");
	}
	
	/**Вызывается системой при возобновлении фокуса на нашу активность (в том числе и 1й раз после старта)*/
	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}
	
	/**Вызывается системой перед постановкой активности на паузу*/
	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		if (isFinishing()) screen.dispose();
	}
	
	/**получить доступ к интерфейсу ввода (тач,акселерометр и.т.д (всё что будет реализовано в AndroidInput))*/
	@Override
	public Input getInput() {
		return input;
	}
	
	/**получить доступ к интерфейсу работы с файлами*/
	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	/**Доступ к интерфейсу работы с графикой по средствам обычного Canvas*/
	@Override
	public Graphics getGraphics() {
		return graphics;
	}
	
	/**Доступ к интерфейсу для работы с Audio*/
	@Override
	public Audio getAudio() {
		return audio;
	}
	
	@Override
	public void setScreen(Screen screen) {
		if (screen == null) throw new IllegalArgumentException("Screen must not be null");
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
	
	public Screen getCurrentScreen() {
		return screen;
	}
}
	