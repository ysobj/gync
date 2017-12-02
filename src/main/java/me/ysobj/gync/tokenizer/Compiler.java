package me.ysobj.gync.tokenizer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Compiler {
	public static class Constant {
		private Integer[] constant;

		public Constant() {

		}

		public Constant(Integer[] value) {
			constant = value;
		}

		public Integer[] toIntegerArray() {
			return constant;
		}
	}

	public static class UTF8Constant extends Constant {
		private String str;

		public UTF8Constant(String str) {
			this.str = str;
		}

		public Integer[] toIntegerArray() {
			List<Integer> list = new ArrayList<>();
			list.add(0x01);
			list.add(0x00);
			list.add(str.length());
			str.chars().forEach(c -> list.add(c));
			return list.toArray(new Integer[0]);
		}
	}

	public static class ConstantPool {
		List<Constant> constantList = new ArrayList<>();

		public void add(Constant constant) {
			constantList.add(constant);
		}

		public Integer[] toIntegerArray() {
			List<Integer> list = new ArrayList<>();
			list.add(0);
			list.add(constantList.size() + 1);
			constantList.stream().flatMap(x -> Arrays.stream(x.toIntegerArray())).forEach(x -> list.add(x));
			return list.toArray(new Integer[0]);
		}
	}

	public static void main(String[] args) throws Exception {
		Integer[] cafebabe = { 0xca, 0xfe, 0xba, 0xbe };
		Integer[] minorVersion = { 0x00, 0x00 };
		Integer[] majorVersion = { 0x00, 0x34 };
		// Integer[] constantPoolCount = { 0x00, 0x20 };
		Constant[] constants = { //
				new Constant(new Integer[] { 0x0a, 0x00, 0x06, 0x00, 0x11 }), // #1
				new Constant(new Integer[] { 0x09, 0x00, 0x12, 0x00, 0x13 }), // #2
				new Constant(new Integer[] { 0x08, 0x00, 0x14 }), // #3
				new Constant(new Integer[] { 0x0a, 0x00, 0x15, 0x00, 0x16 }), // #4
				new Constant(new Integer[] { 0x07, 0x00, 0x17 }), // #5
				new Constant(new Integer[] { 0x07, 0x00, 0x18 }), // #6
				new UTF8Constant("<init>"), // #7
				new UTF8Constant("()V"), // #8
				new UTF8Constant("Code"), // #9
				new UTF8Constant("LineNumberTable"), // #10
				new UTF8Constant("main"), // #11
				new UTF8Constant("([Ljava/lang/String;)V"), // #12
				new UTF8Constant("Exceptions"), // #13
				new Constant(new Integer[] { 0x07, 0x00, 0x19 }), // #14
				new UTF8Constant("SourceFile"), // #15
				new UTF8Constant("Test.java"), // #16
				new Constant(new Integer[] { 0x0c, 0x00, 0x07, 0x00, 0x08 }), // #17
				new Constant(new Integer[] { 0x07, 0x00, 0x1a }), // #18
				new Constant(new Integer[] { 0x0c, 0x00, 0x1b, 0x00, 0x1c }), // #19
				new UTF8Constant("Hello, Werld"), // #20
				new Constant(new Integer[] { 0x07, 0x00, 0x1d }), // #21
				new Constant(new Integer[] { 0x0c, 0x00, 0x1e, 0x00, 0x1f }), // #22
				new UTF8Constant("Test"), // #23
				new UTF8Constant("java/lang/Object"), // #24
				new UTF8Constant("java/lang/Exception"), // #25
				new UTF8Constant("java/lang/System"), // #26
				new UTF8Constant("out"), // #27
				new UTF8Constant("Ljava/io/PrintStream;"), // #28
				new UTF8Constant("java/io/PrintStream"), // #29
				new UTF8Constant("println"), // #30
				new UTF8Constant("(Ljava/lang/String;)V") // #31
		};
		ConstantPool pool = new ConstantPool();
		Arrays.stream(constants).forEach(c -> pool.add(c));
		Integer[] accessflg = { 0x00, 0x21 };
		Integer[] thisClass = { 0x00, 0x05 };
		Integer[] superClass = { 0x00, 0x06 };
		Integer[] interfaceCount = { 0x00, 0x00 };
		Integer[] interfaces = {};
		Integer[] fieldsCount = { 0x00, 0x00 };
		Integer[] fields = {};
		Integer[] methodsCount = { 0x00, 0x02 };
		Integer[][] methodsInfo0 = { { 0x00, 0x01 }, // access_flag
				{ 0x00, 0x07 }, // name_index
				{ 0x00, 0x08 }, // descriptor_index
				{ 0x00, 0x01 }, // attributes_count
				{ 0x00, 0x09 }, // attribute_info[0] attribute_name_index
				{ 0x00, 0x00, 0x00, 0x1d }, // attribute_info[0] attribute_length
				{ 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x05, 0x2a, 0xb7, 0x00, 0x01, 0xb1, 0x00, 0x00, 0x00, 0x01,
						0x00, 0x0a, 0x00, 0x00, 0x00, 0x06, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01 } };
		Integer[][] methodsInfo1 = { { 0x00, 0x09 }, // access_flag
				{ 0x00, 0x0b }, // name_index
				{ 0x00, 0x0c }, // descriptor_index
				{ 0x00, 0x02 }, // attributes_count
				{ 0x00, 0x09 }, // attribute_info[0] attribute_name_index
				{ 0x00, 0x00, 0x00, 0x25 }, // attribute_info[0] attribute_length
				{ 0x00, 0x02, 0x00, 0x01, 0x00, 0x00, 0x00, 0x09, 0xb2, 0x00, 0x02, 0x12, 0x03, 0xb6, 0x00, 0x04, 0xb1,
						0x00, 0x00, 0x00, 0x01, 0x00, 0x0a, 0x00, 0x00, 0x00, 0x0a, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03,
						0x00, 0x08, 0x00, 0x04 },
				{ 0x00, 0x0d }, // attribute_info[1] attribute_name_index
				{ 0x00, 0x00, 0x00, 0x04 }, // attribute_info[1] attribute_length
				{ 0x00, 0x01, 0x00, 0x0e }, };
		Integer[] attributeCount = { 0x00, 0x01 };
		Integer[][] attributeInfo0 = { { 0x00, 0x0f }, // attribute_name_index
				{ 0x00, 0x00, 0x00, 0x02 }, // attribute_length
				{ 0x00, 0x10 } };
		File f = new File("/tmp/Test.class");
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
			write(bos, cafebabe);
			write(bos, minorVersion);
			write(bos, majorVersion);
			write(bos, pool.toIntegerArray());
			// write(bos, constantPoolCount);
			// write(bos, constantPool);
			write(bos, accessflg);
			write(bos, thisClass);
			write(bos, superClass);
			write(bos, interfaceCount);
			write(bos, interfaces);
			write(bos, fieldsCount);
			write(bos, fields);
			write(bos, methodsCount);
			write(bos, methodsInfo0);
			write(bos, methodsInfo1);
			write(bos, attributeCount);
			write(bos, attributeInfo0);
		}
	}

	protected static void write(OutputStream os, Integer[][] x) {
		Arrays.stream(x).forEach(a -> write(os, a));
	}

	protected static void write(OutputStream os, Integer[] x) {
		Arrays.stream(x).forEach(b -> {
			try {
				os.write(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
