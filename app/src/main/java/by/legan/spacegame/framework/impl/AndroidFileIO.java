package by.legan.spacegame.framework.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import by.legan.spacegame.framework.FileIO;

import android.content.res.AssetManager;
import android.os.Environment;

/**Класс реализующий файловый ввод/вывод*/
public class AndroidFileIO implements FileIO {
	AssetManager assets;
	String externalStoragePath;
	public AndroidFileIO(AssetManager assets) {
		this.assets = assets;
		this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	/**Открывает поток ввода для чтения файла с папки ресурсов Assets
	 * @param fileName - имя файла папке ресурсов Assets*/
	@Override
	public InputStream readAsset(String fileName) throws IOException {
		return assets.open(fileName);
	}
	
	/**Открывает поток ввода для чтения файла с внешней SDCard
	 * @param fileName - имя файла на внешней SDCard*/
	@Override
	public InputStream readFile(String fileName) throws IOException {
		return new BufferedInputStream(new FileInputStream(externalStoragePath + fileName));
	}
	
	/**Открывает поток вывода для записи файла на внешнюю SDCard
	 * @param fileName - имя файла на внешней SDCard*/
	@Override
	public OutputStream writeFile(String fileName) throws IOException {
		return new FileOutputStream(externalStoragePath + fileName);
	}
}