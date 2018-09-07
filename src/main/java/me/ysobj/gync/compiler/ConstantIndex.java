package me.ysobj.gync.compiler;

public class ConstantIndex {
	Constant constant;

	public ConstantIndex(Constant constant) {
		this.constant = constant;
	}

	public Byte[] toByteArray() {
		return new Byte[] { (byte) (this.constant.getIndex().intValue() >> 8),
				(byte) (this.constant.getIndex().intValue()) };
	}
}