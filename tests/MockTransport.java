import org.objectweb.asm.*;
import java.nio.file.*;
import java.util.zip.*;

/** Test artifact only: replaces every runtime send site; never shipped. */
class MockTransport implements Opcodes {
    public static void main(String[] args) throws Exception {
        try(ZipFile src=new ZipFile(args[0]);ZipOutputStream dst=new ZipOutputStream(Files.newOutputStream(Path.of(args[1])))) {
            var entries=src.entries();
            while(entries.hasMoreElements()) {
                var e=entries.nextElement();byte[] b=src.getInputStream(e).readAllBytes();
                if(e.getName().equals("EAVbpdxFGDnFcb.class") || e.getName().equals("zxnsayePWpajv.class") || e.getName().equals("AutoPetBridge.class")) {
                    ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    new ClassReader(b).accept(new ClassVisitor(ASM9,w) {
                        public MethodVisitor visitMethod(int a,String n,String d,String sig,String[] ex) {
                            String op=null;
                            if(e.getName().equals("EAVbpdxFGDnFcb.class")) {
                                if(n.equals("uYrMAoZFCm") && d.equals("(II)V"))op="info";
                                if(n.equals("jPtiHTBf") && d.equals("(I)V"))op="equip";
                                if(n.equals("zmEhqDHwAKvzjM") && d.equals("(I)V"))op="list";
                                if(n.equals("BMWCZFcnBgzHkh") && d.equals("(III)V"))op="buy";
                                if(n.equals("hFWcqhZjekJK") && d.equals("(I)V"))op="confirm";
                            }
                            if(e.getName().equals("zxnsayePWpajv.class") && n.equals("oXAaoinDuwaN") && d.equals("(I)V"))op="npc";
                            MethodVisitor v=super.visitMethod(a,n,d,sig,ex);
                            if(e.getName().equals("AutoPetBridge.class") && n.equals("notice") && d.equals("(Ljava/lang/String;)V")) {
                                v.visitCode();v.visitVarInsn(ALOAD,0);v.visitMethodInsn(INVOKESTATIC,"TestTransport","notice","(Ljava/lang/String;)V",false);v.visitInsn(RETURN);v.visitMaxs(0,0);v.visitEnd();return null;
                            }
                            if(op==null)return v;
                            v.visitCode();v.visitLdcInsn(op);int start=(a&ACC_STATIC)!=0?0:1;
                            int count=Type.getArgumentTypes(d).length;
                            for(int i=0;i<3;i++)if(i<count)v.visitVarInsn(ILOAD,start+i);else v.visitInsn(ICONST_0);
                            v.visitMethodInsn(INVOKESTATIC,"TestTransport","record","(Ljava/lang/String;III)V",false);v.visitInsn(RETURN);v.visitMaxs(0,0);v.visitEnd();return null;
                        }
                    },0);b=w.toByteArray();
                }
                dst.putNextEntry(new ZipEntry(e.getName()));dst.write(b);dst.closeEntry();
            }
        }
    }
}
