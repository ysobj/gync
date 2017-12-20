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

	// CONSTANT_Class 7
	// CONSTANT_Fieldref 9
	// CONSTANT_Methodref 10
	// CONSTANT_InterfaceMethodref 11
	// CONSTANT_String 8
	// CONSTANT_Integer 3
	// CONSTANT_Float 4
	// CONSTANT_Long 5
	// CONSTANT_Double 6
	// CONSTANT_NameAndType 12
	// CONSTANT_Utf8 1
	// CONSTANT_MethodHandle 15
	// CONSTANT_MethodType 16
	// CONSTANT_InvokeDynamic 18

	public static class Constant {
		private Integer index;
		private Integer[] constant;

		public Constant() {

		}

		public Constant(Integer[] value) {
			constant = value;
		}

		public Integer[] toIntegerArray() {
			return constant;
		}

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}
	}

	public static class UTF8Constant extends Constant {
		private String str;

		public UTF8Constant(String str) {
			this.str = str;
		}

		@Override
		public Integer[] toIntegerArray() {
			List<Integer> list = new ArrayList<>();
			list.add(0x01);
			list.add(0x00);
			list.add(str.length());
			str.chars().forEach(c -> list.add(c));
			return list.toArray(new Integer[0]);
		}
	}

	public static class StringConstant extends Constant {
		private int index;

		public StringConstant(int index) {
			this.index = index;
		}

		@Override
		public Integer[] toIntegerArray() {
			List<Integer> list = new ArrayList<>();
			list.add(0x08);
			list.add(0x00);
			list.add(this.index);
			return list.toArray(new Integer[0]);
		}
	}

	public static class ClassConstant extends Constant {
		private int index;

		public ClassConstant(int index) {
			this.index = index;
		}

		@Override
		public Integer[] toIntegerArray() {
			List<Integer> list = new ArrayList<>();
			list.add(0x07);
			list.add(0x00);
			list.add(this.index);
			return list.toArray(new Integer[0]);
		}
	}

	public static class FieldRef extends Constant {
		ClassConstant clazz;
		NameAndType nat;

		public FieldRef(ClassConstant clazz, NameAndType nat) {
			this.clazz = clazz;
			this.nat = nat;
		}
	}

	public static class MethodRef extends Constant {
		ClassConstant clazz;
		NameAndType nat;

		public MethodRef(ClassConstant clazz, NameAndType nat) {
			this.clazz = clazz;
			this.nat = nat;
		}
	}

	public static class NameAndType extends Constant {
		UTF8Constant name;
		UTF8Constant type;

		public NameAndType(UTF8Constant name, UTF8Constant type) {
			this.name = name;
			this.type = type;
		}
	}

	public static class ConstantPool {
		Integer index = new Integer(1);
		List<Constant> constantList = new ArrayList<>();

		public void add(Constant constant) {
			constant.setIndex(index);
			constantList.add(constant);
			index = index++;
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
				new StringConstant(20), // #3
				new Constant(new Integer[] { 0x0a, 0x00, 0x15, 0x00, 0x16 }), // #4
				new ClassConstant(23), // #5
				new ClassConstant(24), // #6
				new UTF8Constant("<init>"), // #7
				new UTF8Constant("()V"), // #8
				new UTF8Constant("Code"), // #9
				new UTF8Constant("LineNumberTable"), // #10(0x0a)
				new UTF8Constant("main"), // #11(0x0b)
				new UTF8Constant("([Ljava/lang/String;)V"), // #12(0x0c)
				new UTF8Constant("Exceptions"), // #13(0x0d)
				new ClassConstant(25), // #14(0x0e)
				new UTF8Constant("SourceFile"), // #15(0x0f)
				new UTF8Constant("Test.java"), // #16(0x10)
				new Constant(new Integer[] { 0x0c, 0x00, 0x07, 0x00, 0x08 }), // #17(0x11)
				new ClassConstant(26), // #18(0x12)
				new Constant(new Integer[] { 0x0c, 0x00, 0x1b, 0x00, 0x1c }), // #19(0x13)
				new UTF8Constant("Hello, Werld"), // #20(0x14)
				new ClassConstant(29), // #21(0x15)
				new Constant(new Integer[] { 0x0c, 0x00, 0x1e, 0x00, 0x1f }), // #22(0x16)
				new UTF8Constant("Test"), // #23(0x17)
				new UTF8Constant("java/lang/Object"), // #24(0x18)
				new UTF8Constant("java/lang/Exception"), // #25(0x19)
				new UTF8Constant("java/lang/System"), // #26(0x1a)
				new UTF8Constant("out"), // #27(0x1b)
				new UTF8Constant("Ljava/io/PrintStream;"), // #28(0x1c)
				new UTF8Constant("java/io/PrintStream"), // #29(0x1d)
				new UTF8Constant("println"), // #30(0x1e)
				new UTF8Constant("(Ljava/lang/String;)V") // #31(0x1f)
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
				{ 0x00, 0x01, // attribute_info[0] max_stack
						0x00, 0x01, // attribute_info[0] max_locals
						0x00, 0x00, 0x00, 0x05, // attribute_info[0] code_length
						// attribute_info[0] code
						0x2a, // --> aload_0
						0xb7, 0x00, 0x01, // --> invokespecial #1
						0xb1, // --> return
						// attribute_info[0] code
						0x00, 0x00, // attribute_info[0] exception_table_length
						0x00, 0x01, // attribute_info[0] attributes_count
						0x00, 0x0a, // attribute_info[0] attribute_info[0] attribute_name_index = "LineNumberTable"
						0x00, 0x00, 0x00, 0x06, // attribute_info[0] attribute_info[0] attribute_length
						0x00, 0x01, // line_number_table_length
						0x00, 0x00, // start_pc
						0x00, 0x01 // line_number
				} };
		Integer[][] methodsInfo1 = { { 0x00, 0x09 }, // access_flag
				{ 0x00, 0x0b }, // name_index
				{ 0x00, 0x0c }, // descriptor_index
				{ 0x00, 0x02 }, // attributes_count
				{ 0x00, 0x09 }, // attribute_info[0] attribute_name_index = "Code"
				{ 0x00, 0x00, 0x00, 0x25 }, // attribute_info[0] attribute_length
				{ 0x00, 0x02, // attribute_info[0] max_stack
						0x00, 0x01, // attribute_info[0] max_locals
						0x00, 0x00, 0x00, 0x09, // attribute_info[0] code_length
						// attribute_info[0] code
						0xb2, 0x00, 0x02, // --> getstatic #2
						0x12, 0x03, // --> ldc #3
						0xb6, 0x00, 0x04, // --> invokevirtual #4
						0xb1, // --> return
						// attribute_info[0] code
						0x00, 0x00, // attribute_info[0] exception_table_length
						0x00, 0x01, // attribute_info[0] attributes_count
						0x00, 0x0a, // attribute_info[0] attribute_info[0] attribute_name_index = "LineNumberTable"
						0x00, 0x00, 0x00, 0x0a, // attribute_info[0] attribute_info[0] attribute_length
						0x00, 0x02, // attribute_info[0] attribute_info[0] line_number_table_length
						0x00, 0x00, // start_pc
						0x00, 0x03, // line_number
						0x00, 0x08, // start_pc
						0x00, 0x04 // line_number

				}, //
				{ 0x00, 0x0d }, // attribute_info[1] attribute_name_index = "Exceptions"
				{ 0x00, 0x00, 0x00, 0x04 }, // attribute_info[1] attribute_length
				{ //
						0x00, 0x01, // number_of_exceptions
						0x00, 0x0e // exception_index_table
				} };
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
