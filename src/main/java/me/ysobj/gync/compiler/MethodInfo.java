package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodInfo {
	Byte[] accessFlag;
	UTF8Constant nameIndex;
	UTF8Constant descriptorIndex;
	List<AttributeInfo> attributeList = new ArrayList<>();

	public MethodInfo(Byte[] accessFlag, UTF8Constant nameIndex, UTF8Constant descriptorIndex) {
		this.accessFlag = accessFlag;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add(this.accessFlag[0]);
		list.add(this.accessFlag[1]);
		list.add((byte) (this.nameIndex.getIndex().intValue() >> 8));
		list.add((byte) this.nameIndex.getIndex().intValue());
		list.add((byte) (this.descriptorIndex.getIndex().intValue() >> 8));
		list.add((byte) this.descriptorIndex.getIndex().intValue());
		int ac = attributeList.size();
		list.add((byte) (ac >> 8));
		list.add((byte) ac);
		for (AttributeInfo attributeInfo : attributeList) {
			Arrays.stream(attributeInfo.toByteArray()).forEach(b -> list.add(b));
		}
		return list.toArray(new Byte[0]);
	}

	public void addAttributeInfo(AttributeInfo attributeInfo) {
		this.attributeList.add(attributeInfo);
	}
}