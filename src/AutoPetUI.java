import javax.microedition.lcdui.*;
import javax.microedition.rms.RecordStore;

/** Canvas keeps Save at bottom right and prevents editing disabled groups. */
public final class AutoPetUI extends Canvas implements CommandListener {
    private static final String STORE="AutoPetLC.v1";
    private static AutoPetConfig[] saved={new AutoPetConfig(),new AutoPetConfig()};
    private static boolean loaded;
    private final AutoPetConfig[] draft;
    private final Display display;
    private final Displayable previous;
    private final Font font=Font.getFont(Font.FACE_SYSTEM,Font.STYLE_PLAIN,Font.SIZE_SMALL);
    private int tab,top,focus,editing;
    private TextBox input;
    private final Command accept=new Command("OK",Command.OK,1);
    private final Command cancel=new Command("Huỷ",Command.CANCEL,2);

    public static AutoPetConfig[] settings() {
        load(); return new AutoPetConfig[]{saved[0].copy(),saved[1].copy()};
    }
    private static void load() {
        if(loaded) return;
        RecordStore rs=null;
        try {
            rs=RecordStore.openRecordStore(STORE,true);
            if(rs.getNumRecords()>0) saved=AutoPetConfig.decode(rs.getRecord(1));
            loaded=true;
        } catch(Exception e) {
            throw new RuntimeException("Không đọc được cấu hình AutoPet/LC");
        } finally {if(rs!=null) try {rs.closeRecordStore();} catch(Exception e) {}}
    }
    public AutoPetUI(Display d,Displayable prev) {
        display=d;previous=prev;draft=settings();setFullScreenMode(true);
    }
    private int rowHeight() {
        int width=Math.max(40,getWidth()-28),longest=0;
        for(int i=0;i<AutoPetConfig.OPTIONS.length;i++) longest=Math.max(longest,font.stringWidth(AutoPetConfig.OPTIONS[i]));
        // Leave one extra line for wrapping at word boundaries.
        return Math.max(22,((longest+width-1)/width+1)*font.getHeight()+8);
    }
    private int visible() {return Math.max(1,(getHeight()-72)/rowHeight());}
    private String label(int row) {
        AutoPetConfig c=draft[tab];
        if(row==0) return "Enable "+AutoPetConfig.NAMES[tab];
        if(row<=11) return AutoPetConfig.OPTIONS[row-1];
        if(row==12) return "Nhận khi hành trang chưa có";
        if(row==13) return "Luyện "+AutoPetConfig.NAMES[tab];
        if(row==14) return "Nâng cấp "+AutoPetConfig.NAMES[tab];
        if(row==15) return "Số lần luyện: "+c.attempts;
        return "Cấp nâng: "+c.target;
    }
    private boolean checked(int row) {
        AutoPetConfig c=draft[tab];
        if(row==0) return c.enabled;
        if(row<=11) return (c.selected & (1<<(row-1)))!=0;
        if(row==12) return c.receive;
        if(row==13) return c.train;
        return row==14 && c.upgrade;
    }
    protected void paint(Graphics g) {
        int w=getWidth(),h=getHeight(),rh=rowHeight();
        g.setColor(0x12202b);g.fillRect(0,0,w,h);g.setFont(font);
        g.setColor(0xffffff);g.drawString("AutoPet/LC r4",w/2,2,Graphics.TOP|Graphics.HCENTER);
        for(int i=0;i<2;i++) {
            g.setColor(i==tab?0x247c9e:0x30414f);g.fillRect(i*w/2,22,w/2-1,24);
            g.setColor(0xffffff);g.drawString(i==0?"Pet Long":"LC Mika2",i*w/2+w/4,26,Graphics.TOP|Graphics.HCENTER);
        }
        g.setClip(0,48,w,h-76);
        for(int row=top;row<17 && row<top+visible();row++) {
            int y=48+(row-top)*rh;
            boolean active=row==0 || draft[tab].enabled;
            if(row==focus) {g.setColor(active?0x355061:0x26343c);g.fillRect(0,y,w,rh);}
            g.setColor(active?0xeeeeee:0x68757d);
            if(row<=14) {
                g.drawRect(5,y+5,12,12);
                if(checked(row)) {g.drawLine(7,y+11,10,y+14);g.drawLine(10,y+14,15,y+7);}
            }
            drawLabel(g,label(row),row<=14?23:5,y+4,w-28);
        }
        g.setClip(0,0,w,h);
        if(top>0) {g.setColor(0xffffff);g.drawString("^",w-8,48,Graphics.TOP|Graphics.RIGHT);}
        if(top+visible()<17) {g.setColor(0xffffff);g.drawString("v",w-8,h-40,Graphics.TOP|Graphics.RIGHT);}
        g.setColor(0x233744);g.fillRect(0,h-26,w,26);
        g.setColor(0xffffff);g.drawString("Đóng",6,h-22,Graphics.TOP|Graphics.LEFT);
        g.setColor(0x32a070);g.fillRect(w-66,h-26,66,26);
        g.setColor(0xffffff);g.drawString("Lưu",w-12,h-22,Graphics.TOP|Graphics.RIGHT);
    }
    private void drawLabel(Graphics g,String text,int x,int y,int width) {
        while(text.length()>0) {
            int end=text.length();
            while(end>1 && font.stringWidth(text.substring(0,end))>width) --end;
            int space=text.lastIndexOf(' ',end);
            if(end<text.length() && space>0) end=space;
            g.drawString(text.substring(0,end),x,y,Graphics.TOP|Graphics.LEFT);
            text=text.substring(end).trim();y+=font.getHeight();
        }
    }
    private void select(int row) {
        if(row<0 || row>16) return;
        focus=row;
        AutoPetConfig c=draft[tab];
        if(row==0) c.enabled=!c.enabled;
        else if(!c.enabled) return;
        else if(row<=11) c.selected ^= 1<<(row-1);
        else if(row==12) c.receive=!c.receive;
        else if(row==13) c.train=!c.train;
        else if(row==14) c.upgrade=!c.upgrade;
        else {
            editing=row;
            input=new TextBox(row==15?"Số lần luyện":"Cấp nâng",String.valueOf(row==15?c.attempts:c.target),7,TextField.NUMERIC);
            input.addCommand(accept);input.addCommand(cancel);input.setCommandListener(this);
            display.setCurrent(input);
        }
        repaint();
    }
    private int downX,downY;
    private boolean dragged;
    protected void pointerPressed(int x,int y) {
        downX=x;downY=y;dragStart=y;dragged=false;
    }
    private void tap(int x,int y) {
        if(y>=22 && y<46) {tab=x<getWidth()/2?0:1;top=0;focus=0;repaint();return;}
        if(y>=getHeight()-26) {if(x>=getWidth()-66) save();else display.setCurrent(previous);return;}
        if(y>=48 && y<48+visible()*rowHeight()) select(top+(y-48)/rowHeight());
    }
    private int dragStart;
    protected void pointerDragged(int x,int y) {
        if(Math.abs(y-dragStart)>=rowHeight()) {
            dragged=true;
            top=Math.max(0,Math.min(17-visible(),top+(y<dragStart?1:-1)));
            dragStart=y;repaint();
        }
    }
    protected void pointerReleased(int x,int y) {
        if(!dragged && Math.abs(x-downX)<10 && Math.abs(y-downY)<10) tap(x,y);
        dragStart=0;
    }
    protected void keyPressed(int key) {
        if(key==-6) {display.setCurrent(previous);return;}
        if(key==-7) {save();return;}
        int action=getGameAction(key);
        if(action==LEFT || action==RIGHT) {tab=1-tab;top=0;focus=0;}
        else if(action==UP || action==DOWN) {
            focus=Math.max(0,Math.min(16,focus+(action==UP?-1:1)));
            if(focus<top) top=focus;
            if(focus>=top+visible()) top=focus-visible()+1;
        } else if(action==FIRE) select(focus);
        repaint();
    }
    private void error(String s,Displayable next) {
        Alert a=new Alert("AutoPet/LC",s,null,AlertType.ERROR);a.setTimeout(Alert.FOREVER);
        display.setCurrent(a,next);
    }
    private void save() {
        RecordStore rs=null;
        try {
            byte[] data=AutoPetConfig.encode(draft);
            rs=RecordStore.openRecordStore(STORE,true);
            if(rs.getNumRecords()==0) rs.addRecord(data,0,data.length);
            else rs.setRecord(1,data,0,data.length);
            saved=new AutoPetConfig[]{draft[0].copy(),draft[1].copy()};
            display.setCurrent(previous);
        } catch(Exception e) {error("Chưa lưu: "+e.getMessage(),this);}
        finally {if(rs!=null) try {rs.closeRecordStore();} catch(Exception e) {}}
    }
    public void commandAction(Command c,Displayable screen) {
        if(c==accept) {
            try {
                int value=Integer.parseInt(input.getString());
                if(value<0 || value>(editing==15?1000000:1000)) throw new NumberFormatException();
                if(editing==15) draft[tab].attempts=value;else draft[tab].target=value;
            } catch(Exception e) {error("Nhập số từ 0 đến "+(editing==15?1000000:1000),input);return;}
        }
        display.setCurrent(this);repaint();
    }
}
