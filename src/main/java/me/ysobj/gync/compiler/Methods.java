package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Methods {
	private List<MethodInfo> methods = new ArrayList<>();

	public void addMethod(MethodInfo method) {
		methods.add(method);
	}

	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add(Byte.valueOf((byte) (methods.size() >> 8)));
		list.add(Byte.valueOf((byte) (methods.size())));
		methods.stream().forEach(m -> Arrays.stream(m.toByteArray()).forEach(b -> list.add(b)));
		return list.toArray(new Byte[0]);
	}

}
