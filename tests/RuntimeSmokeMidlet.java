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
    static wCmqWHpCSBaGX mob(int kind) {
        return new wCmqWHpCSBaGX((short)0,false,false,false,false,false,0,1,100L,1,100L,(short)100,(short)100,(byte)2,(byte)kind,false,false);
    }
    static boolean nativeAllowed(wCmqWHpCSBaGX mob) throws Exception {
        Method m=BMWCZFcnBgzHkh.class.getDeclaredMethod("peMayxrZEBA",wCmqWHpCSBaGX.class,int.class);m.setAccessible(true);
        return ((Boolean)m.invoke(null,mob,Integer.valueOf(-1))).booleanValue();
    }
    static void huntFlow() throws Exception {
        SJSCaeScqbiua auto=new SJSCaeScqbiua();auto.xNWwdhkGHYvrH=-1;AutoPetAccess.setAuto(auto);
        UepTKcHi def=new UepTKcHi();field(UepTKcHi.class,def,"BMWCZFcnBgzHkh",javax.microedition.lcdui.Image[].class,new javax.microedition.lcdui.Image[0]);
        field(wCmqWHpCSBaGX.class,null,"peMayxrZEBA",UepTKcHi[].class,new UepTKcHi[]{def});
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)1));
        field(SBnKDsbzS.class,null,"GAWjBFnKuf",byte.class,Byte.valueOf((byte)0));
        AutoLevelHook.reset(auto);field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(1));
        kacyVKUNru mobs=new kacyVKUNru();field(zxnsayePWpajv.class,null,"JWQMnAXscj",kacyVKUNru.class,mobs);
        wCmqWHpCSBaGX normal=mob(0),elite=mob(1),leader=mob(2),beast=mob(3);
        mobs.addElement(normal);mobs.addElement(elite);mobs.addElement(leader);mobs.addElement(beast);
        check(AutoLevelHook.checkZone(auto,1000),"new zone with mobs must attack immediately");
        check(nativeAllowed(leader) && !nativeAllowed(normal) && !nativeAllowed(elite) && !nativeAllowed(beast),"native target validation must prioritize Thu linh type 2");
        check(AutoLevelHook.checkZone(auto,9000),"living leader must keep zone beyond six seconds");
        int changes=count("zone:");leader.ubmVeVhBeAG=0;
        check(!nativeAllowed(leader),"dead leader must invalidate native cached target immediately");
        check(AutoLevelHook.checkZone(auto,9001) && nativeAllowed(normal) && nativeAllowed(elite) && nativeAllowed(beast),"leader death must immediately allow other mobs and start a new six-second interval");
        check(count("zone:")==changes,"leader death must not immediately leave a zone with other mobs");
        check(AutoLevelHook.checkZone(auto,15000),"ordinary combat must continue for 5999 ms after leader death");
        check(!AutoLevelHook.checkZone(auto,15001) && count("zone:")==changes+1,"no leader for six seconds after death must change zones");
        AutoLevelHook.checkZone(auto,15002);check(count("zone:")==changes+1,"pending zone change must not spam requests");
        field(SBnKDsbzS.class,null,"GAWjBFnKuf",byte.class,Byte.valueOf((byte)1));
        check(AutoLevelHook.checkZone(auto,15003) && nativeAllowed(normal),"new zone starts fighting immediately without old change cooldown");
        normal.ubmVeVhBeAG=elite.ubmVeVhBeAG=beast.ubmVeVhBeAG=0;
        check(!AutoLevelHook.checkZone(auto,15004) && count("zone:")==changes+2,"last mob killed must change next update without waiting six seconds");
        field(SBnKDsbzS.class,null,"GAWjBFnKuf",byte.class,Byte.valueOf((byte)2));
        normal.ubmVeVhBeAG=100;
        check(AutoLevelHook.checkZone(auto,16000),"normal mobs must be attacked on first update without leader");
        check(AutoLevelHook.checkZone(auto,19000),"after three seconds Auto must still attack ordinary mobs");
        check(AutoLevelHook.checkZone(auto,21999),"initial leader search must last the full six seconds");
        check(!AutoLevelHook.checkZone(auto,22000) && count("zone:")==changes+3,"no leader must change at six seconds from arrival");
        AutoLevelHook.reset(auto);field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(1));
        mobs.removeAllElements();
        check(!AutoLevelHook.checkZone(auto,23000),"empty newly loaded list must allow initial data to arrive");
        AutoLevelHook.checkZone(auto,28999);check(count("zone:")==changes+3,"initial data grace must not end before six seconds");
        check(!AutoLevelHook.checkZone(auto,29000) && count("zone:")==changes+4,"initial empty zone must change after six-second leader search");
        AutoLevelHook.reset(auto);field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(1));
        mobs.addElement(normal);mobs.addElement(leader);leader.ubmVeVhBeAG=100;
        check(AutoLevelHook.checkZone(auto,30000) && !nativeAllowed(normal),"new leader must immediately take priority");
        leader.ubmVeVhBeAG=0;check(AutoLevelHook.checkZone(auto,31000),"first leader death starts ordinary combat window");
        check(AutoLevelHook.checkZone(auto,36999),"first window stays active before deadline");
        leader.ubmVeVhBeAG=100;
        check(AutoLevelHook.checkZone(auto,36999) && !nativeAllowed(normal),"leader respawn interrupts ordinary combat window");
        leader.ubmVeVhBeAG=0;
        check(AutoLevelHook.checkZone(auto,39000) && nativeAllowed(normal),"second leader death must start another fresh six seconds");
        check(AutoLevelHook.checkZone(auto,44999),"old window deadline must not cut the new window short");
        check(!AutoLevelHook.checkZone(auto,45000) && count("zone:")==changes+5,"second leaderless window must expire six seconds after second death");
        AutoLevelHook.reset(auto);field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(1));
        // Bare game instance only supplies the zone catalog; do not load assets.
        Class<?> allocator=ClassLoader.getSystemClassLoader().loadClass("sun.misc.Unsafe");Field singleton=allocator.getDeclaredField("theUnsafe");singleton.setAccessible(true);
        Object game=allocator.getMethod("allocateInstance",Class.class).invoke(singleton.get(null),zxnsayePWpajv.class);
        field(zxnsayePWpajv.class,null,"EgoDKTjkxEUMu",zxnsayePWpajv.class,game);
        field(zxnsayePWpajv.class,game,"PgaoLeqNLao",int[].class,new int[]{0});
        AutoLevelHook.checkZone(auto,50000);changes=count("zone:");
        check(AutoLevelHook.checkZone(auto,56000) && count("zone:")==changes,"single-zone map must not spam impossible changes");
        field(zxnsayePWpajv.class,game,"PgaoLeqNLao",int[].class,null);
        AutoPetAccess.setAuto(null);check(AutoLevelHook.allowed(normal),"other auto modes keep native target behavior");
        AutoLevelHook.reset(auto);field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(2));
        changes=count("zone:");check(AutoLevelHook.checkZone(auto,57000) && count("zone:")==changes,"travel to training map must not change zones prematurely");
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)22));
    }

    static void upgradeOnly(int target,int mask) throws Exception {
        AutoPetConfig[] settings=AutoPetUI.settings();settings[target].train=false;settings[target].selected=mask;
        field(AutoPetUI.class,null,"saved",AutoPetConfig[].class,settings);
    }
    static void realmFlow() throws Exception {
        SJSCaeScqbiua auto=new SJSCaeScqbiua();AutoPetAccess.setAuto(auto);AutoLevelHook.reset(auto);
        int[][] maps=autouplv.MapCatalog.MAPS;
        for(int i=0;i<AutoPetMaps.WORLD.length;i++) {
            int[] row=AutoPetMaps.WORLD[i];boolean found=false;
            for(int j=0;j<maps.length;j++) found|=maps[j][0]==row[0] && maps[j][1]==row[1];
            check(found,"native map catalog must contain each supplied outside level");
        }
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(60));
        field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(24));
        field(AutoLevelHook.class,null,"trainingMonsterLevel",int.class,Integer.valueOf(65));auto.PVQpZEqSNMpYBM=24;
        check(AutoLevelHook.update(auto) && auto.PVQpZEqSNMpYBM==139,"native AutoLV must abandon valid outside map immediately at level 60");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(65));
        check(AutoLevelHook.update(auto) && auto.PVQpZEqSNMpYBM==139,"native AutoLV must remain until low mob level plus two");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(66));
        check(AutoLevelHook.update(auto) && auto.PVQpZEqSNMpYBM==140,"native AutoLV must move from 139 to 140 at level 66");
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)140));
        kacyVKUNru mobs=new kacyVKUNru();field(zxnsayePWpajv.class,null,"JWQMnAXscj",kacyVKUNru.class,mobs);
        wCmqWHpCSBaGX low=mob(0),highLeader=mob(2);low.aRzRUEjwca=74;highLeader.aRzRUEjwca=77;
        mobs.addElement(low);mobs.addElement(highLeader);
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(67));
        check(AutoLevelHook.checkZone(auto,1000) && nativeAllowed(low) && !nativeAllowed(highLeader),"gap 10 must prefer correct monster level over higher leader");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(68));
        check(AutoLevelHook.checkZone(auto,1100) && nativeAllowed(highLeader) && nativeAllowed(low),"gap 9 unlocks all levels without leader priority in realm");
        check(AutoPetHunt.scoreType(2)==0 && !AutoPetHunt.leaderPreference(true),"realm must disable native score bonus and elite scan as well as target filtering");
        highLeader.QgPHSngFRpo=0;
        check(AutoLevelHook.checkZone(auto,1200) && nativeAllowed(low) && nativeAllowed(highLeader),"all levels must be targetable when no leader exists in all-level mode");
        int zones=count("zone:");check(AutoLevelHook.checkZone(auto,12000) && count("zone:")==zones,"realm must not switch zones because no leader appeared in six seconds");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(59));
        check(!nativeAllowed(low) && !nativeAllowed(highLeader),"native targets must not unlock realm below level 60");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(155));
        check(AutoLevelHook.update(auto) && auto.PVQpZEqSNMpYBM==148,"native AutoLV must select terminal map instead of pausing at 155");
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)148));
        low.aRzRUEjwca=153;highLeader.aRzRUEjwca=159;highLeader.QgPHSngFRpo=2;
        check(AutoLevelHook.update(auto) && auto.PVQpZEqSNMpYBM==148 && nativeAllowed(low) && nativeAllowed(highLeader),"terminal map must keep killing all eligible mobs including ordinary mobs");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(200));
        check(AutoLevelHook.update(auto) && auto.PVQpZEqSNMpYBM==148 && AutoPetAccess.currentAuto()==auto,"terminal map must have no client level cap and keep Tan sat enabled");
        low.ubmVeVhBeAG=highLeader.ubmVeVhBeAG=0;
        check(!AutoLevelHook.checkZone(auto,System.currentTimeMillis()) && count("zone:")==zones+1,"empty terminal map still changes zone, not map");
        AutoLevelHook.reset(auto);field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(148));mobs.removeAllElements();
        check(!AutoLevelHook.checkZone(auto,1000),"fresh empty realm zone must allow initial monster loading");
        check(!AutoLevelHook.checkZone(auto,7000) && count("zone:")==zones+2,"fresh realm zone that stays empty must not wait forever");
        AutoPetAccess.setAuto(null);field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)22));
        AutoPetAccess.setAuto(auto);check(AutoPetHunt.scoreType(2)==2 && AutoPetHunt.leaderPreference(true),"outside map restores normal leader preference");AutoPetAccess.setAuto(null);
    }
    static void useReply(int bagSlot,int equipSlot) throws Exception {
        wpiwfWAXuWze it=bag[bagSlot];bag[bagSlot]=null;body[equipSlot]=it;it.tTqVmuHPEhAbg=5;it.AZmTbjgvi=equipSlot;
        AutoPetRuntime.tick();check(TestTransport.last.equals("info") && TestTransport.a==5,"use must await actual equipped item info");info(it);
    }
    static void hudFlow() throws Exception {
        check(!AutoPetHunt.realmLeaders(),"TL defaults off for realm");
        SJSCaeScqbiua auto=new SJSCaeScqbiua();AutoPetAccess.setAuto(auto);AutoLevelHook.reset(auto);
        field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(140));
        field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)140));
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(68));
        kacyVKUNru mobs=new kacyVKUNru();wCmqWHpCSBaGX normal=mob(0),boss=mob(2);normal.aRzRUEjwca=74;boss.aRzRUEjwca=77;
        mobs.addElement(normal);mobs.addElement(boss);field(zxnsayePWpajv.class,null,"JWQMnAXscj",kacyVKUNru.class,mobs);
        check(AutoLevelHook.checkZone(auto,1000) && nativeAllowed(normal) && nativeAllowed(boss),"TL off allows ordinary mobs alongside leader");
        field(NBAFgUXWGCkgPq.class,null,"DndwYNCrvBa",int.class,Integer.valueOf(240));
        field(NBAFgUXWGCkgPq.class,null,"SXPEzvYGy",int.class,Integer.valueOf(160));
        field(NBAFgUXWGCkgPq.class,null,"gxpcPAQGYwZ",boolean.class,Boolean.FALSE);
        field(BWWvgXUCGmx.class,null,"ydijJozSmduyg",int.class,Integer.valueOf(0));
        field(rHNGbrXZGUD.class,null,"YDPUzPRaKKz",int.class,Integer.valueOf(2));
        Field gameField=null;
        for(Field candidate:zxnsayePWpajv.class.getDeclaredFields())if(candidate.getName().equals("EgoDKTjkxEUMu") && candidate.getType()==zxnsayePWpajv.class)gameField=candidate;
        gameField.setAccessible(true);
        field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",XZRZEVaKAwWG.class,gameField.get(null));
        int commands=TestTransport.log.size();
        check(AutoPetAccess.isGame(),"HUD fixture must select game screen");
        Method hudY=AutoPetBridge.class.getDeclaredMethod("y");hudY.setAccessible(true);
        Method hudX=AutoPetBridge.class.getDeclaredMethod("x");hudX.setAccessible(true);
        check(AutoPetBridge.pointer(434,126,0) && AutoPetBridge.pointer(434,126,2),"scaled TL tap must be captured; x="+hudX.invoke(null)+" y="+hudY.invoke(null)+" scale="+rHNGbrXZGUD.YDPUzPRaKKz);
        check(AutoPetHunt.realmLeaders() && !AutoPetRuntime.running() && TestTransport.log.size()==commands,"TL tap changes only realm preference, not AutoPet");
        check(AutoLevelHook.checkZone(auto,1100) && nativeAllowed(boss) && !nativeAllowed(normal),"TL on prioritizes realm leader immediately");
        check(AutoPetHunt.scoreType(2)==2 && AutoPetHunt.leaderPreference(true),"TL on enables native priority as well as target filter");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(67));AutoLevelHook.checkZone(auto,1101);
        check(nativeAllowed(normal) && !nativeAllowed(boss),"TL on must still obey nearest-level restriction");
        field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(68));AutoLevelHook.checkZone(auto,1102);
        boss.ubmVeVhBeAG=0;check(AutoLevelHook.checkZone(auto,1200) && AutoLevelHook.checkZone(auto,7199),"TL on fights ordinary mobs for six seconds after leader dies");
        int zones=count("zone:");check(!AutoLevelHook.checkZone(auto,7200) && count("zone:")==zones+1,"TL on resumes six-second leader-search zone changes");
        field(AutoPetHunt.class,null,"realmLeaders",boolean.class,Boolean.FALSE);field(AutoPetHunt.class,null,"preferenceLoaded",boolean.class,Boolean.FALSE);
        check(AutoPetHunt.realmLeaders(),"TL preference reloads from RMS");
        check(AutoPetBridge.pointer(348,126,0) && AutoPetBridge.pointer(434,126,2) && AutoPetHunt.realmLeaders(),"drag from Auto to TL must not toggle either button");
        AutoPetBridge.pointer(434,126,0);AutoPetBridge.pointer(0,0,1);AutoPetBridge.pointer(0,0,2);
        check(AutoPetHunt.realmLeaders(),"release outside TL cancels tap");
        check(!AutoPetBridge.pointer(390,126,0) && !AutoPetBridge.pointer(390,126,2),"gap between buttons must not capture game input");
        config(false,false,0);AutoPetBridge.pointer(348,126,0);AutoPetBridge.pointer(348,126,2);
        check(TestTransport.notice.indexOf("Enable")>=0 && AutoPetHunt.realmLeaders(),"Auto button retains AutoPet behavior independently");
        AutoPetBridge.pointer(434,126,0);AutoPetBridge.pointer(434,126,2);
        check(!AutoPetHunt.realmLeaders(),"second TL tap disables realm priority");
        AutoLevelHook.reset(auto);field(AutoLevelHook.class,null,"trainingMap",int.class,Integer.valueOf(140));
        check(AutoLevelHook.checkZone(auto,10000) && AutoLevelHook.checkZone(auto,20000),"TL off keeps ordinary combat beyond six seconds");
        hudPreview();
        field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",XZRZEVaKAwWG.class,null);
        check(!AutoPetBridge.pointer(217,123,0),"HUD must not capture taps off game screen");
        field(rHNGbrXZGUD.class,null,"YDPUzPRaKKz",int.class,Integer.valueOf(1));AutoPetAccess.setAuto(null);
    }
    static void hudPreview() throws Exception {
        field(rHNGbrXZGUD.class,null,"YDPUzPRaKKz",int.class,Integer.valueOf(1));
        javax.microedition.lcdui.Image picture=javax.microedition.lcdui.Image.createImage(240,160);
        javax.microedition.lcdui.Graphics g=picture.getGraphics();g.setColor(0x141e24);g.fillRect(0,0,240,160);
        rHNGbrXZGUD wrapped=new rHNGbrXZGUD(g);
        wBpDHXtSRtR.LBtrnuKVnvuG.EgoDKTjkxEUMu(wrapped,"OFF",20,47,0);AutoPetBridge.paint(wrapped);
        AutoPetHunt.toggleRealmLeaders();field(BWWvgXUCGmx.class,null,"ydijJozSmduyg",int.class,Integer.valueOf(60));
        Field f=AutoPetRuntime.class.getDeclaredField("INSTANCE");f.setAccessible(true);
        field(AutoPetRuntime.class,f.get(null),"active",boolean.class,Boolean.TRUE);
        wBpDHXtSRtR.LBtrnuKVnvuG.EgoDKTjkxEUMu(wrapped,"ON",20,107,0);AutoPetBridge.paint(wrapped);
        field(AutoPetRuntime.class,f.get(null),"active",boolean.class,Boolean.FALSE);AutoPetHunt.toggleRealmLeaders();
        int[] pixels=new int[240*160];picture.getRGB(pixels,0,240,0,0,240,160);
        java.io.DataOutputStream out=new java.io.DataOutputStream(new java.io.FileOutputStream("/tmp/autopet-r19-hud.ppm"));
        out.writeBytes("P6\n240 160\n255\n");
        for(int i=0;i<pixels.length;i++){out.writeByte(pixels[i]>>16);out.writeByte(pixels[i]>>8);out.writeByte(pixels[i]);}out.close();
        boolean changed=false;for(int y=0;y<38;y++)for(int x=155;x<236;x++)changed|=pixels[240*(44+y)+x]!=pixels[240*(104+y)+x];
        check(changed,"active and inactive HUD labels must look different");
        check(AutoPetAccess.treoButton(false)!=null && AutoPetAccess.treoButton(true)!=null,"HUD must reuse both native Treo button textures");
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
            // Supply the screen callback without starting the login/asset constructor.
            Class<?> unsafeClass=ClassLoader.getSystemClassLoader().loadClass("sun.misc.Unsafe");
            Field unsafeField=unsafeClass.getDeclaredField("theUnsafe");unsafeField.setAccessible(true);
            Object screen=unsafeClass.getMethod("allocateInstance",Class.class).invoke(unsafeField.get(null),NBAFgUXWGCkgPq.class);
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",NBAFgUXWGCkgPq.class,screen);
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",XxukxTaDkERtV.class,new XxukxTaDkERtV());
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",RjDwVJrHk.class,new RjDwVJrHk());
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",gqQMxdaie.class,new gqQMxdaie());
            field(SBnKDsbzS.class,null,"uTxJizAJr",short.class,Short.valueOf((short)22));
            field(NBAFgUXWGCkgPq.class,null,"peMayxrZEBA",ceMGdFNBJnq.class,new ceMGdFNBJnq());
            HAeSnYtYmns t=new HAeSnYtYmns();t.mvjvhHcftNANDP=9;t.wLHNXXbvUSit="Tashino";
            field(gsxwWgJt.class,null,"peMayxrZEBA",HAeSnYtYmns[].class,new HAeSnYtYmns[]{t,JourneyCases.okane()});
            kacyVKUNru ns=new kacyVKUNru();ns.addElement(new gsxwWgJt(0,0,100,100,0));ns.addElement(new gsxwWgJt(0,0,100,100,1));
            field(zxnsayePWpajv.class,null,"HSjEMvafS",kacyVKUNru.class,ns);
            player=currentPlayer();field(oFpffzUyucjDJX.class,player,"peMayxrZEBA",wpiwfWAXuWze[].class,bag);
            field(oFpffzUyucjDJX.class,player,"rdmjdEJDQ",wpiwfWAXuWze[].class,body);
            field(oFpffzUyucjDJX.class,player,"vMyUkiiUKxerr",int.class,Integer.valueOf(21));
            checkSetupMenu("GRYYuFfQZGmfH");checkSetupMenu("xbqjzadR");
            reset(true,false,2);pet=bag[0];int usedBefore=count("equip:");
            AutoPetRuntime.toggle();check(TestTransport.last.equals("info") && TestTransport.a==3 && TestTransport.b==0,"Pet must start at bag slot 1");
            info(pet);check(TestTransport.last.equals("npc") && TestTransport.a==9 && count("equip:")==usedBefore,"bag training must precede use");
            rootAndShop();check(count("menu:101")==0,"existing items must not be claimed again");
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
            check(!AutoPetRuntime.running(),"both existing bag items did not complete");
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
            reset(true,false,0);upgradeOnly(0,0);pet=bag[0];AutoPetRuntime.toggle();
            info(pet);check(AutoPetRuntime.running() && TestTransport.last.equals("equip") && TestTransport.a==0,"upgrade-only bag item must use without training or stat checks");
            AutoPetRuntime.toggle();
            reset(true,false,2);pet=bag[7]=bag[0];pet.AZmTbjgvi=7;bag[0]=null;AutoPetRuntime.toggle();
            check(AutoPetRuntime.running() && TestTransport.last.equals("info") && TestTransport.b==7,"Pet must be found in any bag slot");
            info(pet);check(TestTransport.last.equals("npc"),"moved Pet must train before use");AutoPetRuntime.toggle();
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
            check(count("menu:101")==0,"existing-item workflows must not enter receive menu");
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
            JourneyCases.run();
            huntFlow();
            realmFlow();
            hudFlow();
            System.out.println("RUNTIME_SMOKE_PASS "+checks+" (actual game classes, transport interception; no live server)");
        } catch(Throwable t) {System.out.println("RUNTIME_SMOKE_FAIL "+t);t.printStackTrace();}
        notifyDestroyed();
    }
    protected void pauseApp() {}
    protected void destroyApp(boolean b) {}
}
