package by.legan.spacegame.framework.gl;

/**Часть модели (1н объект модели)*/
public class ItemsModel3D{
/**Набор координат вершин,их цветов, координат текстур, нормалей, отдельного элемента модели*/
	public Vertices3 vertices; // Набор координат вершин,их цветов, координат текстур, нормалей
/** название составной части модели (одного элемента)*/
	 public String name;
/**Используемый материал для данного элемента*/
	 public String usemtl;
/**Количество вершин и их параметров в каждом элементе модели*/
	 int countVerticesFormItems;
/**свойства описывающий материал элемента*/	 
	 public Material material;
}