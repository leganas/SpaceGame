package by.legan.spacegame.framework;

/**Интерфейс Sound, воспроизведение мелкого звука, — вызов метода play(), принимающего в качестве параметра громкость в виде числа float. Мы можем вызывать метод play() всякий раз, когда захотим (например, при каждом выстреле или прыжке персонажа). Когда необходимость в экземпляре Sound отпадет, его следует уничтожить
 * */
public interface Sound {
	public void play(float volume);
	public void dispose();
}