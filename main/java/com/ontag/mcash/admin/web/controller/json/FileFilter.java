package com.ontag.mcash.admin.web.controller.json;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class FileFilter {
	
	private Short id;
	private MultipartFile file;

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
