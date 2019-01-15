package by.legan.spacegame.framework;

/**Интерфейс игры*/
public interface Game {
	/**Получить доступ к объекту ввода*/
	public Input getInput();
	/**Получить доступ к объекту файлового ввода/вывода*/
	public FileIO getFileIO();
	/**Получить доступ к объекту для работы с графикой обычной*/
	public Graphics getGraphics();
	/**Получить доступ к объекту для работы со звуком*/
	public Audio getAudio();
	/**Позволяет установить для игры текущий экран*/
	public void setScreen(Screen screen);
	/**Возвращает текущий активный экран.*/
	public Screen getCurrentScreen();
	/**останется абстрактным. Когда мы создадим экземпляр AndroidGame для нашей игры, то унаследуем и реализуем в нем метод Game.getStartScreen(), возвратив экземпляр первого экрана игры*/
	public Screen getStartScreen();
}