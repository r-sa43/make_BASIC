package newlang5;

public class Variable {
	String var_name;
	Value value;

	public Variable(String name) {
		var_name = name;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Value getValue() {
		return value;
	}

	public String getName() {
		return var_name;
	}

}
