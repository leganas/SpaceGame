package by.legan.spacegame.framework.gl;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**Класс описывающий 3D модель объекта или целой сцены и его атрибуты*/
public class Model3D{
/**Набор координат вершин,их цветов, координат текстур, нормалей всей модели*/
	 public Vertices3 vertices; 
//Все остальные параметры не обязательные и могут отсутствовать (особенно вся модель загружается как один объект)
/** имя файла описания материала всех элементов модели*/
	 public String mtllib; 
/**Массив описывающий каждый элемент модели в отдельности*/	 
	 public ArrayList <ItemsModel3D> items = new ArrayList<ItemsModel3D>();

/*динамические массивы

Динамические массивы реализованы на уровне параметризованных классов: Vector и ArrayList. Однако в качестве элементов простые типы не могут выступать, только объектные типы. Для управления элементами эти классы используют методы интерфейсов Collection и List:

    add(E o) - добавление элемента в конец;
    add(int index, E element) - вставка элемента в указанную позицию;
    remove(int index) - удаление элемента в указанной позиции;
    remove(Object o) - удаление первого вхождения объекта в списке;
    clear() - удаление всех элементов;
    isEmpty() - определяет, содержит ли список элементы;
    size() - число элементов;
    set(int index, E element) - заменить элемент в указанной позиции новым;
    get(int index) - получить элемент по указанному индексу;
    contains(Object o) - определение, содержится ли указанный объект в списке элементов;
    lastIndexOf(Object o) - поиск последнего вхождения элемента, возвращается индекс элемента или -1;
    indexOf(Object o) - поиск первого вхождения элемента, возвращается индекс элемента или -1;
    toArray() - возвращает копию в виде статического массива;
    toArray(T[] a) - сохраняет элементы в указанный массив.
*/
	 public void Draw(GL10 gl){
		 Vertices3 item;
		 Material mat;
		 Texture texture=null;
		 for (int i=0;i<items.size();i++){
			item=items.get(i).vertices;
			texture=null; 
			if (items.get(i).material != null) {
				mat = items.get(i).material;
				mat.enable(gl);
			}
			// как использовать остальные текстуры я в рот не ебу 
			if (items.get(i).material.ambient_texture !=null) texture = items.get(i).material.ambient_texture;
//			if (items.get(i).material.diffuse_texture !=null) texture = items.get(i).material.diffuse_texture;
//			if (items.get(i).material.specular_texture !=null) texture = items.get(i).material.specular_texture;
			if (texture != null) {
				gl.glEnable(GL10.GL_TEXTURE_2D);
				if (texture.textureId ==0) texture.reload();
				texture.bind();
			} else  gl.glDisable(GL10.GL_TEXTURE_2D);
			item.bind();
		 	item.draw(GL10.GL_TRIANGLES, 0,item.getNumVertices());
		 	item.unbind();
		 }
	 }
	 
	 /**Перезагружает текстуры после потери контекста*/
	 public void reload(){
		for (int a=0;a<items.size();a++){
			if (items.get(a).material.ambient_texture != null) items.get(a).material.ambient_texture.reload();
			if (items.get(a).material.diffuse_texture != null) items.get(a).material.diffuse_texture.reload();
			if (items.get(a).material.specular_texture != null) items.get(a).material.specular_texture.reload();
		}
	 }
}