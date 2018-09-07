package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.List;

public class StringConstant extends Constant {
	private UTF8Constant utf8;

	public StringConstant(UTF8Constant utf8) {
		this.utf8 = utf8;
	}

	@Override
	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add(CompilerConstants.CONSTANT_String);
		list.add((byte) 0x00);
		list.add(this.utf8.getIndex().byteValue());
		return list.toArray(new Byte[0]);
	}
}