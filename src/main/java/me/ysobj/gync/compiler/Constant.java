package me.ysobj.gync.compiler;

public class Constant {
	private Integer index;
	private Byte[] constant;

	public Constant() {

	}

	public Constant(Byte[] value) {
		constant = value;
	}

	public Byte[] toByteArray() {
		return constant;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}