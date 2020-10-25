package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Image {
	@Id
	private int id=-1;
	private String imagepath="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	public String toString() {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("id", id);
			json.accumulate("imagePath", imagepath);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}
	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		try {
			json.accumulate("id", id);
			json.accumulate("imagePath", imagepath);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}
