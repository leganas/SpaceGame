package by.legan.spacegame.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**Класс для реализации отдельного протока отрисовки SurfaceView*/
public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;
	long startTimeFrame = System.nanoTime();
	int frames = 0;
	int fps;
	
	/**реализации отдельного протока отрисовки SurfaceView
	 * @param game - ссылка на объект нашей игры типа (AndroidGame)
	 * @param framebuffer - фреймбуфер тип(Bitmap)*/
	public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}
	
	/**Вызывается каждый раз при возобновления игры (вызывается реализацией Game)*/
	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}
	
	/**Метод потока отрисовки*/
	public void run() {
		long startTime = System.nanoTime();
		Rect dstRect = new Rect();
		while(running) {
			if(!holder.getSurface().isValid()) continue;
			float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			game.getCurrentScreen().update(deltaTime);
// в качестве аргумента передается FPS (это как бы не верно , ну просто для теста, нужно изменить на deltaTime)
			game.getCurrentScreen().present(fpsCheck());
			Canvas canvas = holder.lockCanvas(); /*блокируем Canvas для начала отрисовки*/
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(framebuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas); /*разблокируем Canvas тем самым отрисовывая все изменения что произошли на экране*/
		}
	}
	
	/**Подсчет FPS*/
	public int fpsCheck() {
		frames++;
		if(System.nanoTime()-startTimeFrame >= 1000000000) {
			fps = (int)frames;
			frames = 0;
			startTimeFrame = System. nanoTime();
		}
		return fps;
	}

	/**Вызывается каждый раз при постановке игры на паузу (вызывается реализацией Game)*/
	public void pause() {
		running = false;
		while(true) {
			try {
				renderThread.join();
				break;
			} catch (InterruptedException e) {
			// повтор
			}
		}
	}
}	
		
