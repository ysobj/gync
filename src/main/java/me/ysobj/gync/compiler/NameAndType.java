package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.List;

public class NameAndType extends Constant {
	UTF8Constant name;
	UTF8Constant type;

	public NameAndType(UTF8Constant name, UTF8Constant type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add(CompilerConstants.CONSTANT_NameAndType);
		list.add((byte) (this.name.getIndex() >> 8));
		list.add(this.name.getIndex().byteValue());
		list.add((byte) (this.type.getIndex() >> 8));
		list.add(this.type.getIndex().byteValue());
		return list.toArray(new Byte[0]);
	}
}