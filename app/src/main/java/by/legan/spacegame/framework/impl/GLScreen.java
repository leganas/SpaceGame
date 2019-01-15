package by.legan.spacegame.framework.impl;

import by.legan.spacegame.framework.Game;
import by.legan.spacegame.framework.Screen;

/**Обобщающий класс для хранения ссылок на объект GLGraphics, GLGame, и Screen*/
public abstract class GLScreen extends Screen {
	protected final GLGraphics glGraphics;
	protected final GLGame glGame;
	/**Мы храним экземпляры классов GLGraphics и GLGame. Конечно же, программа выдаст ошибку, если экземпляр класса Game, передаваемый конструктору, не будет иметь тип GLGame. Но теперь можно быть уверенным, что этого не случится.*/
	public GLScreen(Game game) {
		super(game);
		glGame = (GLGame)game;
		glGraphics = ((GLGame)game).getGLGraphics();
	}
}