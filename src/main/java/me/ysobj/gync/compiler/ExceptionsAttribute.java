package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.List;

public class ExceptionsAttribute extends AttributeInfo {
	List<ClassConstant> exceptions = new ArrayList<>();

	public ExceptionsAttribute(UTF8Constant utf8) {
		super(utf8, null);
	}

	public void addException(ClassConstant clazz) {
		this.exceptions.add(clazz);
	}

	public Byte[] toByteArray() {
		int length = 2 + 2 * (this.exceptions.size());
		Byte[] tmp = new Byte[6 + length];
		tmp[0] = (byte) (this.utf8.getIndex().intValue() >> 8);
		tmp[1] = (byte) (this.utf8.getIndex().intValue());
		tmp[2] = (byte) (length >> 24);
		tmp[3] = (byte) (length >> 16);
		tmp[4] = (byte) (length >> 8);
		tmp[5] = (byte) (length);
		tmp[6] = (byte) (this.exceptions.size() >> 8);
		tmp[7] = (byte) (this.exceptions.size());
		int index = 8;
		for (ClassConstant classConstant : exceptions) {
			tmp[index++] = (byte) (classConstant.getIndex().intValue() >> 8);
			tmp[index++] = (byte) (classConstant.getIndex().intValue());
		}
		return tmp;
	}

}