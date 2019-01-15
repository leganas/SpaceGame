package by.legan.spacegame.framework.math;

public class Sphere {
public final Vector3 center = new Vector3();
public float radius;

/**Класс ограничивающих сфер*/
public Sphere(float x, float y, float z, float radius) {
	this.center.set(x,y,z);
	this.radius = radius;
	}
}