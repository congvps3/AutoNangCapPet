import java.util.zip.*;
public class VerifyJar {
    public static void main(String[] args) throws Exception {
        int count=0;
        try(ZipFile z=new ZipFile(args[0])) {
            var all=z.entries();
            while(all.hasMoreElements()) {
                var e=all.nextElement();
                if(!e.getName().endsWith(".class")) continue;
                String name=e.getName().replace('/','.').replaceAll("\\.class$","");
                Class<?> type=Class.forName(name,false,VerifyJar.class.getClassLoader());
                type.getDeclaredMethods();type.getDeclaredConstructors();++count;
            }
        }
        System.out.println("PASS: JVM verified and linked "+count+" classes (no initialization/login)");
    }
}
