package jdes.gdeltedu;

public class DemoUDFs {

	public static void main(String[] args) {
		
		
		String state = substr2("USOR");
		
		System.out.println(state);
		

	}
	
	public static String substr2(String ActionGeo_ADM1Code) {
		if (ActionGeo_ADM1Code == "" || ActionGeo_ADM1Code == null) {
			return ActionGeo_ADM1Code;
		}
		// Trim leading white space
		ActionGeo_ADM1Code = ActionGeo_ADM1Code.trim();
		String state = ActionGeo_ADM1Code;
		
		// Validate prefix
        String prefix = ActionGeo_ADM1Code.substring(0,2);
		if (prefix.equals("US")) {
			state = ActionGeo_ADM1Code.substring(2);
		}

        return state;
	}

}
