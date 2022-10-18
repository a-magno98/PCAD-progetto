package shared;

public class User implements UserInt {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String surname;
	
	public User (String id, String name, String surname) {
		this.id = id;
		this.name = name;
		this.surname = surname;
	} 
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getSurname() {
		return surname;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setSurname(String surname) {
		this.surname = surname;
	}

}
