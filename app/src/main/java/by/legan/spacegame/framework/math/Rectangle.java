package by.legan.spacegame.framework.math;

/**Класс описывающий треугольник*/
public class Rectangle {
	public final Vector2 lowerLeft;
	public float width, height;
	/**Описание треугольника заданных размеров и вектора по x,y*/
	public Rectangle(float x, float y, float width, float height) {
		this.lowerLeft = new Vector2(x,y);
		this.width = width;
		this.height = height;
	}
	
}
