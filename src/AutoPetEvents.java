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
    public static volatile int giftTX,giftTXCommand,giftTXToken;
    public static volatile String giftTXCode;
    public static volatile long giftTXAt;
    public static volatile int replies,shops,shopSource,catalogs;
    public static final int[] catalogRevisions=new int[256];
    public static void shop(int source){shopSource=source;++shops;}
    private static final String[] replyText=new String[12];
    private static final int[] replySerial=new int[12],replyNpc=new int[12],replyStatus=new int[12];
    private static int command,kind,code,npc;
    private static String title,claimed,message;
    private static boolean ok;
    public static void before(WZKDTXfSh packet) {
        command=packet==null?0:packet.GAuWiEMckbFHeh;kind=-1;npc=-1;title=claimed=message=null;
        if(command!=92 && command!=-33 && command!=-24 && command!=-25 && command!=38 && command!=53 && command!=33 && command!=31)return;
        try {
            DataInputStream in=AutoPetAccess.input(packet);in.mark(in.available()+1);
            try {
                if(command==31){npc=4;kind=4;}
                else if(command==33){npc=in.readUnsignedByte();kind=4;}
                else if(command==92){title=in.readUTF();code=in.readShort();kind=92;}
                else if(command==-24 || command==-25) {message=in.readUTF();kind=3;}
                else if(command==38) {npc=in.readShort();message=in.readUTF();kind=3;}
                else if(command==53) {title=in.readUTF();if(!title.equals("typemoi")){message=in.readUTF();kind=3;}}
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
        if(kind==3)reply(message,npc);
        if(kind==4)catalogRevisions[npc]=++catalogs;
    }
    public static void menu(kacyVKUNru choices){menu=choices;++menus;}
    // Keep relevant replies even if several unrelated notifications arrive before Auto's next tick.
    private static void reply(String text,int sourceNpc) {
        int result=AutoPetLevel.codeResult(text);if(result==0)return;
        int serial=replies+1,index=(serial-1)%replyText.length;
        replyText[index]=text.length()>480?text.substring(0,480):text;replyNpc[index]=sourceNpc;
        replyStatus[index]=result;replySerial[index]=serial;replies=serial;
    }
    public static String codeReply(int serial,int sourceNpc) {
        int index=(serial-1)%replyText.length;
        return replySerial[index]==serial && (replyNpc[index]<0 || replyNpc[index]==sourceNpc)?replyText[index]:null;
    }
    public static int codeReplyStatus(int serial) {return replyStatus[(serial-1)%replyText.length];}
    public static void notice(String text){notice=text;++notices;reply(text,-1);}
    /** Called only after the original socket writer successfully flushes a packet. */
    public static void transmitted(WZKDTXfSh packet) {
        if(packet==null || (packet.GAuWiEMckbFHeh!=92 && packet.GAuWiEMckbFHeh!=-33))return;
        try {
            DataInputStream in=new DataInputStream(new java.io.ByteArrayInputStream(packet.EgoDKTjkxEUMu()));
            int token=-1;
            if(packet.GAuWiEMckbFHeh==92)token=in.readShort();else if(in.readUnsignedByte()!=1)return;
            String value=in.readUTF();if(in.available()!=0)return;
            giftTXCommand=packet.GAuWiEMckbFHeh;giftTXToken=token;giftTXCode=value;giftTXAt=System.currentTimeMillis();++giftTX;
        }catch(Exception ignored){}
    }
    public static void confirm(String text,int value){confirmText=text;confirmCode=value;++confirms;}
}
