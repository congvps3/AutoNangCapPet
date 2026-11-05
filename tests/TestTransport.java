import java.util.ArrayList;
public class TestTransport implements qHSDVwkeNovng {
    public static String last,notice;
    public static int a,b,c,buys,confirms;
    public static ArrayList<String> log=new ArrayList<String>();
    public static void record(String name,int x,int y,int z) {
        last=name;a=x;b=y;c=z;log.add(name+":"+x+":"+y+":"+z);
        if(name.equals("buy"))++buys;if(name.equals("confirm"))++confirms;
    }
    public static void notice(String text) {notice=text;}
    public void peMayxrZEBA(int id,Object value) {record("menu",id,0,0);}
}
