package by.legan.spacegame.StarDefender;

import by.legan.spacegame.framework.DynamicGameObject3D;

import java.util.Random;

public class Asteroid extends DynamicGameObject3D {
	static final float ASTEROID_RADIUS = 1f;
	int dRotatef_x = 45;
	int dRotatef_y = 45;
	int dRotatef_z = 45;

	int Rotatef_x = 0;
	int Rotatef_y = 0;
	int Rotatef_z = 0;

	public Asteroid(float x, float y, float z) {
		super(x, y, z, ASTEROID_RADIUS);
		Random rnd = new Random(System.currentTimeMillis());
		int min = 50;
		int max = 150;
		dRotatef_x = min + rnd.nextInt(max - min + 1);
		dRotatef_y = min + rnd.nextInt(max - min + 1);
		dRotatef_z = min + rnd.nextInt(max - min + 1);
	}
	
	public void update(float deltaTime) {
		Rotatef_x += dRotatef_x*deltaTime;
		Rotatef_y += dRotatef_y*deltaTime;
		Rotatef_z += dRotatef_z*deltaTime;
	}
	
}
