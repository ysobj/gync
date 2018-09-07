package me.ysobj.gync.compiler;

public class SourceFileAttribute extends AttributeInfo {
	UTF8Constant sourceFile;

	public SourceFileAttribute(UTF8Constant utf8, UTF8Constant sourceFile) {
		super(utf8, null);
		this.sourceFile = sourceFile;
	}

	public Byte[] toByteArray() {
		Byte[] tmp = new Byte[8];
		tmp[0] = (byte) (this.utf8.getIndex().intValue() >> 8);
		tmp[1] = (byte) (this.utf8.getIndex().intValue());
		tmp[2] = 0;
		tmp[3] = 0;
		tmp[4] = 0;
		tmp[5] = 0b00000010;
		tmp[6] = (byte) (this.sourceFile.getIndex().intValue() >> 8);
		tmp[7] = (byte) (this.sourceFile.getIndex().intValue());
		return tmp;
	}
}