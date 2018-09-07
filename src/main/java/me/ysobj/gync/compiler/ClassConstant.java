package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.List;

public class ClassConstant extends Constant {
	private UTF8Constant utf8;

	public ClassConstant(UTF8Constant utf8) {
		this.utf8 = utf8;
	}

	@Override
	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add((byte) 0x07);
		list.add((byte) (this.utf8.getIndex() >> 8));
		list.add(this.utf8.getIndex().byteValue());
		return list.toArray(new Byte[0]);
	}
}