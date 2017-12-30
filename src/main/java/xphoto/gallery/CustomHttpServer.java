package xphoto.gallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rapidoid.buffer.Buf;
import org.rapidoid.http.AbstractHttpServer;
import org.rapidoid.http.HttpStatus;
import org.rapidoid.http.HttpUtils;
import org.rapidoid.http.MediaType;
import org.rapidoid.net.abstracts.Channel;
import org.rapidoid.net.impl.RapidoidHelper;

import xphoto.gallery.models.Album;
import xphoto.gallery.models.Photo;

public class CustomHttpServer extends AbstractHttpServer {

	private static final String ROUTE_INDEX = "/";
	
	private static final String ROUTE_REINDEX = "/reindex";

	private static final byte[] ROUTE_PHOTOS_JSON = "/photos".getBytes();

	@Override
	protected HttpStatus handle(Channel ctx, Buf buf, RapidoidHelper req) {
		String path = req.path.str(buf);

		if (req.isGet.value) {
			if (path.equals(ROUTE_INDEX)) {
				
				try {
					return ok(ctx, req.isKeepAlive.value, index(), MediaType.HTML_UTF_8);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if(path.contains(ROUTE_REINDEX)) {
				try {
					Application.parsePhotos();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ok(ctx, req.isKeepAlive.value, "Reindex complete".getBytes(), MediaType.TEXT_PLAIN);
			} else if(path.contains("thumb")) {
				return ok(ctx, req.isKeepAlive.value, readThumbnail(path), MediaType.IMAGE_JPEG);
			} else if(path.contains("fullphoto")) {
				return ok(ctx, req.isKeepAlive.value, readPhoto(path), MediaType.IMAGE_JPEG); 
			} else if (matches(buf, req.path, ROUTE_PHOTOS_JSON)) {
				try {
					return serializeToJson(HttpUtils.noReq(), ctx, req.isKeepAlive.value, Application.getCachedAlbums());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return HttpStatus.NOT_FOUND;
	}

	public byte[] index() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("static/index.html");
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	public byte[] readThumbnail(String file) {

		Path path = Paths.get("thumbnails/" + file);
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] readPhoto(String id) {
		String idOnly = id.replace("/fullphoto=", "");
		Photo foundPhoto = null;
		try {
			for(Album album : Application.getCachedAlbums()) {
				for(Photo photo : album.getPhotos()) {
					if(photo.getId().equals(idOnly)) {
						foundPhoto = photo;
						break;
					}
				}
				if(foundPhoto != null) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(foundPhoto != null) {
			Path path = Paths.get(foundPhoto.getFilePath());
			try {
				return Files.readAllBytes(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}