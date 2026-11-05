import java.util.ArrayList;
public class TestTransport implements qHSDVwkeNovng {
    public static String last,notice;
    public static int a,b,c,buys,confirms,lists;
    public static ArrayList<String> log=new ArrayList<String>();
    public static Runnable duringPacket;
    public static void packetEntered() {
        if(!Thread.holdsLock(AutoPetRuntime.packetLock())) throw new RuntimeException("Native parser races Auto inventory reads");
        Runnable hook=duringPacket;duringPacket=null;if(hook!=null)hook.run();
    }
    public static void record(String name,int x,int y,int z) {
        last=name;a=x;b=y;c=z;log.add(name+":"+x+":"+y+":"+z);
        if(name.equals("buy") || name.equals("box"))++buys;if(name.equals("confirm"))++confirms;
        if(name.equals("list"))++lists;
    }
    public static void notice(String text) {notice=text;}
    public static void wire(WZKDTXfSh message) {
        try {
            byte[] bytes=message.EgoDKTjkxEUMu();
            java.io.DataInputStream in=new java.io.DataInputStream(new java.io.ByteArrayInputStream(bytes));
            int command=message.GAuWiEMckbFHeh;
            if(command==13) {int source=in.readUnsignedByte(),index=in.readUnsignedByte();record("buy",source,index,in.available()==0?1:in.readUnsignedShort());}
            else if(command==16) {if(bytes.length!=1)throw new RuntimeException("box frame must have one slot byte");record("box",in.readUnsignedByte(),0,0);}
            else if(command==42) record("info",in.readUnsignedByte(),in.readUnsignedByte(),0);
            else if(command==11) record("equip",in.readUnsignedByte(),0,0);
            else if(command==107) record("confirm",in.readUnsignedByte(),0,0);
            else if(command==-30 && in.readByte()==-103) record("list",in.readUnsignedByte(),0,0);
            else throw new RuntimeException("Unexpected wire command "+command);
        } catch(java.io.IOException e) {throw new RuntimeException(e);}
    }
    public void peMayxrZEBA(int id,Object value) {record("menu",id,0,0);}
}
