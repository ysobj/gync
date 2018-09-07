package me.ysobj.gync.compiler;

public class AttributeInfo {
	UTF8Constant utf8;
	Byte[] info;

	public AttributeInfo(UTF8Constant utf8, Byte[] info) {
		this.utf8 = utf8;
		this.info = info;
	}

	public Byte[] toByteArray() {
		Byte[] tmp = new Byte[6 + this.info.length];
		tmp[0] = (byte) (this.utf8.getIndex().intValue() >> 8);
		tmp[1] = (byte) (this.utf8.getIndex().intValue());
		tmp[2] = (byte) (this.info.length >> 24);
		tmp[3] = (byte) (this.info.length >> 16);
		tmp[4] = (byte) (this.info.length >> 8);
		tmp[5] = (byte) (this.info.length);
		System.arraycopy(this.info, 0, tmp, 6, this.info.length);
		return tmp;
	}
}