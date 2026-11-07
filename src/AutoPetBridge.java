import javax.microedition.lcdui.*;

/** Native UI hooks and the Tashino runtime controller. */
public final class AutoPetBridge {
    private static int pressed;
    private static final int AUTO_W=38,TL_W=38,GAP=5,BUTTON_H=38;
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
        Image frame=AutoPetAccess.treoButton(pressed==id);
        if(frame!=null)g.peMayxrZEBA(frame,x+width/2,y+BUTTON_H/2,3);
        wBpDHXtSRtR font=on?wBpDHXtSRtR.PVQpZEqSNMpYBM:wBpDHXtSRtR.MtiBnBEFYaf;
        font.peMayxrZEBA(g,label,x+width/2,y+11,2,wBpDHXtSRtR.xNWwdhkGHYvrH);
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
    private static void showNotice(String text,int timeout) {
        Display d=display();Displayable prev=d.getCurrent();
        Alert a=new Alert("AutoPet/LC",text,null,AlertType.INFO);a.setTimeout(timeout);
        d.setCurrent(a,prev);
    }
}
