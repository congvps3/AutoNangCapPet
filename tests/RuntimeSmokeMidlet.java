import java.lang.reflect.*;
import javax.microedition.midlet.MIDlet;

/** Actual obfuscated classes and generated accessors, with all send sites mocked. */
public class RuntimeSmokeMidlet extends MIDlet {
    static int checks;
    static Object player;
    static wpiwfWAXuWze pet;
    static wpiwfWAXuWze[] bag=new wpiwfWAXuWze[30],body=new wpiwfWAXuWze[20];
    static void check(boolean c,String m) {if(!c)throw new RuntimeException(m+"; last="+TestTransport.last+" notice="+TestTransport.notice);++checks;}
    static void field(Class<?> owner,Object target,String name,Class<?> type,Object value) throws Exception {
        for(Field f:owner.getDeclaredFields())if(f.getName().equals(name) && f.getType()==type){f.setAccessible(true);f.set(target,value);return;}
        throw new RuntimeException("Field missing "+owner+" "+name+" "+type);
    }
    static Object currentPlayer() throws Exception {
        for(Method m:oFpffzUyucjDJX.class.getDeclaredMethods())if(m.getName().equals("rdmjdEJDQ") && m.getParameterTypes().length==0 && m.getReturnType()==oFpffzUyucjDJX.class)return m.invoke(null);
        throw new RuntimeException("player method missing");
    }
    static void config(boolean petEnabled,boolean mikaEnabled,int attempts) throws Exception {
        AutoPetConfig a=new AutoPetConfig();a.enabled=petEnabled;a.receive=true;a.train=true;a.upgrade=true;a.selected=3;a.attempts=attempts;a.target=1;
        AutoPetConfig b=new AutoPetConfig();b.enabled=mikaEnabled;b.receive=true;
        field(AutoPetUI.class,null,"saved",AutoPetConfig[].class,new AutoPetConfig[]{a,b});
        field(AutoPetUI.class,null,"loaded",boolean.class,Boolean.TRUE);
    }
    static wpiwfWAXuWze item(int id,int source,int slot) {
        wpiwfWAXuWze it=new wpiwfWAXuWze();
        it.EgoDKTjkxEUMu=new sVUYcBLt((short)id,(byte)10,(byte)0,id==1305?"Pet Long":"Long Châu Mika2","",(short)0,(short)0,(short)0,false);
        it.tTqVmuHPEhAbg=source;it.AZmTbjgvi=slot;options(it,1);return it;
    }
    static void options(wpiwfWAXuWze it,int mask) {
        it.ScymBikfyop=new kacyVKUNru();
        for(int i=0;i<2;i++)if((mask&(1<<i))!=0) {
            eNSzLVSc op=new eNSzLVSc();op.peMayxrZEBA=new HcmUnYtL();op.peMayxrZEBA.wLHNXXbvUSit=AutoPetConfig.OPTIONS[i];it.ScymBikfyop.addElement(op);
        }
    }
    static void event(int cmd,int source,int slot) throws Exception {
        WZKDTXfSh msg=new WZKDTXfSh((byte)cmd,new byte[]{(byte)source,(byte)slot});
        AutoPetRuntime.beforePacket(msg);
        check(AutoPetAccess.input(msg).readUnsignedByte()==source,"packet observer consumed bytes");
        AutoPetRuntime.afterPacket();
    }
    static void info(wpiwfWAXuWze it) throws Exception {it.ffWUPzwfbDA=true;event(42,it.tTqVmuHPEhAbg,it.AZmTbjgvi);AutoPetRuntime.tick();}
    static void rootAndShop() throws Exception {
        kacyVKUNru choices=new kacyVKUNru();TestTransport callback=new TestTransport();
        choices.addElement(new bmxqULQDW("Nhận Vật Phẩm",callback,101,null));
        choices.addElement(new bmxqULQDW("Luyện Trang Bị",callback,102,null));
        choices.addElement(new bmxqULQDW("Nâng cấp vật phẩm",callback,103,null));
        AutoPetRuntime.sawMenu(choices);AutoPetRuntime.tick();
        check(TestTransport.last.equals("menu"),"native menu callback not invoked");
        AutoPetRuntime.sawShop(52);AutoPetRuntime.tick();
        check(TestTransport.last.equals("menu"),"shop-open redundantly requested an arriving list");
        field(zxnsayePWpajv.class,null,"oXAaoinDuwaN",wpiwfWAXuWze[].class,new wpiwfWAXuWze[]{item(1305,32,0),item(1282,32,1)});
        event(33,32,0);AutoPetRuntime.tick();
        check(TestTransport.last.equals("buy") && TestTransport.a==32,"wrong shop source used");
    }
    static void confirm(boolean upgrade) {
        AutoPetRuntime.sawConfirm(upgrade?"Nâng cấp: Pet Long\nCần: 1.000 lượng.\nTỉ lệ thành công: 100%.\nXác nhận nâng cấp?":"Xác nhận luyện vật phẩm đã chọn?",8890,Integer.valueOf(7),8891);
        AutoPetRuntime.tick();check(TestTransport.last.equals("confirm") && TestTransport.a==7,"wrong opaque confirmation code");
    }
    static void refreshNow() throws Exception {
        Field instance=AutoPetRuntime.class.getDeclaredField("INSTANCE");instance.setAccessible(true);
        field(AutoPetRuntime.class,instance.get(null),"nextInfo",long.class,Long.valueOf(0));AutoPetRuntime.tick();
        check(TestTransport.last.equals("info"),"fresh actual item info not requested");
    }
    static void checkSetupMenu(String method) throws Exception {
        Method build=zxnsayePWpajv.class.getDeclaredMethod(method);build.setAccessible(true);build.invoke(null);
        Field f=AutoPetRuntime.class.getDeclaredField("menu");f.setAccessible(true);
        kacyVKUNru choices=(kacyVKUNru)f.get(null);
        bmxqULQDW first=(bmxqULQDW)choices.elementAt(0);
        check(first.oXAaoinDuwaN.equals("AutoPet/LC"),method+" does not show AutoPet/LC first");
        int count=0;
        for(int i=0;i<choices.size();i++)if(((bmxqULQDW)choices.elementAt(i)).oXAaoinDuwaN.equals("AutoPet/LC"))++count;
        check(count==1,method+" has duplicate AutoPet/LC entries");
        first.qHSDVwkeNovng();
        check(TestTransport.last.equals("setup"),method+" entry does not dispatch to settings");
    }
    protected void startApp() {
        try {
            sVdEtbWKGxAKR.QWSFikNNoKDGiu();
            field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)22));
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",ceMGdFNBJnq.class,new ceMGdFNBJnq());
            HAeSnYtYmns t=new HAeSnYtYmns();t.mvjvhHcftNANDP=9;t.wLHNXXbvUSit="Tashino";
            field(gsxwWgJt.class,null,"peMayxrZEBA",HAeSnYtYmns[].class,new HAeSnYtYmns[]{t});
            kacyVKUNru ns=new kacyVKUNru();ns.addElement(new gsxwWgJt(0,0,100,100,0));
            field(zxnsayePWpajv.class,null,"HSjEMvafS",kacyVKUNru.class,ns);
            player=currentPlayer();field(oFpffzUyucjDJX.class,player,"peMayxrZEBA",wpiwfWAXuWze[].class,bag);
            field(oFpffzUyucjDJX.class,player,"rdmjdEJDQ",wpiwfWAXuWze[].class,body);
            checkSetupMenu("GRYYuFfQZGmfH");
            checkSetupMenu("xbqjzadR");
            config(true,false,2);pet=item(1305,3,0);bag[0]=pet;
            AutoPetRuntime.toggle();check(TestTransport.last.equals("info") && TestTransport.a==3,"boot did not request bag stats");
            info(pet);check(TestTransport.last.equals("equip"),"pet not equipped before training");
            bag[0]=null;body[0]=pet;pet.tTqVmuHPEhAbg=5;AutoPetRuntime.tick();
            check(TestTransport.last.equals("npc") && TestTransport.a==9,"NPC ID was guessed rather than loaded");
            rootAndShop();confirm(false);refreshNow();options(pet,3);info(pet);
            check(TestTransport.last.equals("npc"),"all matching options did not open upgrade menu");
            rootAndShop();confirm(true);refreshNow();pet.yKHpKzQyZroJ=1;info(pet);
            check(!AutoPetRuntime.running() && TestTransport.notice.indexOf("đủ cấp 1")>=0,"target-level completion failed");
            config(true,false,1);pet.yKHpKzQyZroJ=0;options(pet,1);
            AutoPetRuntime.toggle();info(pet);rootAndShop();confirm(false);refreshNow();info(pet);
            check(!AutoPetRuntime.running() && TestTransport.notice.indexOf("Không nâng cấp")>=0,"exhausted training upgraded");
            config(true,false,2);AutoPetRuntime.toggle();info(pet);rootAndShop();
            int confirms=TestTransport.confirms;AutoPetRuntime.toggle();
            AutoPetRuntime.sawConfirm("Xác nhận luyện vật phẩm đã chọn?",8890,Integer.valueOf(8),8891);AutoPetRuntime.tick();
            check(!AutoPetRuntime.running() && TestTransport.confirms==confirms && TestTransport.notice.equals("đã dừng auto"),"late confirmation advanced stopped Auto");
            config(false,true,0);AutoPetRuntime.toggle();rootAndShop();
            check(TestTransport.b==1,"Mika receive used Pet Long index");
            wpiwfWAXuWze mika=item(1282,3,1);bag[1]=mika;refreshNow();info(mika);
            check(!AutoPetRuntime.running(),"Mika receiving did not finish");
            bag[1]=null;config(true,false,2);options(pet,1);
            AutoPetRuntime.toggle();info(pet);rootAndShop();
            int beforeWrong=TestTransport.confirms;
            AutoPetRuntime.sawConfirm("Xác nhận bán vật phẩm?",8890,Integer.valueOf(9),8891);AutoPetRuntime.tick();
            check(!AutoPetRuntime.running() && TestTransport.confirms==beforeWrong,"unrelated confirmation was accepted");
            config(true,false,2);AutoPetRuntime.toggle();info(pet);rootAndShop();
            int beforeError=TestTransport.buys;
            AutoPetRuntime.sawNotice("Không đủ lượng");AutoPetRuntime.tick();
            check(!AutoPetRuntime.running() && TestTransport.buys==beforeError && TestTransport.notice.indexOf("Không đủ lượng")>=0,"resource error did not stop runtime");
            // Real servers may send the list before the shop-open packet; it must not be discarded.
            config(false,true,0);bag[1]=null;AutoPetRuntime.toggle();
            kacyVKUNru receiveMenu=new kacyVKUNru();
            receiveMenu.addElement(new bmxqULQDW("Nhận Vật Phẩm",new TestTransport(),101,null));
            AutoPetRuntime.sawMenu(receiveMenu);AutoPetRuntime.tick();
            QfMjPqGqZaRx.EgoDKTjkxEUMu(item(1282,32,9).EgoDKTjkxEUMu);
            // Actual controller and binary list packet, not an event-only fixture.
            new yRvgxykm().WRmEtfNMLdMb(new WZKDTXfSh((byte)33,new byte[]{32,1,9,5,2}));
            int earlyBuys=TestTransport.buys;
            new zxnsayePWpajv().yXFfVgpsUAFd(52);
            int nativeRequests=TestTransport.lists;
            AutoPetRuntime.tick();AutoPetRuntime.tick();
            check(TestTransport.buys==earlyBuys+1 && TestTransport.b==9,"fresh list received before shop-open was lost");
            check(TestTransport.lists==nativeRequests,"fresh native list caused a duplicate list request");
            AutoPetRuntime.toggle();
            // A list from a previous operation or another source must not trigger receiving.
            config(false,true,0);AutoPetRuntime.toggle();
            AutoPetRuntime.sawMenu(receiveMenu);AutoPetRuntime.tick();
            AutoPetRuntime.sawShop(52);int staleBuys=TestTransport.buys;AutoPetRuntime.tick();
            check(TestTransport.buys==staleBuys,"old cached shop list triggered receiving");
            Field instance=AutoPetRuntime.class.getDeclaredField("INSTANCE");instance.setAccessible(true);
            field(AutoPetRuntime.class,instance.get(null),"shopRequestAt",long.class,Long.valueOf(0));
            int reads=TestTransport.lists;AutoPetRuntime.tick();AutoPetRuntime.tick();
            check(TestTransport.lists==reads+1 && TestTransport.buys==staleBuys,"fallback must be one read-only request");
            event(33,2,0);AutoPetRuntime.tick();
            check(TestTransport.buys==staleBuys,"unrelated shop source advanced receiving");
            new yRvgxykm().WRmEtfNMLdMb(new WZKDTXfSh((byte)33,new byte[]{32,1,9,5,2}));
            // An unrelated later packet must not hide the matching fresh list.
            event(33,2,0);AutoPetRuntime.tick();AutoPetRuntime.tick();
            check(TestTransport.buys==staleBuys+1 && TestTransport.b==9,"fresh late list was lost or receiving duplicated");
            AutoPetRuntime.toggle();
            System.out.println("RUNTIME_SMOKE_PASS "+checks+" (actual game classes, mocked transport; no live server)");
        } catch(Throwable t) {System.out.println("RUNTIME_SMOKE_FAIL "+t);t.printStackTrace();}
        notifyDestroyed();
    }
    protected void pauseApp() {}
    protected void destroyApp(boolean b) {}
}
