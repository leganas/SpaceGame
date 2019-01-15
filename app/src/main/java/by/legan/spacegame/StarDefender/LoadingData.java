package by.legan.spacegame.StarDefender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.gl.Animation;
import by.legan.spacegame.framework.gl.AnimationGIF;
import by.legan.spacegame.framework.gl.Camera2D;
import by.legan.spacegame.framework.gl.Font;
import by.legan.spacegame.framework.gl.ItemsModel3D;
import by.legan.spacegame.framework.gl.Material;
import by.legan.spacegame.framework.gl.Model3D;
import by.legan.spacegame.framework.gl.SpriteBatcher;
import by.legan.spacegame.framework.gl.Texture;
import by.legan.spacegame.framework.gl.TextureRegion;
import by.legan.spacegame.framework.gl.Vertices;
import by.legan.spacegame.framework.gl.Vertices3;
import by.legan.spacegame.framework.impl.GLGame;
import by.legan.spacegame.framework.impl.GLScreen;

import android.util.Log;

/**Специальный класс (заготовка) для загрузки данных и автоматической передачи управления следующему экрану*/
public class LoadingData  extends GLScreen {
	enum LoadState {
		Init,
		Load,
		LoadMaterial,
		LoadComplete
	}
	public LoadState state = LoadState.Init;
	public int loadThread = 0;
	public static int loadprogress = 0;
	public static int maxprogress = 1;
	Camera2D guiCam;
	Camera2D cam;
	SpriteBatcher batcher;
	float[] vertices = {0,4, 1, 1, 1, 1,
						1,5, 1, 1, 1, 1, // этот менять
						0,5, 1, 1, 1, 1,
						0,4, 1, 1, 1, 1,
						1,4, 1, 1, 1, 1, // этот менять
						1,5, 1, 1, 1, 1}; // этот тоже
	Vertices progressbar;
	Texture load_background;
	public static Texture items;

