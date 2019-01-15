package by.legan.spacegame.framework.gl;
import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.math.Vector3;

import android.opengl.GLU;
import android.opengl.Matrix;

/**простая камера с видом от первого лица, основанная на Эйлеровых углах вокруг осей x и y*/
public class EulerCamera {
	final Vector3 position = new Vector3();
	float yaw;
	float pitch;
	float fieldOfView;
	float aspectRatio;
	float near;
	float far;
	/**простая камера с видом от первого лица, основанная на Эйлеровых углах вокруг осей x и y
	 *@param fieldOfView - поле обзора
	 *@param aspectRatio - соотношение сторон
	 *@param near - расстояние ближней плоскости отсечения
	 *@param far - расстояние дальней плоскости отсечения */
	public EulerCamera(float fieldOfView, float aspectRatio, float near,
			float far){
			this.fieldOfView = fieldOfView;
			this.aspectRatio = aspectRatio;
			this.near = near;
			this.far = far;
	}
	
	/**Возвращает позицию камеры*/
	public Vector3 getPosition() {
		return position;
		}
	/**Эти методы просто возвращают ориентацию камеры.*/
	public float getYaw() {
		return yaw;
	}
	/**Эти методы просто возвращают ориентацию камеры.*/
	public float getPitch() {
		return pitch;
	}
	/**позволяет нам напрямую устанавливать значения угла поворота камеры. 
	 * Обратите внимание на то, что мы ограничиваем значение угла поворота по оси x промежутком от –90 до 90.*/
	public void setAngles(float yaw, float pitch) {
		if (pitch < -90)
		pitch = -90;
		if (pitch > 90)
		pitch = 90;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	/**практически идентичен методу setAngles(). Вместо установки градусов он увеличивает их на значение параметра.*/
	public void rotate(float yawInc, float pitchInc) {
		this.yaw += yawInc;
		this.pitch += pitchInc;
		if (pitch < -90)
		pitch = -90;
		if (pitch > 90)
		pitch = 90;
	}
	/**инициализирует проекционную и модельно-видовую матрицы так, как мы говорили ранее. 
	 * Проекционная матрица устанавливается с помощью метода gluPerspective(), основываясь 
	 * на параметрах, переданных камере в конструкторе. Модельно-видовая матрица выполняет 
	 * прием «Магомет — гора», применяя поворот и параллельный перенос на оси x и y. Все факторы,
	 *  включенные в это действие, меняют свой знак на минус, чтобы достичь того, чтобы камера оставалась 
	 *  в начале координат и направлялась вдоль отрицательной части оси z. Именно поэтому мы поворачиваем и 
	 *  параллельно переносим объекты вокруг камеры. Других способов нет*/
	public void setMatrices(GL10 gl) {
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, fieldOfView, aspectRatio, near, far);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glRotatef(-pitch, 1, 0, 0);
		gl.glRotatef(-yaw, 0, 1, 0);
		gl.glTranslatef(-position.x, -position.y, -position.z);
	}
	
	final float[] matrix = new float[16];
	final float[] inVec = { 0, 0, -1, 1 };
	final float[] outVec = new float[4];
	final Vector3 direction = new Vector3();
	
	public Vector3 getDirection() {
		Matrix.setIdentityM(matrix, 0);
		Matrix.rotateM(matrix, 0, yaw, 0, 1, 0);
		Matrix.rotateM(matrix, 0, pitch, 1, 0, 0);
		Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
		direction.set(outVec[0], outVec[1], outVec[2]);
		return direction;
	}
}
