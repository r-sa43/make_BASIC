package newlang4;

public class ProgramNode extends Node {
	Node body;

	public ProgramNode(Environment env) {
		super(env);
	}

	@Override
	public void parse() throws Exception {
		body = handle(Symbol.stmt_list);
	}
}
