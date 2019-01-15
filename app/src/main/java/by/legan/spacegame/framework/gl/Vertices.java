package by.legan.spacegame.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.impl.GLGraphics;

/** Подробное описание страница (342)
 * Объект для работы с вершинами примитивных фигур OpenGL ES 2D
 * а также объединяющий весь функционал по отрисовке текстур в эти примитивах
 * определению их координат и если нужно индексов вершин, цвета вершин*/
public class Vertices {
	final GLGraphics glGraphics;
	final boolean hasColor;
	final boolean hasTexCoords;
	final int vertexSize;
	final FloatBuffer vertices;
	final ShortBuffer indices;
	/*  Вершины
	 *  В конструкторе определяем, какое максимальное количество вершин и индексов может содержать экземпляр класса Vertices,
	 *  а также имеют ли вершины цвет и текстурные координаты. Внутри конструктора устанавливаем соответствующие элементы и 
	 *  указываем значения буферам. Обратите внимание: если maxIndices равен нулю, ShortBuffer получит значение null. 
	 *  В таком случае визуализация будет выполняться без индексирования.*/
	public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices,	boolean hasColor, boolean hasTexCoords) {
			this.glGraphics = glGraphics;
			this.hasColor = hasColor;
			this.hasTexCoords = hasTexCoords;
			this.vertexSize = (2 + (hasColor?4:0) + (hasTexCoords?2:0)) * 4;
			ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices *vertexSize);
			buffer.order(ByteOrder.nativeOrder());
			vertices = buffer.asFloatBuffer();
			if(maxIndices > 0) {
				buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
				buffer.order(ByteOrder.nativeOrder());
				indices = buffer.asShortBuffer();
			} else {
				indices = null;
			}
	}
	
	/**Устанавливает вершины и их сопутствующие атрибуты если нужно(цвет, координаты текстуры)*/
	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		this.vertices.put(vertices, offset, length);
		this.vertices.flip();
		}
	
	/**Устанавливает индексы вершин*/
	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}
	
	/**Отрисовка примитивного элемента OpenGL ES 
	 * @param primitiveType - параметр тип элемента
	 * @param offset - параметр смещение в буфере вершин (или буфере индексов, если мы пользуемся индексами) 
	 * @param numVertices - параметр количество вершин, применяемых для визуализации*/
	public void drawSlow(int primitiveType, int offset, int numVertices) {
		GL10 gl = glGraphics.getGL();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		if(hasColor) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			vertices.position(2);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
		}
		if(hasTexCoords) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			vertices.position(hasColor?6:2);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		}
		if(indices!=null) {
			indices.position(offset);
			gl.glDrawElements(primitiveType, numVertices,
			GL10.GL_UNSIGNED_SHORT, indices);
		} else {
			gl.glDrawArrays(primitiveType, offset, numVertices);
		}
		if(hasTexCoords) gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		if(hasColor) gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
	
	/**Вызывается перед началом использования draw*/
	public void bind() {
		GL10 gl = glGraphics.getGL();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		if(hasColor) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			vertices.position(2);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
		}
		if(hasTexCoords) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			vertices.position(hasColor?6:2);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		}
	}
	
	/**Урезанный метод отрисовки для ускорения, требует вызова bind/unbind
	 * Отрисовка элемента OpenGL ES 
	 * @param primitiveType - тип фигур из которых состоит элемент (треугольники, и. т. д. )
	 * @param offset - параметр смещение в буфере вершин (или буфере индексов, если мы пользуемся индексами) 
	 * @param numVertices - параметр количество вершин, применяемых для визуализации*/
	public void draw(int primitiveType, int offset, int numVertices) {
		GL10 gl = glGraphics.getGL();
		if(indices!=null) {
			indices.position(offset);
			gl.glDrawElements(primitiveType, numVertices,GL10.GL_UNSIGNED_SHORT, indices);
		} else {
			gl.glDrawArrays(primitiveType, offset, numVertices);
		}
	}
	
	/**Вызывается после окончания использования draw, и перед использованием другого экземпляра класса Virtices*/
	public void unbind() {
		GL10 gl = glGraphics.getGL();
		if(hasTexCoords) gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		if(hasColor) gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
	