package test;

public class Test {
	public static String toFixLengthString( String str, int length) {
		String format = String.format("%%%ds", length);
		return String.format(format, str).replace(" ", "0");
	}
	
	public static void main(String[] args) {
		
		String phone = Test.toFixLengthString("13926259838", 32);
		System.out.println( phone);
		
		System.out.println( Test.toFixLengthString("", 32));
	}
}
