package me.ysobj.gync.compiler;

import java.util.ArrayList;
import java.util.List;

public class UTF8Constant extends Constant {
	private String str;

	public UTF8Constant(String str) {
		this.str = str;
	}

	@Override
	public Byte[] toByteArray() {
		List<Byte> list = new ArrayList<>();
		list.add(CompilerConstants.CONSTANT_Utf8);
		List<Byte> tmp = new ArrayList<>();
		str.chars().forEach(c -> {
			if (c != 0x00 && c < 0x80) {
				tmp.add((byte) c);
				return;
			} else if (c >= 0x0800) {
				tmp.add((byte) (0xe0 | (c >> 12)));
				tmp.add((byte) ((0x3f & (c >> 6)) | 0x80));
				tmp.add((byte) ((((byte) c) & 0x3f) | 0x80));
				return;
			}
			tmp.add((byte) (0xc0 | (c >> 6)));
			tmp.add((byte) (c & 0x3f));
			return;
		});
		list.add((byte) (tmp.size() >> 8));
		list.add((byte) tmp.size());
		list.addAll(tmp);
		return list.toArray(new Byte[0]);
	}
}