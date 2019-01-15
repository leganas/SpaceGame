package by.legan.spacegame.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import by.legan.spacegame.framework.Audio;
import by.legan.spacegame.framework.FileIO;
import by.legan.spacegame.framework.Game;
import by.legan.spacegame.framework.Graphics;
import by.legan.spacegame.framework.Input;
import by.legan.spacegame.framework.Screen;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;


/**OpenGL ES аналог AndroidGame , только с синхронизацией
 * 1) Объединяет низкоуровневые классы для реализации приложения по концепции Open GL игры
 * 2) Реализует интерфейс Renderer (потоковая отрисовка )*/
public abstract class GLGame extends Activity implements Game, Renderer {
	/**Переменная перечисление отражающая текущее состояние игры, предназначена для синхронизации потоков 
	 * отрисовки и событий активности Андроид*/
	enum GLGameState {
		Initialized,
		Running,
		Paused,
		Finished,
		Idle
	}
	/**View, который позволит нам рисовать с помощью OpenGL ES. В Android API*/
	GLSurfaceView glView;
	/*Через glGraphics мы получаем доступ свойствам привязанного GLSurfaceView, 
	 * и доступ к объекту установки свойств OpenGL */
	GLGraphics glGraphics;
	/**Доступ к реализации аудио системы*/
	Audio audio;
	/**Доступ к средствам пользовательского ввода*/
	Input input;
	/**Доступ к файловой системе*/
	FileIO fileIO;
	/**Доступ к объекту описывающему модель экрана*/
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	/**Стартовое системное время*/
	long startTime = System.nanoTime();
	/**Объект для реализации механизма не выключения экрана и не засыпания устройства*/
	WakeLock wakeLock;
	
	/**Инициализация всех объектов игры и назначение слушателя для GLSurfaceViewЬ, 
	 * и инициализация контейнера отображения для нашей активности (наш объект GLSurfaceView)*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/**Привязываем glView экрану нашему физическому экрану 
		 * только он знает о нашем физическом разрешении экрана, у него будет спрашивать glGraphics это*/
		glView = new GLSurfaceView(this);
		/*Регистрируется слушатель интерфейса Renderer 
		 * и начинают в потоке вызываться соответствующие методы*/
		glView.setRenderer(this);
		/*Регистрируется контейнер унаследованный от View, и собственно это и есть приказ Андроид приложению 
		 * брать информацию для отображения на экране здесь*/
		setContentView(glView);
		/*Дальше инициализируем все вспомогательные классы для игры*/
		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);
		/**/
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"GLGame");
	}
	
	/*Этот метод унаследован от Activity и вызывается каждый раз при возобновлении фокуса на приложение (в том числе и 1й раз после страта)*/
	public void onResume() {
		super.onResume();
		/*Запускаем поток отрисовки который реализует назначенный слушатель glView.setRenderer(this) - то-есть этот наш класс 
		 * реализующий интерфейс Renderer */
		glView.onResume();
		/*Не даём потухнуть устройству и экрану*/
		wakeLock.acquire();
	}
	
	/**Метод реализующий интерфейс Renderer, Вызывается в потоке при создании и возобновлении игры*/
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glGraphics.setGL(gl);
		synchronized(stateChanged) {
			/*Вот тут реализуется инициализация абстрактного метода получения первого экрана отображения
			 * то-есть иными словами мы вызываем реализованный в каком то классе типа Screen метод getStartScreen() м 
			 * тем самым получаем ссылку на объект реализующий все методы для обработки представления типа ЭКРАН*/
			if(state == GLGameState.Initialized) screen = getStartScreen();
			/**Переводим режим игры в статус Запущено*/
			state = GLGameState.Running;
			/*вызываем первый раз метод resume текущего экрана, 
			 * там должен находится весь текст инициализации экрана после старта или возобновления фокуса*/
			screen.resume();
			startTime = System.nanoTime();
		}
	}
	
	/**Вызывается каждый раз, когда изменяется размер поверхности. 
	 * Мы получаем новую ширину и высоту поверхности в пикселах, а также экземпляр класса GL10, 
	 * если хотим дать OpenGL ES какие-либо команды*/
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
	}
	
	/**Метод реализующий интерфейс Renderer, В методе делается большая часть работы. Он вызывается потоком визуализации 
	 * максимально часто. В нем проверяем состояние нашей игры на данный момент и реагируем в соответствии с этим. 
	 * Поскольку состояние может быть изменено в методе onPause в потоке пользовательского интерфейса, необходимо 
	 * синхронизировать доступ к нему.*/
	@Override
	public void onDrawFrame(GL10 gl) {
		GLGameState state = null;
		/*Получаем текущий статус переменной синхронизации 
		 * страхуем себя через synchronized от смены статуса в этот промежуток времени*/
		synchronized(stateChanged) {
			state = this.state;
		}

		/*Отрабатываем как можно чаще в зависимости от текущего режима*/
		if(state == GLGameState.Running) {
			float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			/*Делаем так чтобы отрабатывались в потоке наши методы обновления экрана и его отрисовки*/
			screen.update(deltaTime);
			screen.present(deltaTime);
		}
		if(state == GLGameState.Paused) {
			screen.pause();
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
		if(state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();
			synchronized(stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
	}
	
	/**Метод класса Activity Вызывается системой Андроид при постановке активности на паузу*/
	@Override
	public void onPause() {
		synchronized(stateChanged) {
			if(isFinishing())
				state = GLGameState.Finished;
			else
				state = GLGameState.Paused;
			while(true) {
				try {
					stateChanged.wait();
					break;
				} catch(InterruptedException e) {
				}
			}
		}
		wakeLock.release();
		glView.onPause();
		super.onPause();
	}
	
	/**Получаем ссылку на текущий объект доступа к интерфейсу отрисовки GLGraphics*/
	public GLGraphics getGLGraphics() {
		return glGraphics;
	}
	
	@Override
	public Input getInput() {
		return input;
	}
	@Override
	public FileIO getFileIO() {
		return fileIO;
	}
	/**Мы не можем реализовать этот метод унаследованного интерфейса Game 
	 * т.к. мы работаем с графикой OpenGL и теперь нужно использовать getGLGraphics*/
	@Override
	public Graphics getGraphics() {
		throw new IllegalStateException("We are using OpenGL!");
	}
	@Override
	public Audio getAudio() {
		return audio;
	}
	
	@Override
	public void setScreen(Screen screen) {
		/*Ну короче если прислали указатель на пустоту то это беда и мы генерируем ошибку*/
		if (screen == null) throw new IllegalArgumentException("Screen must not be null");
		/*Вызываем методы паузы и удаления текущего экрана*/
		this.screen.pause();
		this.screen.dispose();
		/*Вызываем методы возобновления и обновления состояния нового экрана*/
		screen.resume();
		screen.update(0);
		/*Устанавливаем полученный экран в качестве текущего*/
		this.screen = screen;
		}
	@Override
	public Screen getCurrentScreen() {
		return screen;
	}
}
	
