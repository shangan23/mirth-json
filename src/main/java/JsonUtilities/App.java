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
import com.jayway.jsonpath.ReadContext;
import net.minidev.json.JSONArray;

/**
 * Hello world!
 *
 */
public class App {

	public static void main(String[] args) {
		try {
			BuildCustomList();
			//CreateFhir();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static void BuildCustomList() throws FileNotFoundException {

		String basePath = null, customReader = null, fhirReader = null, mapReader = null, sampleFhir = null,
				sampleCustom = null, totalFhir = null, totalCustom = null, mapperFhir = null, mapperCustom = null;
		Object customJson = null, fhirJson = null, mapJson = null;

		basePath = "src/main/java/JsonUtilities/JsonFiles/";

		customReader = readFile(basePath + "customList.json");
		fhirReader = readFile(basePath + "fhirR4List.json");
		mapReader = readFile(basePath + "mapper.json");

		customJson = JsonPath.parse(customReader).json();
		fhirJson = JsonPath.parse(fhirReader).json();
		mapJson = JsonPath.parse(mapReader).json();
		JSONArray feildList = null;

		LinkedHashMap<String, String> mapObj = JsonPath.parse(mapJson).read("$.list");
		Set listSet = mapObj.entrySet();
		Iterator listIterate = listSet.iterator();

		while (listIterate.hasNext()) {
			Map.Entry list = (Map.Entry) listIterate.next();
			switch (list.getKey().toString()) {
			case "length":
				LinkedHashMap<String, String> listTotal = (LinkedHashMap<String, String>) list.getValue();
				totalFhir = cleanString(listTotal.keySet().toString(), 1);
				totalCustom = cleanString(listTotal.values().toString(), 1);
				break;
			case "iterateObj":
				LinkedHashMap<String, String> listMapper = (LinkedHashMap<String, String>) list.getValue();
				mapperFhir = cleanString(listMapper.keySet().toString(), 1);
				mapperCustom = cleanString(listMapper.values().toString(), 1);
				break;
			case "sampleObj":
				LinkedHashMap<String, String> listSample = (LinkedHashMap<String, String>) list.getValue();
				sampleFhir = cleanString(listSample.keySet().toString(), 1);
				sampleCustom = cleanString(listSample.values().toString(), 1);
				break;
			}
		}
		DocumentContext cstm = JsonPath.parse(customJson);
		String sampleJson = cleanString(JsonPath.parse(customJson).read(mapperCustom), 1);
		ReadContext rCtx = JsonPath.parse(sampleJson);
		Object sampleObject = rCtx.json();
		JSONArray ja = new JSONArray();
		DocumentContext newCstm = JsonPath.parse(sampleObject);
		Integer totalFhirCount = JsonPath.parse(fhirJson).read(totalFhir);
		for (Integer f = 0; f < totalFhirCount; f++) {
			LinkedHashMap<String, String> mapFields = JsonPath.parse(mapJson).read("$.list.fieldObj");
			Set mapFieldSet = mapFields.entrySet();
			Iterator mapFieldIterate = mapFieldSet.iterator();
			
			while (mapFieldIterate.hasNext()) {
				Map.Entry mapField = (Map.Entry) mapFieldIterate.next();
				newCstm = setJson(newCstm, fhirJson, JsonPath.parse(sampleJson).json(), mapField.getKey().toString(),
						(mapField.getValue()).toString().replace("*", f.toString()));
			}
			
			ReadContext dataCtx = JsonPath.parse(newCstm.jsonString());
			JsonPath.parse(customJson).add(mapperCustom,dataCtx.json());
		}
		JsonPath.parse(customJson).delete(sampleCustom);
		System.out.println(customJson);
	}

	@SuppressWarnings("rawtypes")
	public static void CreateFhir() throws FileNotFoundException {

		String basePath = "src/main/java/JsonUtilities/JsonFiles/";;

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

	public static DocumentContext setJson(DocumentContext ctx, Object src, Object dest, String key, String val) {
		try {
			switch (typeOf(JsonPath.read(dest, key))) {
			case "String":
				ctx = ctx.set(key, cleanString(JsonPath.read(src, val), 2));
				break;
			default:
				ctx = ctx.set(key, JsonPath.read(src, val));
			}
		} catch (Exception e) {
			ctx = ctx.set(key, "");
		}
		return ctx;
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

	private static String cleanString(Object object, Integer level) {
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
