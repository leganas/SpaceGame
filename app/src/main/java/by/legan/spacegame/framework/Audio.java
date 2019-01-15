package by.legan.spacegame.framework;

/**Работа со звуком. Методы Audio.newMusic() и Audio.newSound() принимают имя файла в качестве аргумента и вызывают исключение IOException при сбое процесса загрузки
 * */
public interface Audio {
	public Music newMusic(String filename);
	public Sound newSound(String filename);
}