	public LoadingData(GLGame game) {
		super(game);
		guiCam = new Camera2D(glGraphics, 100, 6);
		cam = new Camera2D(glGraphics, 480, 320);
		batcher = new SpriteBatcher(glGraphics, 1000);
		load_background = new Texture(game, "background.png");
		progressbar = new Vertices(glGraphics, 12, 0, true, false);
		
// Собственно сама загрузка основных частей		
		Assets.background = new Texture(game, "background2.png", true);
		Assets.backgroundRegion = new TextureRegion(Assets.background, 0, 0, 480, 320);
		Assets.items = new Texture(game, "items.png", true);
		Assets.logoRegion = new TextureRegion(Assets.items, 0, 256, 384, 128);
		Assets.menuRegion = new TextureRegion(Assets.items, 0, 128, 225, 64);
		Assets.gameOverRegion = new TextureRegion(Assets.items, 225, 128, 225, 100); 
		Assets.pauseRegion = new TextureRegion(Assets.items, 0, 192, 160, 64);
		Assets.settingsRegion = new TextureRegion(Assets.items, 0, 160, 224, 32);
		Assets.touchRegion = new TextureRegion(Assets.items, 0, 384, 64, 64);
		Assets.accelRegion = new TextureRegion(Assets.items, 64, 384, 64, 64);
		Assets.touchEnabledRegion = new TextureRegion(Assets.items, 0, 448, 64, 64);
		Assets.accelEnabledRegion = new TextureRegion(Assets.items, 64, 448, 64, 64);
		Assets.soundRegion = new TextureRegion(Assets.items, 128, 384, 64, 64);
		Assets.soundEnabledRegion = new TextureRegion(Assets.items, 128, 384+64, 64, 64);
		Assets.leftRegion = new TextureRegion(Assets.items, 0, 0, 64, 64);
		Assets.rightRegion = new TextureRegion(Assets.items, 64, 0, 64, 64);
		Assets.fireRegion = new TextureRegion(Assets.items, 128, 0, 64, 64);
		Assets.pauseButtonRegion = new TextureRegion(Assets.items, 0, 64, 64, 64);
		Assets.font = new Font(Assets.items, 224, 0, 16, 16, 20);

		Assets.explosionTexture = new Texture(game, "explode.png", true);
		TextureRegion[] keyFrames = new TextureRegion[16];
		int frame = 0;
		for (int y = 0; y < 256; y += 64) {
			for (int x = 0; x < 256; x += 64) {
				keyFrames[frame++] = new TextureRegion(Assets.explosionTexture, x,y, 64, 64);
			}
		}
		Assets.explosionAnim = new Animation(0.1f, keyFrames);

		//------------------------------------------------------

		Assets.sunAnim = new AnimationGIF(game,"sun.gif",0.2f);
		
		Assets.model = new Model3D[5];
		 
		Assets.model[0] = new Model3D(); // модель истребителя
		Thread myThready = new Thread(new OBJLoadRunnable(this,glGame,"istrebitel_mini.obj", Assets.model[0])); // загрузка в эту модель данных
		myThready.start();
		Assets.model[1] = new Model3D(); // модель захватчика
		Thread myThready1 = new Thread(new OBJLoadRunnable(this,glGame,"invader.obj", Assets.model[1])); // загрузка в эту модель данных
		myThready1.start();
		Assets.model[2] = new Model3D(); // модель щита
		Thread myThready2 = new Thread(new OBJLoadRunnable(this,glGame,"shild.obj", Assets.model[2])); // загрузка в эту модель данных
		myThready2.start();
		Assets.model[3] = new Model3D(); // модель пули
		Thread myThready3 = new Thread(new OBJLoadRunnable(this,glGame,"shot.obj", Assets.model[3])); // загрузка в эту модель данных
		myThready3.start();
		Assets.model[4] = new Model3D(); // модель астероида
		Thread myThready4 = new Thread(new OBJLoadRunnable(this,glGame,"asteroid_model.obj", Assets.model[4])); // загрузка в эту модель данных
		myThready4.start();
		
		
		
		
		Assets.music = game.getAudio().newMusic("music.mp3");
		Assets.music.setLooping(true);
		
		Assets.music.setVolume(0.5f);
		if (Settings.soundEnabled)	Assets.music.play();
		Assets.clickSound = game.getAudio().newSound("click.ogg");
		Assets.explosionSound = game.getAudio().newSound("explosion.ogg");
		Assets.shotSound = game.getAudio().newSound("shot.ogg");
	}
	public static synchronized void IncProgress(int value){
		loadprogress = loadprogress + value;
	} 
	
	public void ProgressNull() 
	{
		maxprogress = 0;
	}
	public static synchronized void IncMaxProgress(int value){
		maxprogress = maxprogress + value;
	} 
	/**Класс потоковой загрузки*/
	private class OBJLoadRunnable implements Runnable{
		LoadingData load;
		GLGame game;
		Model3D model;
		String filename;
		public OBJLoadRunnable(LoadingData load,GLGame game,String filename,Model3D model){
			this.load = load;
			this.game = game;
			this.model = model;
			this.filename = filename;
		}
		
		public int getIndex(String index, int size) {
			if (index.equals("")) return -1;
				int idx = Integer.parseInt(index);
			if (idx < 0)
				return size + idx;
			else
				return idx-1;
		}
		public List<String> readLines(InputStream in) throws IOException {
			List<String> lines = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new
			InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null)
				lines.add(line);
			return lines;
		}
		
