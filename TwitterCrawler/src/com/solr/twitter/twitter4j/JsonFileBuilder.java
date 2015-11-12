package com.solr.twitter.twitter4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonFileBuilder {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// JsonFactory factory = new JsonFactory();
		JSONParser parser = null;
		BufferedWriter bos = null;

		try {
			File dir = new File("backup_files/");
			FilenameFilter nameFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.endsWith("09_11_2015.json")) {
						return true;
					} else {
						return false;
					}
				}
			};
			File[] files = dir.listFiles(nameFilter);
			for (File file : files) {
				System.out.println("Reading File: " + file.getName());
				parser = new JSONParser();
				Object obj = parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				// JSONObject jsonObject = (JSONObject) obj;
				JSONArray array = (JSONArray) obj;
				JSONArray jsonArray = new JSONArray();
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					if (object.containsKey("lang")) {
						if (object.get("lang").toString().equalsIgnoreCase("en")) {
							object.put("text_en", object.get("text").toString());
							object.put("text_de", "");
							object.put("text_ru", "");
						} else if (object.get("lang").toString().equalsIgnoreCase("de")) {
							object.put("text_de", object.get("text").toString());
							object.put("text_en", "");
							object.put("text_ru", "");
						} else if (object.get("lang").toString().equalsIgnoreCase("ru")) {
							object.put("text_ru", object.get("text").toString());
							object.put("text_de", "");
							object.put("text_en", "");
						}
					}
					jsonArray.add(object);
				}
				bos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
						new File("working_data/" + (file.getName().split(".json"))[0] + "_v2" + ".json"), false),
						"UTF-8"));
				System.out.println("writing to file: " + (file.getName().split(".json"))[0] + "_v2" + ".json");
				bos.write(jsonArray.toString());
				bos.flush();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
