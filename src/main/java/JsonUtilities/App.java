package JsonUtilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		try {
			BuildCustomList();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static void BuildCustomList() throws FileNotFoundException {

		String basePath = null, customReader = null, fhirReader = null, mapReader = null;

		basePath = "/home/shankarganesh.j/mirth/mirth-fhir-utility/src/main/java/JsonUtilities/JsonFiles/";

		customReader = readFile(basePath + "customList.json");
		fhirReader = readFile(basePath + "fhirR4List.json");
		mapReader = readFile(basePath + "mapper.json");

		Object customJson = JsonPath.parse(customReader).json();
		Object fhirJson = JsonPath.parse(fhirReader).json();
		Object mapJson = JsonPath.parse(mapReader).json();

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.list");
		Set listSet = mapObj.entrySet();
		Iterator listIterate = listSet.iterator();
		String totalFhir = null, totalCustom = null, mapperFhir = null, mapperCustom = null;
		JSONArray feildList = null;
		while (listIterate.hasNext()) {
			Map.Entry list = (Map.Entry) listIterate.next();
			switch (list.getKey().toString()) {
			case "length":
				LinkedHashMap<String, String> listTotal = (LinkedHashMap<String, String>) list.getValue();
				totalFhir = convertToString(listTotal.keySet().toString(), 1);
				totalCustom = convertToString(listTotal.values().toString(), 1);
				break;
			case "mapperObj":
				LinkedHashMap<String, String> listMapper = (LinkedHashMap<String, String>) list.getValue();
				mapperFhir = convertToString(listMapper.keySet().toString(), 1);
				mapperCustom = convertToString(listMapper.values().toString(), 1);
				break;
			case "fieldsList":
				feildList = (JSONArray) list.getValue();
				break;
			}
		}
		System.out.println(mapperCustom);
		Integer totalFhirCount = JsonPath.parse(fhirJson).read(totalFhir);
		for (Integer f = 0; f < totalFhirCount; f++) {
			for (Integer i = 0; i < feildList.size(); i++) {
				LinkedHashMap<Object, Object> fields = (LinkedHashMap<Object, Object>) feildList.get(i);
				//System.out.println(fields.keySet());
				//System.out.println(fields.values());
				//System.out.println(fields.get("Value"));
				//System.out.print(f);
				//System.out.println("-" + i);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void CreateFhir() throws FileNotFoundException {

		String basePath = "/home/shankarganesh.j/mirth/mirth-fhir-utility/src/main/java/JsonUtilities/JsonFiles/";

		String customReader = readFile(basePath + "custom.json");
		String fhirReader = readFile(basePath + "fhirR4.json");
		String mapReader = readFile(basePath + "mapper.json");

		Object customJson = JsonPath.parse(customReader).json();
		Object fhirJson = JsonPath.parse(fhirReader).json();
		Object mapJson = JsonPath.parse(mapReader).json();
		DocumentContext fhir = JsonPath.parse(fhirJson);

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.write");
		Set writeSet = mapObj.entrySet();
		Iterator writeIterate = writeSet.iterator();
		while (writeIterate.hasNext()) {
			Map.Entry write = (Map.Entry) writeIterate.next();
			fhir = setJson(fhir, customJson, fhirJson, write.getKey().toString(), write.getValue().toString());
		}

		System.out.println(fhir.jsonString());
	}

	public static DocumentContext setJson(DocumentContext fhir, Object customJson, Object fhirJson, String key,
			String val) {
		switch (typeOf(JsonPath.read(fhirJson, key))) {
		case "String":
			fhir = fhir.set(key, convertToString(JsonPath.read(customJson, val), 2));
			break;
		default:
			fhir = fhir.set(key, JsonPath.read(customJson, val));
		}
		return fhir;
	}

	public static String readFile(String filename) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String convertToString(Object object, Integer level) {
		String t = object.toString();
		Integer i;
		for (i = 1; i <= level; i++) {
			t = t.substring(1, t.length() - 1);
		}
		return t;
	}

	public static <T> String typeOf(T o) {
		System.out.println("Instance Type >> " + o.getClass().getSimpleName());
		return o.getClass().getSimpleName();
	}
}
