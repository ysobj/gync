package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.List;

public class MethodRef extends Constant {
	ClassConstant clazz;
	NameAndType nat;

	public MethodRef(ClassConstant clazz, NameAndType nat) {
		this.clazz = clazz;
		this.nat = nat;
	}

	@Override
	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add(CompilerConstants.CONSTANT_Methodref);
		list.add((byte) (this.clazz.getIndex() >> 8));
		list.add(this.clazz.getIndex().byteValue());
		list.add((byte) (this.nat.getIndex() >> 8));
		list.add(this.nat.getIndex().byteValue());
		return list.toArray(new Byte[0]);
	}
}