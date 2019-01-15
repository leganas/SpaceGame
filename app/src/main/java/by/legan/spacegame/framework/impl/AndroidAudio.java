package by.legan.spacegame.framework.impl;

import java.io.IOException;

import by.legan.spacegame.framework.Audio;
import by.legan.spacegame.framework.Music;
import by.legan.spacegame.framework.Sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
/**Класс для работы с Аудио системой, объединяет в себе класс для работы с музыкой и просто звуками*/
public class AndroidAudio implements Audio {
	AssetManager assets;
	SoundPool soundPool;
	/**Работа с аудио системой
	 * @param activity - получаем ссылку на вызывающую активность*/
	public AndroidAudio(Activity activity) {
		/*Указываем обработчика регулятора громкости для нашей активности*/
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		/*Получаем доступ к ресурсам в папке внутренней Assets*/
		this.assets = activity.getAssets();
		/*Регистрируем экземпляр класса для работы со звуком*/
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}
	
	/**Загрузить новую музыку
	 * @param filename - имя файла в папке ресурсов assets*/
	@Override
	public Music newMusic(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			return new AndroidMusic(assetDescriptor);
		} catch (IOException e) {
			throw new RuntimeException("Невозможно загрузить музыку '" + filename + "'");
		}
	}
	
	/**Загружает новый звук
	 * @param filename - имя файла в папке ресурсов assets*/
	@Override
	public Sound newSound(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch (IOException e) {throw new RuntimeException("Невозможно загрузить звук '" + filename + "'");}
	}
}

