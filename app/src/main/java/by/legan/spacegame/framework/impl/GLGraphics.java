package by.legan.spacegame.framework.impl;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;

/**OpenGL ES данный класс для визуализации потока, запущенного GLSurfaceView*/
public class GLGraphics {
	GLSurfaceView glView;
	GL10 gl;
	/**Инициализация графической подсистемы OpenGL, 
	 *@param GLSurfaceView - получаем объект рисования в графи. пространстве OpenGL ES*/
	GLGraphics(GLSurfaceView glView) {
		this.glView = glView;
	}
	/**доступ к функциям OpenGL ES
	 * @return GL10 - доступ к системе OpenGL ES конкретно для нашей области рисования*/
	public GL10 getGL() {
		return gl;
	}
	/**Вызывается потоком визуализации GLSurfaceView.Renderer во время назначения слушателя
	 * @param gl - объект доступа к функциям OpenGL ES (назначает поток визуализации)*/
	void setGL(GL10 gl) {
		this.gl = gl;
	}
	/**Получаем ширину области отрисовки
	 * @return int - ширина*/
	public int getWidth() {
		return glView.getWidth();
	}
	/**Получаем высоту области отрисовки
	 * @return int - высота*/
	public int getHeight() {
		return glView.getHeight();
	}
}