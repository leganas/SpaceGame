package by.legan.spacegame.framework.gl;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.FileIO;
import by.legan.spacegame.framework.impl.GLGame;
import by.legan.spacegame.framework.impl.GLGraphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLUtils;
/**Объект для работы с текстурами*/
public class Texture {
	GLGraphics glGraphics;
	FileIO fileIO;
	String fileName;
	Bitmap bit=null;
	int textureId;
	int minFilter;
	int magFilter;
	public int width;
	public int height;
	boolean mipmapped;
	
	/**Загрузка текстуры в пространство OpenGL для работы с текстурами*/
	public Texture(GLGame glGame, String fileName) {
		this(glGame, fileName, false);
	}
	
	/**Конструктор с применением Mip-текстурирования*/
	public Texture(GLGame glGame, String fileName, boolean mipmapped) {
		this.glGraphics = glGame.getGLGraphics();
		this.fileIO = glGame.getFileIO();
		this.fileName = fileName;
		this.mipmapped = mipmapped;
		load();
	}

	/**Конструктор с применением прямой передачи Bitmap и Mip-текстурирования*/
	public Texture(GLGame glGame, Bitmap bit, boolean mipmapped) {
		this.glGraphics = glGame.getGLGraphics();
		this.fileIO = glGame.getFileIO();
		this.bit = bit;
		this.mipmapped = mipmapped;
		load();
	}
	
	/**Загрузка текстуры в буфер системы OpenGL ES*/
	private void load() {
		GL10 gl = glGraphics.getGL();
		int[] textureIds = new int[1];
		/*создать объект текстуры
		 * Первый параметр определяет, как много объектов текстур мы хотим создать. Обычно создаем всего одну
		 * Следующий параметр — это массив int, куда OpenGL ES будет записывать ID сгенерированных объектов текстур
		 * Последний параметр просто сообщает OpenGL ES, с какой точки массива нужно начинать записывать ID.*/
		gl.glGenTextures(1, textureIds, 0);
		/*Выбираем ID текстуры записанной в 0й элемент массива текстур*/
		textureId = textureIds[0];
		InputStream in = null;
		try {
			Bitmap bitmap;
			if (bit == null) {
			/*читаем текстуру из файла fileName в BitMap */
				in = fileIO.readAsset(fileName);
				Bitmap bittmap = BitmapFactory.decodeStream(in);
				bitmap = bittmap;
			} else {
				bitmap = bit;
			}
			if (mipmapped) {
				createMipmaps(gl, bitmap);
			} else {
			/*привязываем объект текстуры*/
			/*Первый параметр определяет тип текстуры, которую мы хотим привязать
			 * двухмерное, поэтому мы используем GL10.GL_TEXTURE_2D.
			 * Второй параметр данного метода ID текстуры*/
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			/*задача загрузки изображения текстуры
			 **1й параметр определить тип текстуры
			 * 2й параметр уровень детализации
			 * 3й параметр изображение, которое мы хотим загрузить
			 * 4й параметр всегда должен быть равен 0*/
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			/*Применение фильтров к текстуре при уменьшении при увеличении*/
			setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
			/*привязываем объект текстуры (уже с примененными фильтрами)*/
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			bitmap.recycle();
			}
		} catch(IOException e) {
		throw new RuntimeException("Couldn't load texture '"+ fileName +"'", e);
		} finally {
		if(in != null)
			try { in.close(); } catch (IOException e) { }
		}
	}
	
	private void createMipmaps(GL10 gl, Bitmap bitmap) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		setFilters(GL10.GL_LINEAR_MIPMAP_NEAREST, GL10.GL_LINEAR); 
		int level = 0;
		int newWidth = width;
		int newHeight = height;
		while (true) {
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			newWidth = newWidth / 2;
			newHeight = newHeight / 2;
		if (newWidth <= 0)
		break;
			Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHeight,bitmap.getConfig());
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(bitmap,new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),new Rect(0, 0, newWidth, newHeight), null);
			bitmap.recycle();
			bitmap = newBitmap;
			level++;
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		bitmap.recycle();
	}
	
	/*Перезагрузка(переинициализация) текстуры*/
	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
	
	/*Выбор применяемых фильтров к текстуре*/
	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		GL10 gl = glGraphics.getGL();
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,magFilter);
	}
	/*привязываем текстуру*/
	public void bind() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}
	
	/*Удаление текстуры из памяти*/
	public void dispose() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		int[] textureIds = { textureId };
		gl.glDeleteTextures(1, textureIds, 0);
	}
}