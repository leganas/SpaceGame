package by.legan.spacegame.framework;
import by.legan.spacegame.framework.math.Vector2;

/**Расширение класса игрового объекта для динамически изменяющихся объектов , которые имеют скорость и ускорение*/
public class DynamicGameObject extends GameObject{
	public final Vector2 velocity;
	public final Vector2 accel;
	/**Динамичный игровой объект*/
	public DynamicGameObject(float x, float y, float width, float height) {
		super(x, y, width, height);
		velocity = new Vector2();
		accel = new Vector2();
	}
}
