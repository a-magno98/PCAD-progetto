package shared;

import java.io.Serializable;

public interface UserInt extends Serializable {
	public String getId();
	public String getName();
	public String getSurname();
	public void setId(String id);
	public void setName(String name);
	public void setSurname(String surname);
}
