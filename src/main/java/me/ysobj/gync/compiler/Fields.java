package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.List;

public class Fields {
	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add((byte) 0x00);
		list.add((byte) 0x00);
		return list.toArray(new Byte[0]);
	}
}
