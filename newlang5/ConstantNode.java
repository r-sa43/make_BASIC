package newlang5;

public class ConstantNode extends Node {
	Value value;

	public ConstantNode(Environment env) {
		super(env);
	}

	@Override
	public void parse() throws Exception {
		LexicalUnit elm = get();
		switch (elm.getType()) {
			case INTVAL:
			case DOUBLEVAL:
			case LITERAL:
				value = elm.getValue();
				return;
			default:
				error("syntax error");
		}
	}

	@Override
	public Value getValue() {
		return value;
	}
}
