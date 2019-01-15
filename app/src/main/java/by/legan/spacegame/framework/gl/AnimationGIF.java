package by.legan.spacegame.framework.gl;

import java.io.IOException;

import by.legan.spacegame.framework.impl.GLGame;

/**Класс для работы с анимацией*/
public class AnimationGIF {
	/**анимация должна быть циклической*/
	public static final int ANIMATION_LOOPING = 0;
	/**анимация должна остановится на последнем кадре*/
	public static final int ANIMATION_NONLOOPING = 1;
	/**Массив спрайтов анимации*/
	Texture[] keyFrames=null;
	/**Длительность анимации*/
	final float frameDuration;
	/**Сcылка на экземпляр класса игры*/
	GLGame game;
	/**Имя файла анимации*/
	String filename;

	/**Анимация
	 * @param game - ссылка на экземпляр класса игры
	 * @param filename - Имя файла с анимацией
	 * @param frameDuration - длительность анимации (Хотя это и не верно по отношению к Gif у него в нутри вшиты задержки между кадрами )*/
	public AnimationGIF(GLGame game,String filename,float frameDuration) {
		this.frameDuration = frameDuration;
		this.game = game;
		this.filename = filename;
		load();
	}

	/**Получить спрайт анимации (учитывая прошедшее время после создания объекта)
	 * @param stateTime - прошедшее время
	 * @param mode - режим отображения анимации*/
	public Texture getKeyFrame(float stateTime, int mode) {
		int frameNumber = (int)(stateTime / frameDuration);
		if(mode == ANIMATION_NONLOOPING) {
			frameNumber = Math.min(keyFrames.length-1, frameNumber);
		} else {
			frameNumber = frameNumber % keyFrames.length;
		}
		return keyFrames[frameNumber];
	}
	
	public void load(){
		GIFDecoder decode = new GIFDecoder();
	    try {
			decode.read(game.getFileIO().readAsset(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    if (keyFrames == null) keyFrames = new Texture[decode.getFrameCount()];
		for (int i = 0; i < decode.getFrameCount(); i++) {
			keyFrames[i] = new Texture(game,decode.getFrame(i),false);
		}
	}
	
	public void reload(){
		load();
	}
}
