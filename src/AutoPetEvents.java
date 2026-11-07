import java.io.DataInputStream;

/** Packet observations are published only after the native parser returns. */
public final class AutoPetEvents {
    public static volatile int menus,notices,inputs,giftWindows,gifts;
    public static volatile kacyVKUNru menu;
    public static volatile String notice,inputTitle,giftCode,giftText;
    public static volatile int inputCode;
    public static volatile boolean giftOK;
    public static volatile int confirms,confirmCode;
    public static volatile String confirmText;
    private static int command,kind,code;
    private static String title,claimed,message;
    private static boolean ok;
    public static void before(WZKDTXfSh packet) {
        command=packet==null?0:packet.GAuWiEMckbFHeh;kind=-1;title=claimed=message=null;
        if(command!=92 && command!=-33)return;
        try {
            DataInputStream in=AutoPetAccess.input(packet);in.mark(in.available()+1);
            try {
                if(command==92){title=in.readUTF();code=in.readShort();kind=92;}
                else {
                    int sub=in.readUnsignedByte();
                    if(sub==0) {
                        int size=in.readShort();if(size<0)throw new java.io.IOException();
                        for(int i=0;i<size;i++) {
                            in.readInt();in.readByte();in.readUTF();in.readInt();in.readInt();in.readInt();
                            rewards(in);in.readByte();
                        }
                        kind=0;
                    }
                    if(sub==1) {
                        ok=in.readUnsignedByte()==1;
                        if(ok){claimed=in.readUTF();in.readInt();in.readInt();in.readInt();rewards(in);}
                        else message=in.readUTF();
                        kind=1;
                    }
                }
            } finally {in.reset();}
        }catch(Exception ignored){kind=-1;}
    }
    private static void rewards(DataInputStream in) throws java.io.IOException {
        int size=in.readUnsignedByte();
        for(int i=0;i<size;i++){in.readInt();in.readInt();in.readUTF();}
    }
    public static void after() {
        if(kind==92){inputTitle=title;inputCode=code;++inputs;}
        if(kind==0)++giftWindows;
        if(kind==1){giftOK=ok;giftCode=claimed;giftText=message;++gifts;}
    }
    public static void menu(kacyVKUNru choices){menu=choices;++menus;}
    public static void notice(String text){notice=text;++notices;}
    public static void confirm(String text,int value){confirmText=text;confirmCode=value;++confirms;}
}
