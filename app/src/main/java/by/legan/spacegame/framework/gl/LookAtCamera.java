package by.legan.spacegame.framework.gl;
import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.math.Vector3;

import android.opengl.GLU;

/**Камера вид от 3его лица*/
public class LookAtCamera {
	final Vector3 position;
	final Vector3 up;
	final Vector3 lookAt;
	float fieldOfView;
	float aspectRatio;
	float near;
	float far;
	
	public LookAtCamera(float fieldOfView, float aspectRatio, float near,float far) {
		this.fieldOfView = fieldOfView;
		this.aspectRatio = aspectRatio;
		this.near = near;
		this.far = far;
		position = new Vector3();
		up = new Vector3(0, 1, 0);
		lookAt = new Vector3(0,0,-1);
	}
	/**Возвращает вектор позиции камеры, можно сразу делать ,set(x,y,z)*/
	public Vector3 getPosition() {
		return position;
	}
	/**Возвращает вектор смотрящий из вашей головы в верх, , можно сразу делать ,set(x,y,z) x = 0..1,y = 0..1,z = 0..1,*/
	public Vector3 getUp() {
		return up;
	}
	/**Возвращает вектор направления, , можно сразу делать ,set(x,y,z) x = 0..1,y = 0..1,z = 0..1,*/
	public Vector3 getLookAt() {
		return lookAt;
	}
	/**Собственно применяет установки нашей камеры, 
	 * задает матрицы проекции(область просмотра) и 
	 * модельно видовую(тут мы всё ворочаем как нам нужно в зависимости от настроек камеры)*/
	public void setMatrices(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, fieldOfView, aspectRatio, near, far);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, position.x, position.y, position.z, lookAt.x, lookAt.y, lookAt.z, up.x, up.y, up.z);
	}
}
