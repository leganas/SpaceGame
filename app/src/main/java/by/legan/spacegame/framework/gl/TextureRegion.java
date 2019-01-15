package by.legan.spacegame.framework.gl;

public class TextureRegion {
	public final float u1, v1;
	public final float u2, v2;
	public final Texture texture;
	
	/**Класс TextureRegion хранит текстурные координаты верхнего левого угла (u1; v1) и 
	 * нижнего правого угла (u2; v2) фрагмента в текстурных координатах*/
	public TextureRegion(Texture texture, float x, float y, float width,float height) {
		this.u1 = x / texture.width;
		this.v1 = y / texture.height;
		this.u2 = this.u1 + width / texture.width;
		this.v2 = this.v1 + height / texture.height;
		this.texture = texture;
	}
}
