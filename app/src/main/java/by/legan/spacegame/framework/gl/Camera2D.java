package by.legan.spacegame.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.impl.GLGraphics;
import by.legan.spacegame.framework.math.Vector2;

/**Класс камеры (матрицы проецирования и порта просмотра)*/
public class Camera2D {
	public final Vector2 position;
	public float zoom;
	public final float frustumWidth;
	public final float frustumHeight;
	final GLGraphics glGraphics;
	/**Задание матрицы проецирования (экрана(камеры)), в заданной области View
	 * @param glGraphis - область OpenGL для рисования
	 * @param frustumWidth - ширина камеры2D (области просмотра)
	 * @param frustumHeight - высота камеры2D (области просмотра)*/
	public Camera2D(GLGraphics glGraphics, float frustumWidth,float frustumHeight) {
			this.glGraphics = glGraphics;
			this.frustumWidth = frustumWidth;
			this.frustumHeight = frustumHeight;
			this.position = new Vector2(frustumWidth / 2, frustumHeight / 2);
			this.zoom = 1.0f;
	}

	/**Метод setViewportAndMatrices() задает область просмотра, охватывающую весь экран, а также задает проекционную матрицу в соответствии с параметрами нашей камеры*/	
	public void setViewportAndMatrices() {
		GL10 gl = glGraphics.getGL();
		/*задает область просмотра, охватывающую весь экран отрисовки наш glGraphics*/
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		/*выбираем матрицу проекции (тое-сть куда OpenGL проецирует всё), загружаем единичную матрицу*/
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		/*задает проекционную матрицу в соответствии с параметрами нашей камеры (короче это и есть задание нашей камеры)*/
		gl.glOrthof(position.x -frustumWidth * zoom / 2,
					position.x + frustumWidth * zoom/ 2,
					position.y - frustumHeight * zoom / 2,
					position.y + frustumHeight * zoom/ 2,1, -1);
		/*все дальнейшие матричные операции направлены на матрицу модели-вида, и загружаем единичную матрицу*/
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**Принимает член класса Vector2, содержащий координаты, 
	 * Которые получены при касании, и переводит вектор в пространство мира (с учетом всяких настроек камеры)
	 * @param touch - вектор*/
	public void touchToWorld(Vector2 touch) {
		touch.x = (touch.x / (float) glGraphics.getWidth()) * frustumWidth * zoom;
		touch.y = (1 - touch.y / (float) glGraphics.getHeight()) *	frustumHeight * zoom;
		touch.add(position).sub(frustumWidth * zoom / 2,frustumHeight * zoom / 2);
	}
}
