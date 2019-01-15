package by.legan.spacegame.framework.gl;


/**Класс для работы с анимацией*/
public class Animation {
	/**анимация должна быть циклической*/
	public static final int ANIMATION_LOOPING = 0;
	/**анимация должна остановится на последнем кадре*/
	public static final int ANIMATION_NONLOOPING = 1;
	/**Массив спрайтов анимации*/
	final TextureRegion[] keyFrames;
	/**Длительность анимации*/
	final float frameDuration;

	/**Анимация
	 * @param frameDuration - длительность анимации
	 * @param keyFrames - массив кадров (TextureRegion)*/
	public Animation(float frameDuration, TextureRegion ... keyFrames) {
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
	}

	/**Получить спрайт анимации (учитывая прошедшее время после создания объекта)
	 * @param stateTime - прошедшее время
	 * @param mode - режим отображения анимации*/
	public TextureRegion getKeyFrame(float stateTime, int mode) {
		int frameNumber = (int)(stateTime / frameDuration);
		if(mode == ANIMATION_NONLOOPING) {
			frameNumber = Math.min(keyFrames.length-1, frameNumber);
		} else {
			frameNumber = frameNumber % keyFrames.length;
		}
		return keyFrames[frameNumber];
	}
}
