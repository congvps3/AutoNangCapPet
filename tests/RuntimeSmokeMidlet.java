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
        AutoPetConfig b=a.copy();b.enabled=mikaEnabled;
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
    static void retryReadNow() throws Exception {
        Field f=AutoPetRuntime.class.getDeclaredField("INSTANCE");f.setAccessible(true);
        field(AutoPetRuntime.class,f.get(null),"infoRetryAt",long.class,Long.valueOf(0));AutoPetRuntime.tick();
    }
    static void concurrentMatchingInfo(final wpiwfWAXuWze it) throws Exception {
        HcmUnYtL[] defs=new HcmUnYtL[2];
        for(int i=0;i<2;i++) {defs[i]=new HcmUnYtL();defs[i].wLHNXXbvUSit=AutoPetConfig.OPTIONS[i];}
        field(zxnsayePWpajv.class,null,"peMayxrZEBA",HcmUnYtL[].class,defs);
        final Thread[] worker=new Thread[1];final Throwable[] error=new Throwable[1];
        TestTransport.duringPacket=new Runnable() {public void run() {
            // Emulate the native parser's temporary empty option vector while
            // the update thread tries to make its next train/use decision.
            options(it,0);
            final java.util.concurrent.CountDownLatch started=new java.util.concurrent.CountDownLatch(1);
            worker[0]=new Thread(new Runnable(){public void run(){started.countDown();try{AutoPetRuntime.tick();}catch(Throwable t){error[0]=t;}}});
            worker[0].start();
            try {started.await();worker[0].join(100);}catch(InterruptedException e){throw new RuntimeException(e);}
            check(worker[0].isAlive(),"Auto must wait until option packet is completely parsed");
        }};
        java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream out=new java.io.DataOutputStream(bytes);
        out.writeByte(3);out.writeByte(it.AZmTbjgvi);out.writeLong(0);out.writeInt(1);out.writeByte(0);
        out.writeByte(0);out.writeInt(10);out.writeByte(1);out.writeInt(20);out.flush();
        WZKDTXfSh packet=new WZKDTXfSh((byte)42,bytes.toByteArray());
        new yRvgxykm().WRmEtfNMLdMb(packet);
        worker[0].join(2000);
        check(!worker[0].isAlive() && error[0]==null,"Auto did not resume after complete packet");
        check(it.ffWUPzwfbDA && it.ScymBikfyop.size()==2,"native item packet must parse both wanted stats; ready="+it.ffWUPzwfbDA+" count="+it.ScymBikfyop.size()+" left="+AutoPetAccess.input(packet).available()+" type="+it.EgoDKTjkxEUMu.peMayxrZEBA+" sys="+it.NUZGwRkdSQ);
        AutoPetRuntime.tick();
    }
    static void openShop(int source,String title) throws Exception {
        java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream out=new java.io.DataOutputStream(bytes);
        out.writeByte(source);out.writeUTF(title);out.writeUTF("");out.flush();
        new yRvgxykm().WRmEtfNMLdMb(new WZKDTXfSh((byte)30,bytes.toByteArray()));
    }
    static void boxList() throws Exception {
        java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();java.io.DataOutputStream out=new java.io.DataOutputStream(bytes);
        out.writeLong(0);out.writeByte(2);
        for(int i=0;i<2;i++) {
            QfMjPqGqZaRx.EgoDKTjkxEUMu(item(i==0?1305:1282,4,i).EgoDKTjkxEUMu);
            out.writeShort(i==0?1305:1282);out.writeBoolean(true);out.writeByte(0);out.writeBoolean(false);out.writeInt(1);
        }
        out.flush();new yRvgxykm().WRmEtfNMLdMb(new WZKDTXfSh((byte)31,bytes.toByteArray()));
    }
    static void rootAndShop() throws Exception {
        kacyVKUNru choices=new kacyVKUNru();TestTransport callback=new TestTransport();
        choices.addElement(new bmxqULQDW("Nhận Vật Phẩm",callback,101,null));
        choices.addElement(new bmxqULQDW("Luyện Trang Bị",callback,102,null));
        choices.addElement(new bmxqULQDW("Nâng cấp vật phẩm",callback,103,null));
        AutoPetRuntime.sawMenu(choices);AutoPetRuntime.tick();
        check(TestTransport.last.equals("menu"),"native menu callback not invoked");
        int selectedAction=TestTransport.a;
        openShop(4,selectedAction==101?"Nhận Vật Phẩm":selectedAction==102?"Luyện":"Nâng cấp vật phẩm");
        int nativeReads=TestTransport.lists;AutoPetRuntime.tick();
        check(TestTransport.lists==nativeReads,"shop-open redundantly requested an arriving list");
        boxList();
        Field instance=AutoPetRuntime.class.getDeclaredField("INSTANCE");instance.setAccessible(true);
        field(AutoPetRuntime.class,instance.get(null),"shopCacheAt",long.class,Long.valueOf(0));
        AutoPetRuntime.tick();
        check(TestTransport.last.equals("box"),"native window 4 must execute action 111101 / packet 16, never buy packet 13");
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
    static void reset(boolean petEnabled,boolean mikaEnabled,int attempts) throws Exception {
        for(int i=0;i<bag.length;i++)bag[i]=null;
        for(int i=0;i<body.length;i++)body[i]=null;
        config(petEnabled,mikaEnabled,attempts);
        if(petEnabled)bag[0]=item(1305,3,0);
        if(mikaEnabled)bag[1]=item(1282,3,1);
    }
    static int count(String prefix) {int n=0;for(String entry:TestTransport.log)if(entry.startsWith(prefix))++n;return n;}
    static void levelFlow() throws Exception {
        reset(true,true,0);upgradeOnly(0,0);upgradeOnly(1,0);
        pet=body[10]=item(1305,5,10);wpiwfWAXuWze mika=body[11]=item(1282,5,11);bag[0]=null;bag[1]=null;
        SJSCaeScqbiua auto=new SJSCaeScqbiua();AutoPetAccess.setAuto(auto);
        field(oFpffzUyucjDJX.class,player,"BfndoYuX",int.class,Integer.valueOf(11001));
        field(oFpffzUyucjDJX.class,player,"dvKEjoqBk",String.class,"level-test");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(20));
        check(!AutoPetLevel.intercept(auto),"level 20 must keep original AutoLV");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(21));
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)1));
        auto.LAEAunqbxr();check(TestTransport.last.equals("travel") && TestTransport.a==22,"native Tan sat update must intercept at level 21 and route to Tone");
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)22));
        field(oFpffzUyucjDJX.class,player,"wLHNXXbvUSit",int.class,Integer.valueOf(1000));
        check(AutoPetLevel.intercept(auto) && TestTransport.last.equals("walk"),"must approach Tashino before starting Pet");
        field(oFpffzUyucjDJX.class,player,"wLHNXXbvUSit",int.class,Integer.valueOf(100));
        field(oFpffzUyucjDJX.class,player,"SgQHNwMh",int.class,Integer.valueOf(100));
        check(AutoPetLevel.intercept(auto) && AutoPetRuntime.running(),"Tone arrival starts saved AutoPet settings");
        check(AutoPetLevel.intercept(auto),"AutoLV must remain paused while Pet runs");
        info(pet);info(mika);rootAndShop();confirm(true);refreshNow();pet.yKHpKzQyZroJ=1;info(pet);
        check(AutoPetLevel.intercept(auto),"Pet-only completion must not resume before Mika");
        rootAndShop();AutoPetRuntime.sawConfirm("Nâng cấp: Long Châu Mika2\nXác nhận nâng cấp?",8890,Integer.valueOf(7),8891);AutoPetRuntime.tick();
        refreshNow();mika.yKHpKzQyZroJ=1;info(mika);
        check(AutoPetRuntime.completed() && !AutoPetRuntime.running(),"whole Pet workflow must report explicit success");
        check(!AutoPetLevel.intercept(auto) && AutoPetAccess.currentAuto()==auto,"success must immediately release same AutoLV worker");
        SJSCaeScqbiua next=new SJSCaeScqbiua();AutoPetAccess.setAuto(next);AutoPetLevel.watch();
        int cmds=TestTransport.log.size();check(!AutoPetLevel.intercept(next) && TestTransport.log.size()==cmds,"persistent completion must prevent repeated level-21 workflow");
        field(oFpffzUyucjDJX.class,player,"BfndoYuX",int.class,Integer.valueOf(11002));AutoPetLevel.watch();
        pet.yKHpKzQyZroJ=0;mika.yKHpKzQyZroJ=0;
        check(AutoPetLevel.intercept(next) && AutoPetRuntime.running(),"another character must have independent level-21 workflow");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();
        check(!AutoPetRuntime.running() && !AutoPetRuntime.completed(),"turning off Tan sat must cancel Pet without success or resuming AutoLV");
        AutoPetAccess.setAuto(next);AutoPetLevel.intercept(next);AutoPetRuntime.stop();
        check(AutoPetLevel.intercept(next) && AutoPetAccess.currentAuto()==null,"manual Pet stop must leave AutoLV stopped");
        field(oFpffzUyucjDJX.class,player,"BfndoYuX",int.class,Integer.valueOf(11003));AutoPetAccess.setAuto(next);
        body[10]=null;
        check(AutoPetLevel.intercept(next) && !AutoPetRuntime.running() && AutoPetAccess.currentAuto()==null,"Pet preflight failure must not resume AutoLV");
        field(oFpffzUyucjDJX.class,player,"BfndoYuX",int.class,Integer.valueOf(11004));AutoPetAccess.setAuto(next);
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)1));
        AutoPetLevel.intercept(next);field(AutoPetLevel.class,null,"deadline",long.class,Long.valueOf(0));
        check(AutoPetLevel.intercept(next) && AutoPetAccess.currentAuto()==null && TestTransport.notice.indexOf("chưa đến")>=0,"unreachable Tone must stop with timeout");
        check(AutoPetLevel.intercept(next) && !AutoPetRuntime.running(),"stale native worker must not restart canceled workflow");
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)22));
    }
    static void upgradeOnly(int target,int mask) throws Exception {
        AutoPetConfig[] settings=AutoPetUI.settings();settings[target].train=false;settings[target].selected=mask;
        field(AutoPetUI.class,null,"saved",AutoPetConfig[].class,settings);
    }
    static void useReply(int bagSlot,int equipSlot) throws Exception {
        wpiwfWAXuWze it=bag[bagSlot];bag[bagSlot]=null;body[equipSlot]=it;it.tTqVmuHPEhAbg=5;it.AZmTbjgvi=equipSlot;
        AutoPetRuntime.tick();check(TestTransport.last.equals("info") && TestTransport.a==5,"use must await actual equipped item info");info(it);
    }
    static void senderBytes() throws Exception {
        WZKDTXfSh packet=new WZKDTXfSh((byte)13);
        java.io.DataOutputStream out=null;
        for(Method m:WZKDTXfSh.class.getMethods())if(m.getName().equals("peMayxrZEBA") && m.getReturnType()==java.io.DataOutputStream.class && m.getParameterTypes().length==0)out=(java.io.DataOutputStream)m.invoke(packet);
        out.writeByte(32);out.writeByte(0);out.flush();
        LZitKUUsicXFt service=LZitKUUsicXFt.peMayxrZEBA();
        java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();
        field(LZitKUUsicXFt.class,service,"peMayxrZEBA",java.io.DataOutputStream.class,new java.io.DataOutputStream(bytes));
        Method send=LZitKUUsicXFt.class.getDeclaredMethod("fNmLnhTF",WZKDTXfSh.class);send.setAccessible(true);send.invoke(service,packet);
        byte[] wire=bytes.toByteArray();
        check(wire.length==5 && wire[0]==13 && wire[1]==0 && wire[2]==2 && wire[3]==32 && wire[4]==0,"original sender produced wrong frame");
        check(AutoPetRuntime.diagnostics().indexOf("TX 13 src=32 pos=0")>=0,"successful flush not observed");
        bytes.reset();packet=new WZKDTXfSh((byte)16);
        for(Method m:WZKDTXfSh.class.getMethods())if(m.getName().equals("peMayxrZEBA") && m.getReturnType()==java.io.DataOutputStream.class && m.getParameterTypes().length==0)out=(java.io.DataOutputStream)m.invoke(packet);
        out.writeByte(1);out.flush();send.invoke(service,packet);wire=bytes.toByteArray();
        check(wire.length==4 && wire[0]==16 && wire[1]==0 && wire[2]==1 && wire[3]==1,"box action must serialize as command 16 plus one index byte");
        check(AutoPetRuntime.diagnostics().indexOf("TX 16 pos=1")>=0,"native box send not retained in diagnostics");
    }
    protected void startApp() {
        try {
            sVdEtbWKGxAKR.QWSFikNNoKDGiu();
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",XxukxTaDkERtV.class,new XxukxTaDkERtV());
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",RjDwVJrHk.class,new RjDwVJrHk());
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",gqQMxdaie.class,new gqQMxdaie());
            field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)22));
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",ceMGdFNBJnq.class,new ceMGdFNBJnq());
            HAeSnYtYmns t=new HAeSnYtYmns();t.mvjvhHcftNANDP=9;t.wLHNXXbvUSit="Tashino";
            field(gsxwWgJt.class,null,"peMayxrZEBA",HAeSnYtYmns[].class,new HAeSnYtYmns[]{t});
            kacyVKUNru ns=new kacyVKUNru();ns.addElement(new gsxwWgJt(0,0,100,100,0));
            field(zxnsayePWpajv.class,null,"HSjEMvafS",kacyVKUNru.class,ns);
            player=currentPlayer();field(oFpffzUyucjDJX.class,player,"peMayxrZEBA",wpiwfWAXuWze[].class,bag);
            field(oFpffzUyucjDJX.class,player,"rdmjdEJDQ",wpiwfWAXuWze[].class,body);
            checkSetupMenu("GRYYuFfQZGmfH");checkSetupMenu("xbqjzadR");
            reset(true,false,2);pet=bag[0];int usedBefore=count("equip:");
            AutoPetRuntime.toggle();check(TestTransport.last.equals("info") && TestTransport.a==3 && TestTransport.b==0,"Pet must start at bag slot 1");
            info(pet);check(TestTransport.last.equals("npc") && TestTransport.a==9 && count("equip:")==usedBefore,"bag training must precede use");
            rootAndShop();check(count("menu:101")==0,"Auto must never select receiving");
            confirm(false);refreshNow();check(TestTransport.a==3 && TestTransport.b==0,"training must refresh bag item");
            int spent=TestTransport.buys;
            // A list refresh can replace or briefly remove the bag object while
            // a detail reply is in flight. Only repeat the non-consuming read.
            bag[0]=null;event(42,3,0);AutoPetRuntime.tick();
            check(AutoPetRuntime.running() && TestTransport.buys==spent,"temporary missing item must not stop or repeat training");
            retryReadNow();check(TestTransport.last.equals("info") && TestTransport.a==3 && TestTransport.b==0,"retry must read original bag slot only");
            bag[0]=pet;pet.ffWUPzwfbDA=false;event(42,3,0);AutoPetRuntime.tick();
            check(AutoPetRuntime.running() && TestTransport.buys==spent,"incomplete item details must wait");
            retryReadNow();int reads=count("info:");retryReadNow();
            check(count("info:")==reads,"read recovery must have a bounded retry count");
            concurrentMatchingInfo(pet);check(TestTransport.last.equals("equip") && TestTransport.a==0 && body[0]==null,"use must start only after matching stats");
            useReply(0,0);check(TestTransport.last.equals("npc"),"upgrade did not wait for equipped stats");
            rootAndShop();confirm(true);spent=TestTransport.buys;
            AutoPetRuntime.sawNotice("Nâng cấp thất bại");AutoPetRuntime.tick();
            check(AutoPetRuntime.running() && TestTransport.buys==spent,"upgrade failure must await fresh item before another attempt");
            refreshNow();info(pet);
            check(AutoPetRuntime.running() && TestTransport.last.equals("npc"),"unchanged level must start next upgrade cycle");
            rootAndShop();confirm(true);refreshNow();pet.yKHpKzQyZroJ=1;info(pet);
            check(!AutoPetRuntime.running() && TestTransport.notice.indexOf("đủ cấp 1")>=0,"Pet target-level completion");
            reset(true,false,1);pet=bag[0];usedBefore=count("equip:");
            AutoPetRuntime.toggle();info(pet);rootAndShop();confirm(false);refreshNow();info(pet);
            check(!AutoPetRuntime.running() && count("equip:")==usedBefore && bag[0]==pet && body[0]==null,"exhaustion must leave Pet in bag");
            check(TestTransport.notice.indexOf("Không nâng cấp")>=0,"exhaustion notice");
            reset(false,true,2);wpiwfWAXuWze mika=bag[1];AutoPetRuntime.toggle();
            check(TestTransport.last.equals("info") && TestTransport.b==1,"Mika must start at bag slot 2");info(mika);
            rootAndShop();check(TestTransport.a==1,"Mika used Pet shop index");confirm(false);refreshNow();options(mika,3);info(mika);
            check(TestTransport.last.equals("equip") && TestTransport.a==1,"Mika must use bag slot 2 after training");
            useReply(1,1);rootAndShop();
            AutoPetRuntime.sawConfirm("Nâng cấp: Long Châu Mika2\nXác nhận nâng cấp?",8890,Integer.valueOf(7),8891);AutoPetRuntime.tick();
            check(TestTransport.last.equals("confirm"),"Mika upgrade confirmation");refreshNow();mika.yKHpKzQyZroJ=1;info(mika);
            check(!AutoPetRuntime.running(),"Mika completion");
            reset(true,true,2);wpiwfWAXuWze bothPet=bag[0],bothMika=bag[1];options(bothPet,3);options(bothMika,3);
            AutoPetRuntime.toggle();info(bothPet);
            check(TestTransport.last.equals("info") && TestTransport.a==3 && TestTransport.b==1,"both enabled items must be read before acting");
            info(bothMika);check(TestTransport.last.equals("equip") && TestTransport.a==0,"matching Pet must be processed first");
            useReply(0,0);rootAndShop();confirm(true);refreshNow();bothPet.yKHpKzQyZroJ=1;info(bothPet);
            check(TestTransport.last.equals("equip") && TestTransport.a==1,"second bag slot must stay available after first item completes");
            useReply(1,1);rootAndShop();AutoPetRuntime.sawConfirm("Nâng cấp: Long Châu Mika2\nXác nhận nâng cấp?",8890,Integer.valueOf(7),8891);AutoPetRuntime.tick();
            refreshNow();bothMika.yKHpKzQyZroJ=1;info(bothMika);
            check(!AutoPetRuntime.running(),"both fixed-slot items did not complete");
            reset(true,true,0);upgradeOnly(0,0);upgradeOnly(1,2047);
            pet=body[10]=item(1305,5,10);mika=body[11]=item(1282,5,11);bag[0]=null;bag[1]=null;
            options(pet,0);options(mika,0);
            int trains=count("menu:102"),equips=count("equip:");
            AutoPetRuntime.toggle();check(TestTransport.last.equals("info") && TestTransport.a==5 && TestTransport.b==10,"upgrade-only reads equipped Pet, not bag slot 1");
            info(pet);check(TestTransport.last.equals("info") && TestTransport.b==11,"upgrade-only reads equipped Mika independently");
            info(mika);rootAndShop();check(TestTransport.a==0,"upgrade-only Pet selected correct box slot");
            confirm(true);refreshNow();pet.yKHpKzQyZroJ=1;info(pet);
            rootAndShop();AutoPetRuntime.sawConfirm("Nâng cấp: Long Châu Mika2\nXác nhận nâng cấp?",8890,Integer.valueOf(7),8891);AutoPetRuntime.tick();
            refreshNow();mika.yKHpKzQyZroJ=1;info(mika);
            check(!AutoPetRuntime.running() && count("menu:102")==trains && count("equip:")==equips,"both upgrade-only items must finish without training, stat checks or use");
            reset(true,false,0);upgradeOnly(0,0);int missingCommands=TestTransport.log.size();AutoPetRuntime.toggle();
            check(!AutoPetRuntime.running() && TestTransport.log.size()==missingCommands && TestTransport.notice.indexOf("trang bị")>=0,"upgrade-only bag item must ask user to equip without spending");
            reset(true,true,2);bag[1]=null;int before=TestTransport.log.size();AutoPetRuntime.toggle();
            check(!AutoPetRuntime.running() && TestTransport.log.size()==before,"preflight must validate both slots before any command");
            reset(true,false,2);bag[1]=bag[0];bag[0]=null;before=TestTransport.log.size();AutoPetRuntime.toggle();
            check(!AutoPetRuntime.running() && TestTransport.log.size()==before,"wrong Pet slot must not be searched or received automatically");
            reset(true,false,2);pet=bag[0];AutoPetRuntime.toggle();info(pet);rootAndShop();
            int confirmed=TestTransport.confirms;AutoPetRuntime.toggle();
            AutoPetRuntime.sawConfirm("Xác nhận luyện vật phẩm đã chọn?",8890,Integer.valueOf(8),8891);AutoPetRuntime.tick();
            check(!AutoPetRuntime.running() && TestTransport.confirms==confirmed && TestTransport.notice.equals("đã dừng auto"),"stop must ignore pending training confirmation");
            reset(true,false,2);pet=bag[0];AutoPetRuntime.toggle();info(pet);rootAndShop();
            AutoPetRuntime.sawNotice("Hãy nhận vật phẩm ở npc tashino rồi để ở hành trang rồi vào đây luyện");AutoPetRuntime.tick();
            check(!AutoPetRuntime.running() && bag[0]==pet,"server bag requirement must stop without using");
            reset(true,false,2);pet=bag[0];AutoPetRuntime.toggle();info(pet);
            kacyVKUNru trainMenu=new kacyVKUNru();trainMenu.addElement(new bmxqULQDW("Luyện Trang Bị",new TestTransport(),102,null));
            AutoPetRuntime.sawMenu(trainMenu);AutoPetRuntime.tick();
            QfMjPqGqZaRx.EgoDKTjkxEUMu(item(1305,32,0).EgoDKTjkxEUMu);
            new yRvgxykm().WRmEtfNMLdMb(new WZKDTXfSh((byte)33,new byte[]{32,1,0,5,25}));
            openShop(52,"Luyện");int buys=TestTransport.buys;AutoPetRuntime.tick();
            check(TestTransport.buys==buys+1,"binary list-before-open training selection");AutoPetRuntime.toggle();
            reset(true,false,2);pet=bag[0];AutoPetRuntime.toggle();info(pet);AutoPetRuntime.sawMenu(trainMenu);AutoPetRuntime.tick();
            openShop(52,"Luyện");AutoPetRuntime.tick();
            Field instance=AutoPetRuntime.class.getDeclaredField("INSTANCE");instance.setAccessible(true);
            field(AutoPetRuntime.class,instance.get(null),"shopRequestAt",long.class,Long.valueOf(0));
            AutoPetRuntime.tick();check(TestTransport.last.equals("list") && TestTransport.a==32,"native window 52 must refresh list 32");
            field(AutoPetRuntime.class,instance.get(null),"shopCacheAt",long.class,Long.valueOf(0));AutoPetRuntime.tick();
            check(TestTransport.last.equals("buy") && TestTransport.b==0,"cached native catalog training selection");
            int stoppedBuys=TestTransport.buys;AutoPetRuntime.tick();check(TestTransport.buys==stoppedBuys,"no duplicate training request");
            field(AutoPetRuntime.class,instance.get(null),"deadline",long.class,Long.valueOf(0));AutoPetRuntime.tick();
            check(!AutoPetRuntime.running() && TestTransport.notice.indexOf("Quá thời gian")>=0,"waiting request must time out");
            Field dialog=null;for(Field f:NBAFgUXWGCkgPq.class.getDeclaredFields())if(f.getName().equals("peMayxrZEBA") && f.getType()==NbLzSVpmyzydCP.class)dialog=f;
            dialog.setAccessible(true);check(dialog.get(null)==null,"timeout must close native Xin cho dialog");
            check(count("menu:101")==0,"manual receiving mode must never enter receive menu");
            senderBytes();
            // Background packets must not erase the last real send or the step we stopped on.
            for(int i=0;i<1200;i++) {
                WZKDTXfSh background=new WZKDTXfSh((byte)-30,new byte[]{(byte)-124});
                AutoPetRuntime.beforePacket(background);
                if(i==0)check(AutoPetAccess.input(background).available()==1,"subcommand observer consumed payload");
                AutoPetRuntime.afterPacket();
            }
            String report=AutoPetRuntime.diagnostics();
            check(report.indexOf("TX 16 pos=1")>=0,"background packet flood erased actual send");
            check(report.indexOf("-124x1200")>=0 && report.indexOf("RX -30")<0,"background subcommand not summarized");
            check(report.indexOf("xác nhận của máy chủ")>=0,"stopping erased the pending step");
            check(report.length()<2048,"diagnostics exceeded text box size");
            levelFlow();
            System.out.println("RUNTIME_SMOKE_PASS "+checks+" (actual game classes, transport interception; no live server)");
        } catch(Throwable t) {System.out.println("RUNTIME_SMOKE_FAIL "+t);t.printStackTrace();}
        notifyDestroyed();
    }
    protected void pauseApp() {}
    protected void destroyApp(boolean b) {}
}
