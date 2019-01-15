package by.legan.spacegame.framework;

/**Экран*/
public abstract class Screen{
	protected final Game game;
	public Screen(Game game) {
		this.game = game;
	}
	/**обновляют состояние экрана*/
	public abstract void update(float deltaTime);
	/**представляет экран пользователю*/
	public abstract void present(float deltaTime);
	/**вызываются при постановке игры на паузу*/
	public abstract void pause();
	/**вызываются при возобновлении игры после паузы(а также сразу после создания экрана)*/
	public abstract void resume();
	/**вызывается экземпляром Game при вызове Game.setScreen(). В результате текущий экземпляр Screen освобождает системные ресурсы*/
	public abstract void dispose();
}