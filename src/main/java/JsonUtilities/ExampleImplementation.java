package JsonUtilities;

import java.io.FileNotFoundException;

public class ExampleImplementation {

	public static void main(String[] args) throws FileNotFoundException {
			GenerateCusomList gcl = new GenerateCusomList();
			gcl.BuildCustomList();
			GenerateFhirCreate gfc = new GenerateFhirCreate();
			gfc.CreateFhir();
	}
	
}
