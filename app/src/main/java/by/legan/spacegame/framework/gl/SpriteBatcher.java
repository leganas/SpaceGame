package by.legan.spacegame.framework.gl;
import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.impl.GLGraphics;
import by.legan.spacegame.framework.math.Vector2;

import android.util.FloatMath;

/**Класс для работы со спрайтами*/
public class SpriteBatcher {

/*Член класса verticesBuffer является временным float-массивом, в котором мы храним вершины спрайтов текущего пакета. 
 * Член bufferIndex указывает, с какого места float-массива мы должны начинать записывать следующие вершины. 
 * Член vertices — это экземпляр Vertices, который используется для отображения пакета. Он также хранит индексы, которые 
 * мы сейчас определим. Член numSprites содержит количество уже нарисованных спрайтов в текущем пакете*/	
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices vertices;
	int numSprites;
	
	/**Класс для работы со спрайтами*/
	public SpriteBatcher(GLGraphics glGraphics, int maxSprites) {
		this.verticesBuffer = new float[maxSprites*4*4];
		this.vertices = new Vertices(glGraphics, maxSprites*4, maxSprites*6,
		false, true);
		this.bufferIndex = 0;
		this.numSprites = 0;
		short[] indices = new short[maxSprites*6];
		int len = indices.length;
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
		indices[i + 0] = (short)(j + 0);
		indices[i + 1] = (short)(j + 1);
		indices[i + 2] = (short)(j + 2);
		indices[i + 3] = (short)(j + 2);
		indices[i + 4] = (short)(j + 3);
		indices[i + 5] = (short)(j + 0);
		}
		vertices.setIndices(indices, 0, indices.length);
	}
	
	/** Он привязывает текстуру и возвращает в исходное состояние члены numSprites и bufferIndex, так что вершины первого спрайта будут вставлены в начало массива float verticesBuffer*/
	public void beginBatch(Texture texture) {
		texture.bind();
		numSprites = 0;
		bufferIndex = 0;
	}
	
	/**Мы вызываем его, чтобы финализировать и отрисовать текущий пакет. Метод сначала переносит определенные для этого пакета вершины из массива float в экземпляр класса Vertices. Осталось привязать экземпляр класса Vertices, нарисовать numSprites ⋅ 2 треугольника и снова отвязать экземпляр класса Vertices. Поскольку мы используем индексированное отображение, мы определяем количество применяемых индексов, которое равно шести индексам для спрайта, умноженным на numSprites*/
	public void endBatch() {
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
		vertices.unbind();
	}
	/**Он принимает x- и y-координаты центра спрайта, его ширину и длину и TextureRegion, к которому он относится.
	   Метод добавляет четыре вершины в массив переменных float, начиная с текущего bufferIndex*/
	public void drawSprite(float x, float y, float width, float height,TextureRegion region) {
			float halfWidth = width / 2;
			float halfHeight = height / 2;
			float x1 = x - halfWidth;
			float y1 = y - halfHeight;
			float x2 = x + halfWidth;
			float y2 = y + halfHeight;
			verticesBuffer[bufferIndex++] = x1;
			verticesBuffer[bufferIndex++] = y1;
			verticesBuffer[bufferIndex++] = region.u1;
			verticesBuffer[bufferIndex++] = region.v2;
			verticesBuffer[bufferIndex++] = x2;
			verticesBuffer[bufferIndex++] = y1;
			verticesBuffer[bufferIndex++] = region.u2;
			verticesBuffer[bufferIndex++] = region.v2;
			verticesBuffer[bufferIndex++] = x2;
			verticesBuffer[bufferIndex++] = y2;
			verticesBuffer[bufferIndex++] = region.u2;
			verticesBuffer[bufferIndex++] = region.v1;
			verticesBuffer[bufferIndex++] = x1;
			verticesBuffer[bufferIndex++] = y2;
			verticesBuffer[bufferIndex++] = region.u1;
			verticesBuffer[bufferIndex++] = region.v1;
			numSprites++;
	}
	
	public void drawSprite(float x, float y, float width, float height,float angle, TextureRegion region) {
			float halfWidth = width / 2;
			float halfHeight = height / 2;
			float rad = angle * Vector2.TO_RADIANS;
			float cos = (float)Math.cos(rad);
			float sin = (float)Math.sin(rad);
			float x1 = -halfWidth * cos - (-halfHeight) * sin;
			float y1 = -halfWidth * sin + (-halfHeight) * cos;
			float x2 = halfWidth * cos - (-halfHeight) * sin;
			float y2 = halfWidth * sin + (-halfHeight) * cos;
			float x3 = halfWidth * cos - halfHeight * sin;
			float y3 = halfWidth * sin + halfHeight * cos;
			float x4 = -halfWidth * cos - halfHeight * sin;
			float y4 = -halfWidth * sin + halfHeight * cos;
			
			x1 += x;
			y1 += y;
			x2 += x;
			y2 += y;
			x3 += x;
			y3 += y;
			x4 += x;
			y4 += y;
			verticesBuffer[bufferIndex++] = x1;
			verticesBuffer[bufferIndex++] = y1;
			verticesBuffer[bufferIndex++] = region.u1;
			verticesBuffer[bufferIndex++] = region.v2;
			verticesBuffer[bufferIndex++] = x2;
			verticesBuffer[bufferIndex++] = y2;
			verticesBuffer[bufferIndex++] = region.u2;
			verticesBuffer[bufferIndex++] = region.v2;
			verticesBuffer[bufferIndex++] = x3;
			verticesBuffer[bufferIndex++] = y3;
			verticesBuffer[bufferIndex++] = region.u2;
			verticesBuffer[bufferIndex++] = region.v1;
			verticesBuffer[bufferIndex++] = x4;
			verticesBuffer[bufferIndex++] = y4;
			verticesBuffer[bufferIndex++] = region.u1;
			verticesBuffer[bufferIndex++] = region.v1;
			numSprites++;
	}
}
