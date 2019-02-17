package Parsing;

public class Star {
	private String name;
	private int dateOfBirth;
	
	public Star() {
		
	}
	
	public Star(String name, int dob) {
		this.name=name;
		this.dateOfBirth=dob;
	}
	public void setName(String name) {
		this.name=name;
	}
	public void setDob(int dob) {
		this.dateOfBirth=dob;
	}
	public String getName() {
		return this.name;
	}
	public int getDob() {
		return this.dateOfBirth;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Star Details - ");
		sb.append("Name:" + getName());
		sb.append(",");
		sb.append("date of birt:"+getDob());
		sb.append(".");
		
		return sb.toString();
	}

	

}
