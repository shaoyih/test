package Parsing;

public class Star {
	private String name;
	private int dateOfBirth;
	private String id;
	
	public Star() {
		
	}
	
	public Star(String name, int dob) {
		this.name=name;
		this.dateOfBirth=dob;
	}
	public void setId(String id) {
		this.id=id;
	}
	public String getId() {
		return this.id;
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
	@Override
	public boolean equals(Object o) {
 
		
		if (o == null) {
			return false;
		}
 
		// this instance check
		if ((o instanceof Star)) {
			return ((Star) o).getName().equals(this.getName())&& ((Star) o).getDob()==this.getDob();
		}
 
		return false;
	}
 
	@Override
	public int hashCode() {
		return (this.getName()+Integer.toString(this.getDob())).hashCode();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Star Details - ");
		sb.append("Name:" + getName());
		sb.append(",");
		sb.append("Id:" + getId());
		sb.append(",");
		sb.append("date of birt:"+getDob());
		sb.append(".");
		
		return sb.toString();
	}

	

}
