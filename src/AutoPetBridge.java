import javax.microedition.lcdui.*;

/** Native UI hooks and the Tashino runtime controller. */
public final class AutoPetBridge {
    private static int pressed;
    private static final int AUTO_W=60,TL_W=44,GAP=5,BUTTON_H=26;
    private static final int MENU_ID=119950;
    private static Display display() {return Display.getDisplay(GameMidlet.peMayxrZEBA);}
    public static void open() {
        if(AutoPetRuntime.running()) {notice("Hãy bấm Auto để dừng trước khi sửa cấu hình");return;}
        try {
            Display d=display();d.setCurrent(new AutoPetUI(d,d.getCurrent()));
        } catch(Exception e) {notice(e.getMessage());}
    }
    public static boolean menu(int id) {
        if(id!=MENU_ID) return false;
        open();return true;
    }
    public static void addMenu(kacyVKUNru menu) {
        menu.insertElementAt(new bmxqULQDW("AutoPet/LC",MENU_ID),0);
    }
    private static int y() {return (NBAFgUXWGCkgPq.gxpcPAQGYwZ?16:BWWvgXUCGmx.ydijJozSmduyg)+44;}
    private static int x() {return Math.max(2,NBAFgUXWGCkgPq.DndwYNCrvBa-AUTO_W-TL_W-GAP-4);}
    private static void button(rHNGbrXZGUD g,int id,int x,int y,int width,String label,boolean on) {
        if(pressed==id)y++;
        g.nRvPqUUFvqMR(0x101614);g.EgoDKTjkxEUMu(x,y+2,width,BUTTON_H,8,8);
        g.nRvPqUUFvqMR(on?0x244d35:0x342a22);g.EgoDKTjkxEUMu(x,y,width,BUTTON_H,8,8);
        g.nRvPqUUFvqMR(on?0x326d48:0x58432b);g.EgoDKTjkxEUMu(x+1,y+1,width-2,11,6,6);
        g.nRvPqUUFvqMR(pressed==id?0xf4d387:(on?0xb6ce8b:0xb08d54));
        g.peMayxrZEBA(x,y,width-1,BUTTON_H-1,8,8);
        g.nRvPqUUFvqMR(on?0x9cf09b:0x85827a);g.EgoDKTjkxEUMu(x+7,y+10,5,5,4,4);
        wBpDHXtSRtR.LBtrnuKVnvuG.EgoDKTjkxEUMu(g,label,x+width/2+4,y+5,2);
    }
    public static void paint(rHNGbrXZGUD g) {
        g.payNGrJwSbhcCy(0,0,NBAFgUXWGCkgPq.DndwYNCrvBa,NBAFgUXWGCkgPq.SXPEzvYGy);
        button(g,1,x(),y(),AUTO_W,"Auto",AutoPetRuntime.running());
        button(g,2,x()+AUTO_W+GAP,y(),TL_W,"TL",AutoPetHunt.realmLeaders());
    }
    private static int hit(int px,int py) {
        if(py<y() || py>=y()+BUTTON_H) return 0;
        int left=x();
        if(px>=left && px<left+AUTO_W) return 1;
        if(px>=left+AUTO_W+GAP && px<left+AUTO_W+GAP+TL_W) return 2;
        return 0;
    }
    public static boolean pointer(int x,int y,int kind) {
        // QQwLYhYdpMaF's active screen must be the game screen.
        if(!AutoPetAccess.isGame()) {pressed=0;return false;}
        int scale=Math.max(1,rHNGbrXZGUD.YDPUzPRaKKz);
        x/=scale;y/=scale;
        int hit=hit(x,y);
        if(kind==0) {pressed=hit;return hit!=0;}
        if(kind==1) return pressed!=0;
        int captured=pressed;pressed=0;
        if(captured!=0 && captured==hit) {
            if(captured==1) toggle();
            else try {AutoPetHunt.toggleRealmLeaders();}catch(Exception e){notice(e.getMessage());}
        }
        return captured!=0;
    }
    public static void toggle() {
        AutoPetRuntime.toggle();
    }
    public static void notice(String text) {
        showNotice(text,Alert.FOREVER);
    }
    public static void complete(String text) {
        showNotice(text,2000);
    }
    private static void showNotice(String text,int timeout) {
        Display d=display();Displayable prev=d.getCurrent();
        Alert a=new Alert("AutoPet/LC",text,null,AlertType.INFO);a.setTimeout(timeout);
        d.setCurrent(a,prev);
    }
}
