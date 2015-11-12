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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonFileMerger {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// JsonFactory factory = new JsonFactory();
		JSONParser parser = null;
		BufferedWriter bos = null;

		try {
			String indexDate = "09_23_2015";
			File dir = new File("working_data/"+indexDate+"/");
			FilenameFilter nameFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.endsWith(".json")) {
						return true;
					} else {
						return false;
					}
				}
			};
			File[] files = dir.listFiles(nameFilter);
			JSONArray jsonArray = new JSONArray();
			DateFormat df = new SimpleDateFormat("MM_dd_yyyy");
			//Date toDay = Calendar.getInstance().getTime();
			File f = new File("merged_files/" + "full_index_"+indexDate + ".json");
			for (File file : files) {
				System.out.println("Reading File: " + file.getName());
				parser = new JSONParser();
				Object obj = parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				// JSONObject jsonObject = (JSONObject) obj;
				JSONArray array = (JSONArray) obj;
				System.out.println("Merging file: " + file.getName() + " to " + f.getName());
				jsonArray.addAll(array);
			}
			bos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f, false), "UTF-8"));
			bos.write(jsonArray.toString());
			bos.flush();
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