		@Override
		public void run() {
			load.state = LoadState.Load;
			
			loadThread++;
			InputStream in = null;
			List<String> lines = null;

			try {
				in = game.getFileIO().readAsset(filename);
				lines = readLines(in);
			} catch (Exception ex) {
				throw new RuntimeException("couldn't load file", ex);
			}
			
			float[] vertices = new float[lines.size() * 3];
			float[] normals = new float[lines.size() * 3];
			float[] uv = new float[lines.size() * 2];
			int[] facesVerts = new int[lines.size() * 2];
			int[] facesNormals = new int[lines.size() * 2];
			int[] facesUV = new int[lines.size() * 2];

			int numVertices = 0;
			int numNormals = 0;
			int numUV = 0;
			int numFaces = 0;
			int vertexIndex=0;
			int normalIndex=0;
			int uvIndex=0;
			int faceIndex=0;
			int format=3;
			int item=0;
			int last_numFaces=0;
			int last_numNormals=0;
			int last_numUV=0;
			String newObject = null;
			
			IncMaxProgress(lines.size()); 
			for (int i = 0; i < lines.size(); i++) {
				
					IncProgress(1);
				
					String line = lines.get(i);
					
					if (line.startsWith("v ")) {
						String[] tokens = line.split("[ ]+");
						vertices[vertexIndex] = Float.parseFloat(tokens[1]);
						vertices[vertexIndex + 1] = Float.parseFloat(tokens[2]);
						vertices[vertexIndex + 2] = Float.parseFloat(tokens[3]);
						vertexIndex += 3;
						numVertices++;
						continue;
					}
				
					if (line.startsWith("vn ")) {
						String[] tokens = line.split("[ ]+");
						normals[normalIndex] = Float.parseFloat(tokens[1]);
						normals[normalIndex + 1] = Float.parseFloat(tokens[2]);
						normals[normalIndex + 2] = Float.parseFloat(tokens[3]);
						normalIndex += 3;
						numNormals++;
						continue;
					}
					if (line.startsWith("vt")) {
						String[] tokens = line.split("[ ]+");
						uv[uvIndex] = Float.parseFloat(tokens[1]);
						uv[uvIndex + 1] = Float.parseFloat(tokens[2]);
						uvIndex += 2;
						numUV++;
						continue;
					}
					if (line.startsWith("usemtl")) {
						String[] tokens = line.split("[ ]+");
						model.items.get(item-1).usemtl = tokens[1]; 
						continue;
					}
					if (line.startsWith("mtllib")) {
						String[] tokens = line.split("[ ]+");
						model.mtllib = tokens[1];
						continue;
					}
					if (line.startsWith("f ")) {
						String[] tokens = line.split("[ ]+");
						int temp;
						String[] parts = tokens[1].split("/");
						facesVerts[faceIndex] = getIndex(parts[0], numVertices);
						if (parts.length > 2)	{
							temp = getIndex(parts[2],numNormals);if (temp >=0) facesNormals[faceIndex] = temp;
						}
						if (parts.length > 1)	{
							temp = getIndex(parts[1],numNormals);if (temp >=0) facesUV[faceIndex] = temp;
						}
						faceIndex++;
						parts = tokens[2].split("/");
						
						temp = getIndex(parts[0], numVertices); if (temp >=0) facesVerts[faceIndex] = temp;
						if (parts.length > 2)	{
							temp = getIndex(parts[2],numNormals);if (temp >=0) facesNormals[faceIndex] = temp;
						}
						if (parts.length > 1)	{
							temp = getIndex(parts[1],numNormals);if (temp >=0) facesUV[faceIndex] = temp;
						}
						faceIndex++;
						parts = tokens[3].split("/");
						temp = getIndex(parts[0], numVertices); if (temp >=0) facesVerts[faceIndex] = temp;
						if (parts.length > 2)	{
							temp = getIndex(parts[2],numNormals);if (temp >=0) facesNormals[faceIndex] = temp;
						}
						if (parts.length > 1)	{
							temp = getIndex(parts[1],numNormals);if (temp >=0) facesUV[faceIndex] = temp; 
						}
						faceIndex++;
						if (tokens.length > 4) { // ВОТ эта поебота не пашет потому что если 4 пачки f то это полигон а не треугольник
							format = 4;
							parts = tokens[4].split("/");
							temp = getIndex(parts[0], numVertices); if (temp >=0) facesVerts[faceIndex] = temp;
							if (parts.length > 2)	{
								temp = getIndex(parts[2],numNormals);if (temp >=0) facesNormals[faceIndex] = temp;
							}
							if (parts.length > 1)	{
								temp = getIndex(parts[1],numNormals);if (temp >=0) facesUV[faceIndex] = temp; 
							} else {format=3;}
							faceIndex++;
						}
						numFaces++;
						if (i!=lines.size()-1) continue;
						}
					
						if (line.startsWith("#")) {
							String[] tokens = line.split("[ ]+");
							if (tokens.length > 1)	if (tokens[1].equals("object")) {newObject = tokens[2];}
						}
						
					
						if (line.startsWith("o") || i==lines.size()-1 || newObject != null) {
							if (newObject != null){
								model.items.add(new ItemsModel3D());
								model.items.get(item).name = newObject;
								newObject = null;
							} else {
							if (i != lines.size()-1){
								String[] tokens = line.split("[ ]+");
								model.items.add(new ItemsModel3D());
								model.items.get(item).name = tokens[1];
								
							}}
							if (item>0) {
								int vi=0;
								int a=((numFaces-last_numFaces) * format) * (3 + ((numNormals - last_numNormals) > 0 ? 3 : 0) + ((numUV-last_numUV) > 0 ? 2 : 0));
								float[] vertss = new float[a];
								
							// Формируем массив координат вершин, координат текстур (относительно номеров вершин), нормали
								for (int l = last_numFaces*format; l < numFaces * format; l++) {
									int vertexIdx = facesVerts[l] * 3;
									vertss[vi++] = vertices[vertexIdx];
									vertss[vi++] = vertices[vertexIdx + 1];
									vertss[vi++] = vertices[vertexIdx + 2];
									if (numUV-last_numUV > 0) {
										int uvIdx = facesUV[l] * 2;
										vertss[vi++] = uv[uvIdx];
										vertss[vi++] = 1 - uv[uvIdx + 1];
								}
									if (numNormals-last_numNormals > 0) {
										int normalIdx = facesNormals[l] * 3;
										vertss[vi++] = normals[normalIdx];
										vertss[vi++] = normals[normalIdx + 1];
										vertss[vi++] = normals[normalIdx + 2];
									}
								}
								model.items.get(item-1).vertices = new Vertices3(game.getGLGraphics(), (numFaces-last_numFaces) *format, 0, false, (numUV-last_numUV) > 0, (numNormals-last_numNormals )> 0);
								model.items.get(item-1).vertices.setVertices(vertss, 0, vertss.length);
							}
							item++;
							last_numFaces = numFaces;
							last_numUV = numUV;
							last_numNormals = numNormals;
							continue;
						}
					}
				
//------------------------------------------
// загрузка материалов и текстур
					if (model.mtllib != null) {
					List<String> lines2 = null;
					load.state = LoadState.LoadMaterial;
					try {
						in = game.getFileIO().readAsset(model.mtllib);
						lines2 = readLines(in);
					} catch (Exception ex) {
						throw new RuntimeException("couldn't load file", ex);
					}
					
					
					IncMaxProgress(lines2.size());
					
					ArrayList <Material> mat = new ArrayList<Material>();
					ArrayList <String> matName = new ArrayList<String>();
					
					int nextMat=0;
					for (int i = 0; i < lines2.size(); i++) {
							IncProgress(1);
							
							String line = lines2.get(i);
							
							if (line.startsWith("map_Ka ")) {
								String[] tokens = line.split("[ ]+");
								mat.get(nextMat-1).ambient_texture = new Texture(game,tokens[1],true);
								continue;
							}
							if (line.startsWith("map_Kd ")) {
								String[] tokens = line.split("[ ]+");
								mat.get(nextMat-1).diffuse_texture = new Texture(game,tokens[1],true);
								continue;
							}
							if (line.startsWith("map_Ks ")) {
								String[] tokens = line.split("[ ]+");
								mat.get(nextMat-1).specular_texture = new Texture(game,tokens[1],true);
								continue;
							}
							
							if (line.startsWith("newmtl ")) {
								String[] tokens = line.split("[ ]+");
								mat.add(new Material());
								matName.add(new String(tokens[1]));
								nextMat++;
								continue;
							}

							
							if (line.startsWith("Ka ")) {
								String[] tokens = line.split("[ ]+");
								mat.get(nextMat-1).setAmbient(Float.parseFloat(tokens[1]),
														   Float.parseFloat(tokens[2]), 
														   Float.parseFloat(tokens[3]),1);
								continue;
							}
							if (line.startsWith("Kd ")) {
								String[] tokens = line.split("[ ]+");
								mat.get(nextMat-1).setDiffuse(Float.parseFloat(tokens[1]),
										   				   Float.parseFloat(tokens[2]), 
										   				   Float.parseFloat(tokens[3]),1);
								continue;
							}
							if (line.startsWith("Ks ")) {
								String[] tokens = line.split("[ ]+");
								mat.get(nextMat-1).setSpecular(Float.parseFloat(tokens[1]),
										   					Float.parseFloat(tokens[2]), 
										   					Float.parseFloat(tokens[3]),1);
								continue;
							}
					}
					
					
					IncMaxProgress(model.items.size());
					
					for (int i = 0;i < model.items.size(); i++){ 
						IncProgress(1);
						for (int l = 0;l < matName.size(); l++) {
							if (model.items.get(i).usemtl.equals(matName.get(l))){
								model.items.get(i).material = new Material();
								model.items.get(i).material.setAmbient(mat.get(l).ambient[0], 
										   							   mat.get(l).ambient[1], 
										   							   mat.get(l).ambient[2],
										   							   mat.get(l).ambient[3]);
								model.items.get(i).material.setDiffuse(mat.get(l).diffuse[0], 
										   							   mat.get(l).diffuse[1], 
										   							   mat.get(l).diffuse[2],
										   							   mat.get(l).diffuse[3]);
								model.items.get(i).material.setSpecular(mat.get(l).specular[0], 
										   							   mat.get(l).specular[1], 
										   							   mat.get(l).specular[2],
										   							   mat.get(l).specular[3]);
								if (mat.get(l).ambient_texture != null) model.items.get(i).material.ambient_texture = mat.get(l).ambient_texture;
								if (mat.get(l).diffuse_texture != null) model.items.get(i).material.diffuse_texture = mat.get(l).diffuse_texture;
								if (mat.get(l).specular_texture != null) model.items.get(i).material.specular_texture = mat.get(l).specular_texture;
								break;
							}
						}
					}
					
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					}
//-------------------------------------------
					loadThread--;
					load.state = LoadState.LoadComplete;
		}
		
	}
	@Override
	public void update(float deltaTime) {
		if (state==LoadState.LoadComplete) 	
			if (loadThread <=0) {
				float a;
				a = (float)((float)loadprogress/(float)maxprogress)*100;
				if (a>99) game.setScreen(new MainMenuScreen(glGame));
			}
	}
			
	
	@Override
	public void present(float deltaTime) {
		float a;
		a = (float)((float)loadprogress/(float)maxprogress)*100;
		Log.d("LEGANAS Поток прорисовки","progress = " + a);
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		batcher.beginBatch(load_background);
		batcher.drawSprite(50, 3,100, 6,new TextureRegion(load_background, 0, 0, 480, 320));
		batcher.endBatch();
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glTranslatef(0, -4, 0);
		vertices[1*6] = a;
		vertices[4*6] = a;
		vertices[5*6] = a;
		progressbar.setVertices(vertices,0,(6*2)+(6*4));
		progressbar.bind();
		progressbar.draw(GL10.GL_TRIANGLES, 0, 6);
		progressbar.unbind();		
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		cam.setViewportAndMatrices();
		batcher.beginBatch(Assets.items);
		Assets.font.drawText(batcher, Integer.toString((int)a)+"%", 480/2, 320/2);
		
		if ((state==LoadState.LoadMaterial) || (loadThread ==1)){
			Assets.font.drawText(batcher,"Loading Texture" , (480/2)-100, (320/2)+25);
		} else {
			Assets.font.drawText(batcher,"Loading 3D model" , (480/2)-100, (320/2)+25);
		}
		batcher.endBatch();

		gl.glDisable(GL10.GL_BLEND);
		
	}
	@Override
	public void pause() {
		// TODO Автоматически созданная заглушка метода
		
	}
	@Override
	public void resume() {
		// TODO Автоматически созданная заглушка метода
		
	}
	@Override
	public void dispose() {
		// TODO Автоматически созданная заглушка метода
		
	}

}