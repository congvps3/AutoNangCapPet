import org.objectweb.asm.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;

/** Changes only three original methods and the Canvas pointer dispatchers. */
class Patch implements Opcodes {
    static int menu,action,paint,pointer,menuObserver,shopObserver,confirmObserver,packetObserver,noticeObserver,update;
    public static void main(String[] args) throws Exception {
        Path original=Path.of(args[0]),classes=Path.of(args[1]),output=Path.of(args[2]);
        Files.createDirectories(output.getParent());
        try(ZipFile in=new ZipFile(original.toFile());ZipOutputStream out=new ZipOutputStream(Files.newOutputStream(output))) {
            var entries=in.entries();
            while(entries.hasMoreElements()) {
                ZipEntry e=entries.nextElement();byte[] data=in.getInputStream(e).readAllBytes();
                if(e.getName().equals("zxnsayePWpajv.class")) data=game(data);
                if(e.getName().equals("PyYQMyyC.class")) data=canvas(data);
                if(e.getName().equals("ceMGdFNBJnq.class")) data=observe(data,0);
                if(e.getName().equals("NBAFgUXWGCkgPq.class")) data=observe(data,1);
                if(e.getName().equals("yRvgxykm.class")) data=packets(data);
                out.putNextEntry(new ZipEntry(e.getName()));out.write(data);out.closeEntry();
            }
            try(var paths=Files.list(classes)) {
                for(Path p:paths.filter(p->p.toString().endsWith(".class")).sorted().toList()) {
                    out.putNextEntry(new ZipEntry(p.getFileName().toString()));
                    out.write(p.getFileName().toString().equals("AutoPetAccess.class")?access():Files.readAllBytes(p));
                    out.closeEntry();
                }
            }
        }
        if(menu!=2 || action!=1 || paint!=1 || pointer!=3 || menuObserver!=1 || shopObserver!=1 || confirmObserver!=1 || packetObserver!=1 || noticeObserver!=2 || update!=1) {
            Files.deleteIfExists(output);
            throw new IllegalStateException("Unexpected hooks: "+menu+","+action+","+paint+","+pointer);
        }
        System.out.println("Patched main menu=1 MenuAuto=1 action=1 paint=1 pointer=3 update=1; observers: menu=1 shop=1 confirmation=1 packet=1");
    }
    static byte[] game(byte[] bytes) {
        ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
            public MethodVisitor visitMethod(int a,String n,String d,String s,String[] es) {
                MethodVisitor mv=super.visitMethod(a,n,d,s,es);
                return new MethodVisitor(ASM9,mv) {
                    public void visitInsn(int opcode) {
                        if(opcode==RETURN && n.equals("yXFfVgpsUAFd") && d.equals("(I)V")) {
                            shopObserver++;visitVarInsn(ILOAD,1);
                            super.visitMethodInsn(INVOKESTATIC,"AutoPetRuntime","sawShop","(I)V",false);
                        }
                        super.visitInsn(opcode);
                    }
                    public void visitCode() {
                        super.visitCode();
                        if(n.equals("rdmjdEJDQ") && d.equals("(ILjava/lang/Object;)V")) {
                            action++;
                            visitVarInsn(ILOAD,1);
                            visitMethodInsn(INVOKESTATIC,"AutoPetBridge","menu","(I)Z",false);
                            Label next=new Label();visitJumpInsn(IFEQ,next);visitInsn(RETURN);visitLabel(next);
                        }
                    }
                    public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                        if((n.equals("xbqjzadR") || n.equals("GRYYuFfQZGmfH")) && d.equals("()V") && owner.equals("ceMGdFNBJnq") && desc.equals("(LkacyVKUNru;)V")) {
                            menu++;visitInsn(DUP);
                            super.visitMethodInsn(INVOKESTATIC,"AutoPetBridge","addMenu","(LkacyVKUNru;)V",false);
                        }
                        super.visitMethodInsn(op,owner,name,desc,itf);
                        if(n.equals("peMayxrZEBA") && d.equals("(LrHNGbrXZGUD;)V") && owner.equals("okhVYHbLapzrW") && desc.equals("(LrHNGbrXZGUD;)V")) {
                            paint++;visitVarInsn(ALOAD,1);
                            super.visitMethodInsn(INVOKESTATIC,"AutoPetBridge","paint","(LrHNGbrXZGUD;)V",false);
                        }
                    }
                };
            }
            public void visitEnd() {
                MethodVisitor m=super.visitMethod(ACC_PUBLIC|ACC_STATIC,"autoPetShopItem","(II)LwpiwfWAXuWze;",null,null);
                m.visitCode();m.visitFieldInsn(GETSTATIC,"zxnsayePWpajv","taYdGEeyp","I");m.visitVarInsn(ISTORE,2);
                m.visitVarInsn(ILOAD,1);m.visitFieldInsn(PUTSTATIC,"zxnsayePWpajv","taYdGEeyp","I");
                m.visitVarInsn(ILOAD,0);m.visitMethodInsn(INVOKESTATIC,"zxnsayePWpajv","LBtrnuKVnvuG","(I)LwpiwfWAXuWze;",false);
                m.visitVarInsn(ASTORE,3);m.visitVarInsn(ILOAD,2);m.visitFieldInsn(PUTSTATIC,"zxnsayePWpajv","taYdGEeyp","I");
                m.visitVarInsn(ALOAD,3);m.visitInsn(ARETURN);m.visitMaxs(0,0);m.visitEnd();
                m=super.visitMethod(ACC_PUBLIC|ACC_STATIC,"autoPetFocusShop","(I)V",null,null);m.visitCode();
                m.visitVarInsn(ILOAD,0);m.visitFieldInsn(PUTSTATIC,"zxnsayePWpajv","taYdGEeyp","I");
                m.visitInsn(ICONST_1);m.visitFieldInsn(PUTSTATIC,"zxnsayePWpajv","LrmZsvFaTiKDi","I");
                m.visitInsn(RETURN);m.visitMaxs(0,0);m.visitEnd();super.visitEnd();
            }
        },ClassReader.SKIP_DEBUG);
        return w.toByteArray();
    }
    static byte[] canvas(byte[] bytes) {
        ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
            public MethodVisitor visitMethod(int a,String n,String d,String s,String[] es) {
                MethodVisitor mv=super.visitMethod(a,n,d,s,es);
                int kind=n.equals("pointerPressed")?0:n.equals("pointerDragged")?1:n.equals("pointerReleased")?2:-1;
                if(n.equals("run") && d.equals("()V")) return new MethodVisitor(ASM9,mv) {
                    public void visitMethodInsn(int op,String owner,String name,String desc,boolean itf) {
                        super.visitMethodInsn(op,owner,name,desc,itf);
                        if(owner.equals("QQwLYhYdpMaF") && name.equals("peMayxrZEBA") && desc.equals("()V")) {
                            update++;super.visitMethodInsn(INVOKESTATIC,"AutoPetRuntime","tick","()V",false);
                        }
                    }
                };
                if(kind<0 || !d.equals("(II)V")) return mv;
                pointer++;
                return new MethodVisitor(ASM9,mv) {
                    public void visitCode() {
                        super.visitCode();visitVarInsn(ILOAD,1);visitVarInsn(ILOAD,2);visitLdcInsn(kind);
                        visitMethodInsn(INVOKESTATIC,"AutoPetBridge","pointer","(III)Z",false);
                        Label next=new Label();visitJumpInsn(IFEQ,next);visitInsn(RETURN);visitLabel(next);
                    }
                };
            }
        },ClassReader.SKIP_DEBUG);
        return w.toByteArray();
    }
    static byte[] access() {
        ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        w.visit(V1_1,ACC_PUBLIC|ACC_FINAL,"AutoPetAccess",null,"java/lang/Object",null);
        MethodVisitor m=w.visitMethod(ACC_PUBLIC|ACC_STATIC,"isGame","()Z",null,null);m.visitCode();
        Label no=new Label();
        m.visitFieldInsn(GETSTATIC,"zxnsayePWpajv","EgoDKTjkxEUMu","LzxnsayePWpajv;");
        m.visitJumpInsn(IFNULL,no);
        m.visitFieldInsn(GETSTATIC,"NBAFgUXWGCkgPq","peMayxrZEBA","LXZRZEVaKAwWG;");
        m.visitFieldInsn(GETSTATIC,"zxnsayePWpajv","EgoDKTjkxEUMu","LzxnsayePWpajv;");
        m.visitJumpInsn(IF_ACMPNE,no);m.visitInsn(ICONST_1);m.visitInsn(IRETURN);
        m.visitLabel(no);m.visitInsn(ICONST_0);m.visitInsn(IRETURN);m.visitMaxs(0,0);m.visitEnd();
        method(w,"map","()I",v->{v.visitFieldInsn(GETSTATIC,"SBnKDsbzS","uTxJizAJr","S");v.visitInsn(IRETURN);});
        method(w,"npcs","()LkacyVKUNru;",v->{v.visitFieldInsn(GETSTATIC,"zxnsayePWpajv","HSjEMvafS","LkacyVKUNru;");v.visitInsn(ARETURN);});
        method(w,"template","(LgsxwWgJt;)LHAeSnYtYmns;",v->{v.visitVarInsn(ALOAD,0);v.visitFieldInsn(GETFIELD,"gsxwWgJt","peMayxrZEBA","LHAeSnYtYmns;");v.visitInsn(ARETURN);});
        method(w,"input","(LWZKDTXfSh;)Ljava/io/DataInputStream;",v->{v.visitVarInsn(ALOAD,0);v.visitMethodInsn(INVOKEVIRTUAL,"WZKDTXfSh","peMayxrZEBA","()Ljava/io/DataInputStream;",false);v.visitInsn(ARETURN);});
        method(w,"items","(I)[LwpiwfWAXuWze;",v->{
            int[] keys={3,5,41,51,52,53};String[] fields={"peMayxrZEBA","rdmjdEJDQ","LAEAunqbxr","utAFyyExSQ","LBtrnuKVnvuG","BMWCZFcnBgzHkh"};
            Label def=new Label();Label[] labels=new Label[keys.length];for(int i=0;i<labels.length;i++)labels[i]=new Label();
            v.visitVarInsn(ILOAD,0);v.visitLookupSwitchInsn(def,keys,labels);
            for(int i=0;i<labels.length;i++) {v.visitLabel(labels[i]);v.visitMethodInsn(INVOKESTATIC,"oFpffzUyucjDJX","rdmjdEJDQ","()LoFpffzUyucjDJX;",false);v.visitFieldInsn(GETFIELD,"oFpffzUyucjDJX",fields[i],"[LwpiwfWAXuWze;");v.visitInsn(ARETURN);}
            v.visitLabel(def);v.visitInsn(ACONST_NULL);v.visitInsn(ARETURN);
        });
        method(w,"shopItem","(II)LwpiwfWAXuWze;",v->{v.visitVarInsn(ILOAD,0);v.visitVarInsn(ILOAD,1);v.visitMethodInsn(INVOKESTATIC,"zxnsayePWpajv","autoPetShopItem","(II)LwpiwfWAXuWze;",false);v.visitInsn(ARETURN);});
        method(w,"shopTitle","()Ljava/lang/String;",v->{v.visitFieldInsn(GETSTATIC,"zxnsayePWpajv","hFWcqhZjekJK","Ljava/lang/String;");v.visitInsn(ARETURN);});
        method(w,"focusShop","(I)V",v->{
            v.visitVarInsn(ILOAD,0);v.visitMethodInsn(INVOKESTATIC,"zxnsayePWpajv","autoPetFocusShop","(I)V",false);v.visitInsn(RETURN);
        });
        method(w,"dismiss","()V",v->{menuObject(v);v.visitInsn(ICONST_0);v.visitFieldInsn(PUTFIELD,"ceMGdFNBJnq","xWSfQVLnUHeHB","Z");v.visitMethodInsn(INVOKESTATIC,"NBAFgUXWGCkgPq","HmLENarbmj","()V",false);v.visitInsn(RETURN);});
        method(w,"click","(LkacyVKUNru;I)V",v->{
            menuObject(v);v.visitVarInsn(ILOAD,1);v.visitFieldInsn(PUTFIELD,"ceMGdFNBJnq","PqmRMgzgwbH","I");
            menuObject(v);v.visitInsn(ICONST_0);v.visitFieldInsn(PUTFIELD,"ceMGdFNBJnq","xWSfQVLnUHeHB","Z");
            v.visitVarInsn(ALOAD,0);v.visitVarInsn(ILOAD,1);v.visitMethodInsn(INVOKEVIRTUAL,"kacyVKUNru","elementAt","(I)Ljava/lang/Object;",false);
            v.visitTypeInsn(CHECKCAST,"bmxqULQDW");v.visitMethodInsn(INVOKEVIRTUAL,"bmxqULQDW","qHSDVwkeNovng","()V",false);v.visitInsn(RETURN);
        });
        w.visitEnd();
        return w.toByteArray();
    }
    static void menuObject(MethodVisitor v) {v.visitFieldInsn(GETSTATIC,"NBAFgUXWGCkgPq","peMayxrZEBA","LceMGdFNBJnq;");}
    static void method(ClassWriter w,String n,String d,java.util.function.Consumer<MethodVisitor> body) {
        MethodVisitor v=w.visitMethod(ACC_PUBLIC|ACC_STATIC,n,d,null,null);v.visitCode();body.accept(v);v.visitMaxs(0,0);v.visitEnd();
    }
    static byte[] observe(byte[] bytes,int kind) {
        ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
            public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                MethodVisitor v=super.visitMethod(a,n,d,s,ex);
                if(kind==1 && ((n.equals("hrqSMtaRC") && d.equals("(Ljava/lang/String;)V")) || (n.equals("peMayxrZEBA") && d.equals("(Ljava/lang/String;ILjava/lang/Object;)V")))) {
                    noticeObserver++;
                    return new MethodVisitor(ASM9,v) {
                        public void visitCode() {super.visitCode();visitVarInsn(ALOAD,0);visitMethodInsn(INVOKESTATIC,"AutoPetRuntime","sawNotice","(Ljava/lang/String;)V",false);}
                    };
                }
                boolean hit=n.equals("peMayxrZEBA") && d.equals(kind==0?"(LkacyVKUNru;)V":"(Ljava/lang/String;ILjava/lang/Object;ILjava/lang/Object;)V");
                if(!hit)return v;
                if(kind==0)menuObserver++;else confirmObserver++;
                return new MethodVisitor(ASM9,v) {
                    public void visitInsn(int op) {
                        if(op==RETURN) {
                            if(kind==0) {visitVarInsn(ALOAD,1);visitMethodInsn(INVOKESTATIC,"AutoPetRuntime","sawMenu","(LkacyVKUNru;)V",false);}
                            else {visitVarInsn(ALOAD,0);visitVarInsn(ILOAD,1);visitVarInsn(ALOAD,2);visitVarInsn(ILOAD,3);visitMethodInsn(INVOKESTATIC,"AutoPetRuntime","sawConfirm","(Ljava/lang/String;ILjava/lang/Object;I)V",false);}
                        }
                        super.visitInsn(op);
                    }
                };
            }
        },ClassReader.SKIP_DEBUG);return w.toByteArray();
    }
    static byte[] packets(byte[] bytes) {
        ClassWriter w=new ClassWriter(ClassWriter.COMPUTE_MAXS);
        new ClassReader(bytes).accept(new ClassVisitor(ASM9,w) {
            public MethodVisitor visitMethod(int a,String n,String d,String s,String[] ex) {
                if(n.equals("WRmEtfNMLdMb") && d.equals("(LWZKDTXfSh;)V")) {packetObserver++;return super.visitMethod(a,"autoPetOriginalMessage",d,s,ex);}
                return super.visitMethod(a,n,d,s,ex);
            }
            public void visitEnd() {
                MethodVisitor v=super.visitMethod(ACC_PUBLIC|ACC_FINAL,"WRmEtfNMLdMb","(LWZKDTXfSh;)V",null,null);v.visitCode();
                v.visitVarInsn(ALOAD,1);v.visitMethodInsn(INVOKESTATIC,"AutoPetRuntime","beforePacket","(LWZKDTXfSh;)V",false);
                v.visitVarInsn(ALOAD,0);v.visitVarInsn(ALOAD,1);v.visitMethodInsn(INVOKEVIRTUAL,"yRvgxykm","autoPetOriginalMessage","(LWZKDTXfSh;)V",false);
                v.visitMethodInsn(INVOKESTATIC,"AutoPetRuntime","afterPacket","()V",false);v.visitInsn(RETURN);v.visitMaxs(0,0);v.visitEnd();super.visitEnd();
            }
        },ClassReader.SKIP_DEBUG);return w.toByteArray();
    }
}
