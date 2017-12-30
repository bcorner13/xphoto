package xphoto.gallery;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import org.rapidoid.net.Server;

import xphoto.gallery.models.Album;

public class Application {

	public static void main(final String[] args) {

		if (Files.notExists(Paths.get("albums.ser"))) {
			try {
				parsePhotos();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Server server = new CustomHttpServer().listen(Config.getServerPort());

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				server.shutdown();
				System.out.println("Application Terminating ...");
			}
		});
		
	}

	public static void parsePhotos() throws IOException {
		ArrayList<Album> albums = new ArrayList<>();

		final long start = System.currentTimeMillis();
		Files.walkFileTree(Paths.get(Config.getPhotoFolder()), new MediaIndexer(albums));
		Collections.sort(albums);
		writeToCache(albums);
		System.out.println((System.currentTimeMillis() - start) / 1000F);
	}

	public static void writeToCache(Object object) throws IOException {
		FileOutputStream fos = new FileOutputStream("albums.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
	}

	public static ArrayList<Album> getCachedAlbums() throws ClassNotFoundException, IOException {
		FileInputStream fis = new FileInputStream("albums.ser");
		ObjectInputStream ois = new ObjectInputStream(fis);
		@SuppressWarnings("unchecked")
		ArrayList<Album> albums = (ArrayList<Album>) ois.readObject();
		ois.close();
		return albums;
	}


}
