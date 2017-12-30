package xphoto.gallery.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable, Comparable<Album> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5266331053582601538L;
	private List<Photo> photos = new ArrayList<>();
	private String title;
	private long creationTime;
	private long id;
	
	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public int compareTo(Album o) {
		if(getCreationTime() > o.getCreationTime()) {
			return -1;
		}
		return 1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


}
