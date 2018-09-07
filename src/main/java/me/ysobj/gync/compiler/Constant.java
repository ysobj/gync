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

	public Byte[] getIndexAsBytes() {
		Byte[] tmp = new Byte[2];
		tmp[0] = (byte) (this.getIndex().intValue() >> 8);
		tmp[1] = (byte) (this.getIndex().intValue());
		return tmp;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}