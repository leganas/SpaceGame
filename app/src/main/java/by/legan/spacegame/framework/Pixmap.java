package by.legan.spacegame.framework;

import by.legan.spacegame.framework.Graphics.PixmapFormat;

/**Фрейм буфер вроди как*/
public interface Pixmap {
	/**возвращают ширину объекта*/
	public int getWidth();
	/**возвращают высоту объекта*/
	public int getHeight();
	/**возвращает формат PixelFormat, используемый для хранения Pixmap в оперативной памяти.*/
	public PixmapFormat getFormat();
	/**экземпляры Pixmap применяют память и потенциально другие системные ресурсы. Если они нам больше не нужны, их следует уничтожить с помощью данного метода.*/
	public void dispose();
}
