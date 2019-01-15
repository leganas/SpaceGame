package by.legan.spacegame.framework;
import by.legan.spacegame.framework.math.Rectangle;
import by.legan.spacegame.framework.math.Vector2;

/**Класс описывающий игровой объект*/
public class GameObject {
	public final Vector2 position;
	public final Rectangle bounds;
	/**Статичный игровой объект*/
	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x,y);
		this.bounds = new Rectangle(x-width/2, y-height/2, width, height);
	}
}
