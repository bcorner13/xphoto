package xphoto.gallery;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.digest.DigestUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;

import xphoto.gallery.models.Album;
import xphoto.gallery.models.Photo;

public class MediaIndexer extends SimpleFileVisitor<Path> {
	Album album;
	ArrayList<Album> albums;
	long currentAlbumId;

	public MediaIndexer(ArrayList<Album> albums) {
		this.albums = albums;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		File albumDir = dir.toFile();
		BasicFileAttributes attr = Files.readAttributes(dir, BasicFileAttributes.class);

		album = new Album();
		album.setId(currentAlbumId++);
		album.setTitle(albumDir.getName());
		album.setCreationTime(attr.creationTime().toMillis());

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		if (album.getPhotos().size() > 0) {
			albums.add(album);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {

		final String[] types = { "jpg", "jpeg" };
		final File file = path.toFile();

		for (final String t : types) {
			if (file.getName().toLowerCase().endsWith(t)) {
				processImage(file);
			}
		}

		return FileVisitResult.CONTINUE;
	}

	public void processImage(final File file) {
		Photo photo = new Photo();
		List<Map<String, String>> tags = new ArrayList<>();
		if (Config.getParseMetaTags()) {
			try {
				final Metadata metadata = ImageMetadataReader.readMetadata(file);

				for (Directory dir : metadata.getDirectories()) {

					for (com.drew.metadata.Tag tag : dir.getTags()) {

						Map<String, String> hashTag = new HashMap<>();
						hashTag.put(tag.getTagName(), tag.getDescription());
						tags.add(hashTag);
					}
					photo.setTags(tags);
				}

			} catch (final ImageProcessingException | IOException e) {
				System.out.println(e.getMessage());
			}

		}
		photo.setFilePath(file.getAbsolutePath());
		photo.setFileName(file.getName());

		photo.setId(DigestUtils.md5Hex(file.getAbsolutePath()));

		if (Files.notExists(Paths.get("thumbnails/" + photo.getId() + "_thumb.jpg"))) {
			try {
				makeThumbnail(photo.getId(), file.getAbsolutePath(), 224, 150);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		album.getPhotos().add(photo);
		System.out.println(file.getAbsolutePath());

	}
	
	public static void makeThumbnail(String UUID, String image, int WIDTH, int HEIGHT) throws IOException {

		final long start = System.currentTimeMillis();
		ImageIcon originalImage = new ImageIcon(image);

		BufferedImage thumbnail = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = thumbnail.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);

		g.drawImage(originalImage.getImage(), 0, 0, WIDTH, HEIGHT, null);
		g.dispose();

		File output = new File("thumbnails/" + UUID + "_thumb.jpg");
		ImageIO.write(thumbnail, "JPG", output);
		System.out.println((System.currentTimeMillis() - start) / 1000F);
	}
}
