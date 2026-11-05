import javax.microedition.lcdui.*;

/** Native UI hooks and the Tashino runtime controller. */
public final class AutoPetBridge {
    private static boolean pressed;
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
        menu.addElement(new bmxqULQDW("AutoPet/LC",MENU_ID));
    }
    private static int y() {return (NBAFgUXWGCkgPq.gxpcPAQGYwZ?16:BWWvgXUCGmx.ydijJozSmduyg)+44;}
    public static void paint(rHNGbrXZGUD g) {
        AutoPetRuntime.tick();
        int x=NBAFgUXWGCkgPq.DndwYNCrvBa-60,y=y();
        g.payNGrJwSbhcCy(0,0,NBAFgUXWGCkgPq.DndwYNCrvBa,NBAFgUXWGCkgPq.SXPEzvYGy);
        g.nRvPqUUFvqMR(AutoPetRuntime.running()?0x277d45:0x24586e);g.mENQrCHeUeoRg(x,y,58,22);
        g.nRvPqUUFvqMR(0xa5d9ec);g.BMWCZFcnBgzHkh(x,y,58,22);
        wBpDHXtSRtR.LBtrnuKVnvuG.EgoDKTjkxEUMu(g,"Auto",x+29,y+4,2);
    }
    public static boolean pointer(int x,int y,int kind) {
        // QQwLYhYdpMaF's active screen must be the game screen.
        if(!AutoPetAccess.isGame()) return false;
        int scale=rHNGbrXZGUD.YDPUzPRaKKz;
        x/=scale;y/=scale;
        boolean hit=x>=NBAFgUXWGCkgPq.DndwYNCrvBa-60 && x<NBAFgUXWGCkgPq.DndwYNCrvBa-2 && y>=y() && y<y()+22;
        if(kind==0) {pressed=hit;return hit;}
        if(kind==1) return pressed;
        boolean captured=pressed;pressed=false;
        if(captured && hit) toggle();
        return captured;
    }
    public static void toggle() {
        AutoPetRuntime.toggle();
    }
    public static void notice(String text) {
        Display d=display();Displayable prev=d.getCurrent();
        Alert a=new Alert("AutoPet/LC",text,null,AlertType.INFO);a.setTimeout(Alert.FOREVER);
        d.setCurrent(a,prev);
    }
}
