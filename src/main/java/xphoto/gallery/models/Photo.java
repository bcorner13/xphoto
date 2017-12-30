package xphoto.gallery.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class Photo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9022240234724292756L;
	private List<Map<String, String>> tags;
	private String filePath;
	private String fileName;
	private long size;
	private String id;
	
	public List<Map<String, String>> getTags() {
		return tags;
	}

	public void setTags(List<Map<String, String>> tags) {
		this.tags = tags;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
