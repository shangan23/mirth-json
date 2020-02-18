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

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws FileNotFoundException {

		String basePath = "/home/shankarganesh.j/mirth/mirth-fhir-utility/src/main/java/JsonUtilities/JsonFiles/";

		String customReader = readFile(basePath + "custom.json");
		String fhirReader = readFile(basePath + "fhirR4.json");
		String mapReader = readFile(basePath + "mapper.json");

		Object customJson = JsonPath.parse(customReader).json();
		Object fhirJson = JsonPath.parse(fhirReader).json();
		Object mapJson = JsonPath.parse(mapReader).json();
		DocumentContext fhir = JsonPath.parse(fhirJson);

		LinkedHashMap<String, String> lhm = (LinkedHashMap<String, String>) mapJson;
		Set set = lhm.entrySet();
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			Map.Entry me = (Map.Entry) iterator.next();
			fhir = setJson(fhir,customJson,fhirJson,me.getKey().toString(),me.getValue().toString());
		}
		
		//Object resultJson = JsonPath.
		System.out.println(fhir.jsonString());

		// mapJson.keySet().forEach(key -> System.out.println(key));
		// mapJson.entrySet().forEach(entry -> System.out.println(entry.getKey()));

		// typeOf(JsonPath.read(customJson,
		// "$.Data[0].Data[?(@.FieldId=='2202')].Value").getClass().getSimpleName());
		/*fhir = fhir.set("$.category", JsonPath.read(customJson, "$.Data[0].Data[?(@.FieldId=='2201')].Value"));
		if (JsonPath.read(fhirJson, "$.note[0].text") instanceof String) {
			fhir = fhir.set("$.note[0].text",
					convertToString(JsonPath.read(customJson, "$.Data[0].Data[?(@.FieldId=='2202')].Value")));
		}
		System.out.println(fhir.jsonString());*/
	}
	
	public static DocumentContext setJson(DocumentContext fhir, Object customJson, Object fhirJson, String key, String val) {
		System.out.println("Instance Type >> "+JsonPath.read(fhirJson, key).getClass().getSimpleName());
		if (JsonPath.read(fhirJson, key) instanceof String) {
			fhir = fhir.set(key,
					convertToString(JsonPath.read(customJson, val)));
		}else {
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

	private static String convertToString(Object object) {
		String t = object.toString();
		t = t.substring(1, t.length() - 1);
		t = t.substring(1, t.length() - 1);
		return t;
	}

	public static <T> String typeOf(T o) {
		System.out.println(o.getClass().getSimpleName());
		return o.getClass().getSimpleName();
	}
}
