package me.ysobj.gync.compiler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class Compiler {
	private static Byte[] CAFEBABE = { (byte) 0xca, (byte) 0xfe, (byte) 0xba, (byte) 0xbe };
	private static Byte[] MINORVERSION = { 0x00, 0x00 };
	private static Byte[] MAJORVERSION = { 0x00, 0x34 }; // Java SE 8 = 52 (0x34 hex)

	public static void main(String[] args) throws Exception {
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
		UTF8Constant c23 = new UTF8Constant("SourceFile");
		UTF8Constant c24 = new UTF8Constant("Test.java");

		NameAndType nt1 = new NameAndType(c1, c2);
		NameAndType nt2 = new NameAndType(c3, c4);
		NameAndType nt3 = new NameAndType(c5, c6);

		FieldRef f1 = new FieldRef(c14, nt2);

		MethodRef m1 = new MethodRef(c15, nt1);
		MethodRef m2 = new MethodRef(c16, nt3);
		ConstantPool pool = new ConstantPool();
		pool.add(m1);
		pool.add(f1); // #2
		pool.add(c8); // #3
		pool.add(m2); // #4
		pool.add(c17); // #5
		pool.add(c15); // #6
		pool.add(c1); // #7
		pool.add(c2); // #8
		pool.add(c18); // #9
		pool.add(new UTF8Constant("LineNumberTable")); // #10(0x0a)
		pool.add(c20); // #11(0x0b)
		pool.add(c21); // #12(0x0c)
		pool.add(c19); // #13(0x0d)
		pool.add(c22); // #14(0x0e)
		pool.add(c23); // #15(0x0f)
		pool.add(c24); // #16(0x10)
		pool.add(nt1); // #17(0x11)
		pool.add(c14); // #18(0x12)
		pool.add(nt2); // #19(0x13)
		pool.add(c7); // #20(0x14)
		pool.add(c16); // #21(0x15)
		pool.add(nt3); // #22(0x16)
		pool.add(c9); // #23(0x17)
		pool.add(c10); // #24(0x18)
		pool.add(c11); // #25(0x19)
		pool.add(c12); // #26(0x1a)
		pool.add(c3); // #27(0x1b)
		pool.add(c4); // #28(0x1c)
		pool.add(c13); // #29(0x1d)
		pool.add(c5); // #30(0x1e)
		pool.add(c6);// #31(0x1f)

		Byte[] accessflg = { 0x00, 0x21 };
		ConstantIndex thisClass = new ConstantIndex(c17);
		ConstantIndex superClass = new ConstantIndex(c15);
		Interfaces interfaces = new Interfaces();
		Fields fields = new Fields();
		Methods methods = new Methods();
		{
			Byte[] tmp = m1.getIndexAsBytes();
			AttributeInfo a1 = new AttributeInfo(c18, // "Code"
					new Byte[] { 0x00, 0x01, // attribute_info[0] max_stack
							0x00, 0x01, // attribute_info[0] max_locals
							0x00, 0x00, 0x00, 0x05, // attribute_info[0] code_length
							// attribute_info[0] code
							0x2a, // --> aload_0
							(byte) 0xb7, tmp[0], tmp[1], // --> invokespecial #1
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
			methods.addMethod(methodInfo0);
		}
		//
		{
			Byte[] tmp2 = f1.getIndexAsBytes();
			Byte[] tmp3 = c8.getIndexAsBytes();
			Byte[] tmp4 = m2.getIndexAsBytes();
			AttributeInfo a2 = new AttributeInfo(c18, // "Code"
					new Byte[] { 0x00, 0x02, // attribute_info[0] max_stack
							0x00, 0x01, // attribute_info[0] max_locals
							0x00, 0x00, 0x00, 0x09, // attribute_info[0] code_length
							// attribute_info[0] code
							(byte) 0xb2, tmp2[0], tmp2[1], // --> getstatic #2
							0x12, tmp3[1], // --> ldc #3
							(byte) 0xb6, tmp4[0], tmp4[1], // --> invokevirtual #4
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
			methods.addMethod(methodInfo1);
		}

		Byte[] attributeCount = { 0x00, 0x01 };
		SourceFileAttribute sa = new SourceFileAttribute(c23, c24);
		File f = new File(args[0]);
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
			write(bos, CAFEBABE);
			write(bos, MINORVERSION);
			write(bos, MAJORVERSION);
			write(bos, pool.toByteArray()); // constant pool count + constant pool table
			write(bos, accessflg);
			write(bos, thisClass.toByteArray()); // identifies this class, index into the constant pool to a
													// "Class"-type entry
			write(bos, superClass.toByteArray()); // identifies super class, index into the constant pool to a
													// "Class"-type entry
			write(bos, interfaces.toByteArray());
			write(bos, fields.toByteArray());// write 0 byte because our class has no fields.
			write(bos, methods.toByteArray());
			write(bos, attributeCount); // our class has only one attribute.
			write(bos, sa.toByteArray()); // write attribute 0
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
