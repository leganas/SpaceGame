package by.legan.spacegame.framework;
/**Интерфейс Music включает в себя методы для начала воспроизведения музыкального потока, приостановки и прекращения воспроизведения, а также циклического воспроизведения (начало проигрывания звука с начала сразу после окончания). Кроме того, мы можем установить громкость в виде типа данных float в диапазоне от 0 (тишина) до 1 (максимум). Интерфейс также содержит несколько методов получения, позволяющих отследить текущее состояние экземпляра Music. После того как необходимость в объекте Music отпадет, его необходимо утилизировать — это освободит системные ресурсы, а также снимет блокировку с воспроизводимого звукового файла.
 * */
public interface Music {
	public void play();
	public void stop();
	public void pause();
	public void setLooping(boolean looping);
	public void setVolume(float volume);
	public boolean isPlaying();
	public boolean isStopped();
	public boolean isLooping();
	public void dispose();
}