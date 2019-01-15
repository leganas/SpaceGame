package by.legan.spacegame.framework.math;

import android.util.FloatMath;

/**Класс для работы с векторами*/
public class Vector2 {
	public static float TO_RADIANS = (1 / 180.0f) * (float) Math.PI;
	public static float TO_DEGREES = (1 / (float) Math.PI) * 180;
	public float x, y;
	
	public Vector2() {
	}
	
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
	}

	/**создаст дубликат экземпляра класса текущего вектора и вернет его. Это может быть достаточно удобно, если мы хотим управлять копией вектора, сохраняя значение начального вектора*/
	public Vector2 cpy() {
		return new Vector2(x, y);
	}
	
	/**Методы set() позволяют нам установить х- и у-компоненты вектора беря за основу либо два аргумента, выраженных числами с плавающей точкой*/
	public Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
		}

	/**Методы set() позволяют нам установить х- и у-компоненты вектора беря за основу другой вектор*/
	public Vector2 set(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
		return this;
	}
	
	/**Добавить вектор используя x,y возвращают ссылку на этот вектор, чтобы мы могли создать цепь операций*/
	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**Добавить вектор используя другой вектор возвращают ссылку на этот вектор, чтобы мы могли создать цепь операций*/
	public Vector2 add(Vector2 other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}
	
	/**Отнять вектор используя x,y возвращают ссылку на этот вектор, чтобы мы могли создать цепь операций*/
	public Vector2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}

	/**Отнять вектор используя другой вектор возвращают ссылку на этот вектор, чтобы мы могли создать цепь операций*/
	public Vector2 sub(Vector2 other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	
	/**умножает компоненты вектора х и у на данную скалярную величину и снова возвращается к вектору для цепи*/
	public Vector2 mul(float scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	/**Метод len() подсчитывает длину вектора ровно так же, как мы описывали это выше. 
	 * Обратите внимание, что мы используем класс FloatMath вместо обычного Math, предлагаемого Java SE
	 * Это специальный класс � � � � Android�API� , который работает с float, он немного быстрее, чем его аналог Math*/
	public float len() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	/**нормализирует вектор до единичной длины*/
	public Vector2 nor() {
		float len = len();
		if (len != 0) {
			this.x /= len;
			this.y /= len;
		}
		return this;
	}
	
	/**вычисляет угол между вектором и осью х, используя метод atan2()*/
	public float angle() {
		float angle = (float) Math.atan2(y, x) * TO_DEGREES;
		if (angle < 0)
		angle += 360;
		return angle;
	}
	
	/**поворачивает вектор вокруг начала координат на данный угол. Поскольку методы FloatMath.cos() и FloatMath.sin() требуют указания угла в радианах, переводим сначала градусы в радианы*/
	public Vector2 rotate(float angle) {
		float rad = angle * TO_RADIANS;
		float cos = (float)Math.cos(rad);
		float sin = (float)Math.sin(rad);
		float newX = this.x * cos - this.y * sin;
		float newY = this.x * sin + this.y * cos;
		this.x = newX;
		this.y = newY;
		return this;
	}
	/**подсчитывают расстояния между этим и другим вектором*/
	public float dist(Vector2 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		return (float)Math.sqrt(distX * distX + distY * distY);
	}
	
	/**подсчитывают расстояния между этим и другим вектором*/
	public float dist(float x, float y) {
		float distX = this.x - x;
		float distY = this.y - y;
		return (float)Math.sqrt(distX * distX + distY * distY);
	}
	
	/**вернет нам возведенное в квадрат расстояние между двумя векторами*/
	public float distSquared(Vector2 other) {
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		return distX*distX + distY*distY;
	}
	
	/**вернет нам возведенное в квадрат расстояние между двумя векторами*/
	public float distSquared(float x, float y) {
		float distX = this.x - x;
		float distY = this.y - y;
		return distX*distX + distY*distY;
	}
}
