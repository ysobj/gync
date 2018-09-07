package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstantPool {
	Integer index = Integer.valueOf(1);
	List<Constant> constantList = new ArrayList<>();

	public void add(Constant constant) {
		constant.setIndex(index);
		constantList.add(constant);
		index++;
	}

	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add((byte) ((constantList.size() + 1) >> 8));
		list.add((byte) (constantList.size() + 1));
		constantList.stream().flatMap(x -> Arrays.stream(x.toByteArray())).forEach(x -> list.add(x));
		return list.toArray(new Byte[0]);
	}
}