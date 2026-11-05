import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.*;

/** Exercises real Canvas events and RMS under MicroEmulator, without game login. */
public class UiSmokeMidlet extends MIDlet {
    private static int checks;
    private static void check(boolean c,String message) {
        if(!c) throw new RuntimeException(message);
        ++checks;
    }
    private void tap(AutoPetUI ui,int x,int y) {
        ui.pointerPressed(x,y);ui.pointerReleased(x,y);
    }
    protected void startApp() {
        try {
            Display d=Display.getDisplay(this);
            Canvas previous=new Canvas() {protected void paint(Graphics g) {}};
            d.setCurrent(previous);
            AutoPetUI ui=new AutoPetUI(d,previous);d.setCurrent(ui);
            int down=ui.getKeyCode(Canvas.DOWN),fire=ui.getKeyCode(Canvas.FIRE);
            ui.keyPressed(down);ui.keyPressed(fire); // disabled option cannot change
            ui.keyPressed(-7);
            check(AutoPetUI.settings()[0].selected==0,"disabled group editable");
            ui=new AutoPetUI(d,previous);d.setCurrent(ui);
            tap(ui,30,56); // enable Pet Long
            ui.keyPressed(down);ui.keyPressed(fire); // first option
            for(int i=0;i<12;i++) ui.keyPressed(down);
            ui.keyPressed(fire); // train checkbox (focus 13)
            ui.keyPressed(-7);
            AutoPetConfig[] saved=AutoPetUI.settings();
            check(saved[0].enabled && saved[0].selected==1 && saved[0].train,"checkboxes not saved");
            check(!saved[1].enabled && saved[1].selected==0,"Pet settings affected Mika");
            ui=new AutoPetUI(d,previous);d.setCurrent(ui);
            tap(ui,30,56); // disable in draft, close without saving
            ui.keyPressed(-6);
            check(AutoPetUI.settings()[0].enabled,"cancel overwrote saved settings");
            ui=new AutoPetUI(d,previous);d.setCurrent(ui);
            ui.paint(Image.createImage(ui.getWidth(),ui.getHeight()).getGraphics());
            check(ui.getWidth()>0 && ui.getHeight()>0,"Canvas has no size");
            System.out.println("UI_SMOKE_PASS "+checks+" (Canvas + RMS, no live server)");
        } catch(Throwable t) {System.out.println("UI_SMOKE_FAIL "+t);t.printStackTrace();}
        notifyDestroyed();
    }
    protected void pauseApp() {}
    protected void destroyApp(boolean unconditional) {}
}
