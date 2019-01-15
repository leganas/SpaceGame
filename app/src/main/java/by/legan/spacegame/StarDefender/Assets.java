package by.legan.spacegame.StarDefender;


import by.legan.spacegame.framework.Music;
import by.legan.spacegame.framework.Sound;
import by.legan.spacegame.framework.gl.*;

public class Assets {
	 static Model3D[] model;
	 public static Texture background;
	 public static Font font;
	 
	 public static TextureRegion backgroundRegion;
	 public static Texture items;
	 public static TextureRegion logoRegion;
	 public static TextureRegion menuRegion;
	 public static TextureRegion gameOverRegion;
	 public static TextureRegion pauseRegion;
	 public static TextureRegion settingsRegion;
	 public static TextureRegion touchRegion;
	 public static TextureRegion accelRegion;
	 public static TextureRegion touchEnabledRegion;
	 public static TextureRegion accelEnabledRegion;
	 public static TextureRegion soundRegion;
	 public static TextureRegion soundEnabledRegion;
	 public static TextureRegion leftRegion;
	 public static TextureRegion rightRegion;
	 public static TextureRegion fireRegion;
	 public static TextureRegion pauseButtonRegion;
	 
	 public static Texture explosionTexture;
	 public static Animation explosionAnim;
	 public static AnimationGIF sunAnim;
 
	 public static Music music;
	 public static Sound clickSound;
	 public static Sound explosionSound;
	 public static Sound shotSound;
	 
	 public static void playSound(Sound sound) {
		if(Settings.soundEnabled) sound.play(1);
	 }
	
	 /**Перезагрузка текстур после потери контекста*/
	 public static void reload(){
			for (int i=0;i<model.length;i++){
				model[i].reload();
			}
		
			sunAnim.reload();

			if (items != null) items.reload();
			if (explosionTexture != null) explosionTexture.reload();
			if (background != null) background.reload();
		
			if (Settings.soundEnabled) music.play();
	 }
}