package by.legan.spacegame.framework.gl;

/**Класс для работы с bitmap Шрифтами*/
public class Font {
	public final Texture texture;
	public final int glyphWidth;
	public final int glyphHeight;
	public final TextureRegion[] glyphs = new TextureRegion[96];
	/**Этот класс хранит текстуру, содержащую глифы шрифта, ширину и высоту одного глифа, а также массив 
	 * TextureRegions — по одному региону на каждый глиф
	 * @param glyphsPerRow говорит о том, сколько глифов будет в строке
	 * @param glyphWidth определяют ширину одного глифа
	 * @param glyphHeight определяют высоту одного глифа*/
	public Font(Texture texture,int offsetX, int offsetY,int glyphsPerRow, int glyphWidth, int glyphHeight) {
		this.texture = texture;
		this.glyphWidth = glyphWidth;
		this.glyphHeight = glyphHeight;
		int x = offsetX;
		int y = offsetY;
		for(int i = 0; i < 96; i++) {
			glyphs[i] = new TextureRegion(texture, x, y, glyphWidth,
			glyphHeight);
			x += glyphWidth;
			if(x == offsetX + glyphsPerRow * glyphWidth) {
				x = offsetX;
				y += glyphHeight;
			}
		}
	}
	
	/**Метод drawText() принимает экземпляр SpriteBatcher, строку текста и позиции x и y, откуда следует начинать рисовать текст. 
	 * Координаты x и y определяют центр первого глифа. 
	 * Мы получаем индекс каждого символа строки, проверяем, имеется ли для него глиф, и, 
	 * если ответ положительный, отрисовываем его с помощью экземпляра класса SpriteBatcher. 
	 * Далее увеличиваем координату x на значение glyphWidth. После этого мы готовы отрисовывать следующий символ строки.
	   Вы, возможно, задаетесь вопросом, почему нам не нужно привязывать текстуру, содержащую глифы. 
	   Предполагается, что это уже сделано перед вызовом метода drawText(). Причина заключается в том, 
	   что отрисовка текста может быть частью пакета, в этом случае текстура должна быть привязана заранее*/
	public void drawText(SpriteBatcher batcher, String text, float x,float y) {
		int len = text.length();
		for(int i = 0; i < len; i++) {
			int c = text.charAt(i) - ' ';
			if(c < 0 || c > glyphs.length - 1) continue;
			TextureRegion glyph = glyphs[c];
			batcher.drawSprite(x, y, glyphWidth, glyphHeight, glyph);
			x += glyphWidth;
		}
	}
}
