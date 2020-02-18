package JsonUtilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;


/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws FileNotFoundException {
		
		String customReader = readFile("/home/shankarganesh.j/mirth/mirth-fhir-utility/src/main/java/JsonUtilities/JsonFiles/custom.json");
		String fhirReader = readFile("/home/shankarganesh.j/mirth/mirth-fhir-utility/src/main/java/JsonUtilities/JsonFiles/fhirR4.json");
		
		Object customJson = JsonPath.parse(customReader).json();
		Object fhirJson = JsonPath.parse(fhirReader).json();
		//String fhirToParse = fhirJson.toString();
		//List fhirField = JsonPath.read(fhirJson, "$.category");
		
		System.out.println(JsonPath.read(customJson, "$.Data[0].Data[?(@.FieldId=='2202')].Value").getClass().getSimpleName());
		DocumentContext fhir = JsonPath.parse(fhirJson);
		fhir = fhir.set("$.category", JsonPath.read(customJson, "$.Data[0].Data[?(@.FieldId=='2201')].Value"));
		if(JsonPath.read(fhirJson, "$.note[0].text") instanceof String){
			String t = JsonPath.read(customJson, "$.Data[0].Data[?(@.FieldId=='2202')].Value").toString();
			t = t.substring(1, t.length()-1);
			t= t.substring(1, t.length()-1);  
			fhir = fhir.set("$.note[0].text", t);
		}
		System.out.println(fhir.jsonString());
	    
	    /*String json = fhirAllergy();
		Object updatedJson = JsonPath.parse(json).json();
		List category = JsonPath.read(updatedJson, "$.entry[*].resource.category");
		List clinicalStatus = JsonPath.read(updatedJson, "$.entry[*].resource.clinicalStatus.coding[0].code");
		String jsonToParse = updatedJson.toString();
		// json = JsonPath.parse(jsonToParse).set("$.entry[1].resource.category",
		// "food");
		// [*][Data][0][0].InterfaceData[?FieldId=='2254'][].Value
		List ncategory = JsonPath.read(json, "$.entry[*].resource.category");
		System.out.println(category);
		System.out.println(clinicalStatus);
		System.out.println(ncategory);

		String cstm = customJson();
		Object jsonObj = JsonPath.parse(cstm).json();
		List Field = JsonPath.read(jsonObj, "$.[*].Data[0].InterfaceData[?(@.FieldId=='2254')].Value");
		System.out.println(Field);*/
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
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
}
