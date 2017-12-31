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

	public static class UTF8Constant extends Constant {
		private String str;

		public UTF8Constant(String str) {
			this.str = str;
		}

		@Override
		public Byte[] toByteArray() {
			List<Byte> list = new ArrayList<>();
			list.add((byte) 0x01);
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

	public static class StringConstant extends Constant {
		private UTF8Constant utf8;

		public StringConstant(UTF8Constant utf8) {
			this.utf8 = utf8;
		}

		@Override
		public Byte[] toByteArray() {
			List<Byte> list = new ArrayList<>();
			list.add((byte) 0x08);
			list.add((byte) 0x00);
			list.add(this.utf8.getIndex().byteValue());
			return list.toArray(new Byte[0]);
		}
	}

	public static class ClassConstant extends Constant {
		private UTF8Constant utf8;

		public ClassConstant(UTF8Constant utf8) {
			this.utf8 = utf8;
		}

		@Override
		public Byte[] toByteArray() {
			List<Byte> list = new ArrayList<>();
			list.add((byte) 0x07);
			list.add((byte) (this.utf8.getIndex() >> 8));
			list.add(this.utf8.getIndex().byteValue());
			return list.toArray(new Byte[0]);
		}
	}

	public static class FieldRef extends Constant {
		ClassConstant clazz;
		NameAndType nat;

		public FieldRef(ClassConstant clazz, NameAndType nat) {
			this.clazz = clazz;
			this.nat = nat;
		}

		@Override
		public Byte[] toByteArray() {
			List<Byte> list = new ArrayList<>();
			list.add((byte) 0x09);
			list.add((byte) (this.clazz.getIndex() >> 8));
			list.add(this.clazz.getIndex().byteValue());
			list.add((byte) (this.nat.getIndex() >> 8));
			list.add(this.nat.getIndex().byteValue());
			return list.toArray(new Byte[0]);
		}

	}

	public static class MethodRef extends Constant {
		ClassConstant clazz;
		NameAndType nat;

		public MethodRef(ClassConstant clazz, NameAndType nat) {
			this.clazz = clazz;
			this.nat = nat;
		}

		@Override
		public Byte[] toByteArray() {
			List<Byte> list = new ArrayList<>();
			list.add((byte) 0x0a);
			list.add((byte) (this.clazz.getIndex() >> 8));
			list.add(this.clazz.getIndex().byteValue());
			list.add((byte) (this.nat.getIndex() >> 8));
			list.add(this.nat.getIndex().byteValue());
			return list.toArray(new Byte[0]);
		}
	}

	public static class NameAndType extends Constant {
		UTF8Constant name;
		UTF8Constant type;

		public NameAndType(UTF8Constant name, UTF8Constant type) {
			this.name = name;
			this.type = type;
		}

		@Override
		public Byte[] toByteArray() {
			List<Byte> list = new ArrayList<>();
			list.add((byte) 0x0c);
			list.add((byte) (this.name.getIndex() >> 8));
			list.add(this.name.getIndex().byteValue());
			list.add((byte) (this.type.getIndex() >> 8));
			list.add(this.type.getIndex().byteValue());
			return list.toArray(new Byte[0]);
		}
	}

	public static class ConstantPool {
		Integer index = new Integer(1);
		List<Constant> constantList = new ArrayList<>();

		public void add(Constant constant) {
			constant.setIndex(index);
			constantList.add(constant);
			index++;
		}

		public Byte[] toByteArray() {
			List<Byte> list = new ArrayList<>();
			list.add((byte) ((constantList.size() + 1) >> 8));
			list.add((byte) (constantList.size() + 1));
			constantList.stream().flatMap(x -> Arrays.stream(x.toByteArray())).forEach(x -> list.add(x));
			return list.toArray(new Byte[0]);
		}
	}

	public static class ConstantIndex {
		Constant constant;

		public ConstantIndex(Constant constant) {
			this.constant = constant;
		}

		public Byte[] toByteArray() {
			return new Byte[] { (byte) (this.constant.getIndex().intValue() >> 8),
					(byte) (this.constant.getIndex().intValue()) };
		}
	}

	public static class AttributeInfo {
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

	public static class ExceptionsAttribute extends AttributeInfo {
		List<ClassConstant> exceptions = new ArrayList<>();

		public ExceptionsAttribute(UTF8Constant utf8) {
			super(utf8, null);
		}

		public void addException(ClassConstant clazz) {
			this.exceptions.add(clazz);
		}

		public Byte[] toByteArray() {
			int length = 2 + 2 * (this.exceptions.size());
			Byte[] tmp = new Byte[6 + length];
			tmp[0] = (byte) (this.utf8.getIndex().intValue() >> 8);
			tmp[1] = (byte) (this.utf8.getIndex().intValue());
			tmp[2] = (byte) (length >> 24);
			tmp[3] = (byte) (length >> 16);
			tmp[4] = (byte) (length >> 8);
			tmp[5] = (byte) (length);
			tmp[6] = (byte) (this.exceptions.size() >> 8);
			tmp[7] = (byte) (this.exceptions.size());
			int index = 8;
			for (ClassConstant classConstant : exceptions) {
				tmp[index++] = (byte) (classConstant.getIndex().intValue() >> 8);
				tmp[index++] = (byte) (classConstant.getIndex().intValue());
			}
			return tmp;
		}

	}

	public static class MethodInfo {
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

	public static void main(String[] args) throws Exception {
		Byte[] cafebabe = { (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe };
		Byte[] minorVersion = { 0x00, 0x00 };
		Byte[] majorVersion = { 0x00, 0x34 };
		// Byte[] constantPoolCount = { 0x00, 0x20 };
		UTF8Constant c1 = new UTF8Constant("<init>");
		UTF8Constant c2 = new UTF8Constant("()V");
		UTF8Constant c3 = new UTF8Constant("out");
		UTF8Constant c4 = new UTF8Constant("Ljava/io/PrintStream;");
		UTF8Constant c5 = new UTF8Constant("println");
		UTF8Constant c6 = new UTF8Constant("(Ljava/lang/String;)V");
		UTF8Constant c7 = new UTF8Constant("こんにちは、世界!");
		Constant c8 = new StringConstant(c7);
		UTF8Constant c9 = new UTF8Constant("Test");
		UTF8Constant c10 = new UTF8Constant("java/lang/Object");
		UTF8Constant c11 = new UTF8Constant("java/lang/Exception");
		UTF8Constant c12 = new UTF8Constant("java/lang/System");
		UTF8Constant c13 = new UTF8Constant("java/io/PrintStream");
		ClassConstant c14 = new ClassConstant(c12);
		ClassConstant c15 = new ClassConstant(c10);
		ClassConstant c16 = new ClassConstant(c13);
		Constant c17 = new ClassConstant(c9);
		UTF8Constant c18 = new UTF8Constant("Code");
		UTF8Constant c19 = new UTF8Constant("Exceptions");
		UTF8Constant c20 = new UTF8Constant("main");
		UTF8Constant c21 = new UTF8Constant("([Ljava/lang/String;)V");
		ClassConstant c22 = new ClassConstant(c11);

		NameAndType nt1 = new NameAndType(c1, c2);
		NameAndType nt2 = new NameAndType(c3, c4);
		NameAndType nt3 = new NameAndType(c5, c6);

		FieldRef f1 = new FieldRef(c14, nt2);

		MethodRef m1 = new MethodRef(c15, nt1);
		MethodRef m2 = new MethodRef(c16, nt3);
		Constant[] constants = { //
				m1, // #1
				f1, // #2
				c8, // #3
				m2, // #4
				c17, // #5
				c15, // #6
				c1, // #7
				c2, // #8
				c18, // #9
				new UTF8Constant("LineNumberTable"), // #10(0x0a)
				c20, // #11(0x0b)
				c21, // #12(0x0c)
				c19, // #13(0x0d)
				c22, // #14(0x0e)
				new UTF8Constant("SourceFile"), // #15(0x0f)
				new UTF8Constant("Test.java"), // #16(0x10)
				nt1, // #17(0x11)
				c14, // #18(0x12)
				nt2, // #19(0x13)
				c7, // #20(0x14)
				c16, // #21(0x15)
				nt3, // #22(0x16)
				c9, // #23(0x17)
				c10, // #24(0x18)
				c11, // #25(0x19)
				c12, // #26(0x1a)
				c3, // #27(0x1b)
				c4, // #28(0x1c)
				c13, // #29(0x1d)
				c5, // #30(0x1e)
				c6// #31(0x1f)
		};
		ConstantPool pool = new ConstantPool();
		Arrays.stream(constants).forEach(c -> pool.add(c));
		Byte[] accessflg = { 0x00, 0x21 };
		ConstantIndex thisClass = new ConstantIndex(c17);
		ConstantIndex superClass = new ConstantIndex(c15);
		Byte[] interfaceCount = { 0x00, 0x00 };
		Byte[] interfaces = {};
		Byte[] fieldsCount = { 0x00, 0x00 };
		Byte[] fields = {};
		Byte[] methodsCount = { 0x00, 0x02 };
		AttributeInfo a1 = new AttributeInfo(c18, // "Code"
				new Byte[] { 0x00, 0x01, // attribute_info[0] max_stack
						0x00, 0x01, // attribute_info[0] max_locals
						0x00, 0x00, 0x00, 0x05, // attribute_info[0] code_length
						// attribute_info[0] code
						0x2a, // --> aload_0
						(byte) 0xb7, 0x00, 0x01, // --> invokespecial #1
						(byte) 0xb1, // --> return
						// attribute_info[0] code
						0x00, 0x00, // attribute_info[0] exception_table_length
						0x00, 0x01, // attribute_info[0] attributes_count
						0x00, 0x0a, // attribute_info[0] attribute_info[0] attribute_name_index = "LineNumberTable"
						0x00, 0x00, 0x00, 0x06, // attribute_info[0] attribute_info[0] attribute_length
						0x00, 0x01, // line_number_table_length
						0x00, 0x00, // start_pc
						0x00, 0x01 // line_number
				});
		MethodInfo methodInfo0 = new MethodInfo(new Byte[] { 0x00, 0x01 }, // access_flag
				c1, c2);
		methodInfo0.addAttributeInfo(a1);

		AttributeInfo a2 = new AttributeInfo(c18, // "Code"
				new Byte[] { 0x00, 0x02, // attribute_info[0] max_stack
						0x00, 0x01, // attribute_info[0] max_locals
						0x00, 0x00, 0x00, 0x09, // attribute_info[0] code_length
						// attribute_info[0] code
						(byte) 0xb2, 0x00, 0x02, // --> getstatic #2
						0x12, 0x03, // --> ldc #3
						(byte) 0xb6, 0x00, 0x04, // --> invokevirtual #4
						(byte) 0xb1, // --> return
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
				});
		ExceptionsAttribute ea = new ExceptionsAttribute(c19 // "Exceptions"
		);
		ea.addException(c22);
		MethodInfo methodInfo1 = new MethodInfo(new Byte[] { 0x00, 0x09 }, c20, c21);
		methodInfo1.addAttributeInfo(a2);
		methodInfo1.addAttributeInfo(ea);

		Byte[] attributeCount = { 0x00, 0x01 };
		Byte[][] attributeInfo0 = { { 0x00, 0x0f }, // attribute_name_index
				{ 0x00, 0x00, 0x00, 0x02 }, // attribute_length
				{ 0x00, 0x10 } };
		File f = new File("/tmp/Test.class");
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
			write(bos, cafebabe);
			write(bos, minorVersion);
			write(bos, majorVersion);
			write(bos, pool.toByteArray());
			write(bos, accessflg);
			write(bos, thisClass.toByteArray());
			write(bos, superClass.toByteArray());
			write(bos, interfaceCount);
			write(bos, interfaces);
			write(bos, fieldsCount);
			write(bos, fields);
			write(bos, methodsCount);
			write(bos, methodInfo0.toByteArray());
			write(bos, methodInfo1.toByteArray());
			write(bos, attributeCount);
			write(bos, attributeInfo0);
		}
	}

	protected static void write(OutputStream os, Byte[][] x) {
		Arrays.stream(x).forEach(a -> write(os, a));
	}

	protected static void write(OutputStream os, Byte[] x) {
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
