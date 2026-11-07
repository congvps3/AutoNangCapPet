/** Native catalog -> one purchase -> bag -> one use, before other preparation items. */
public final class PurchaseCases extends JourneyCases {
    static HAeSnYtYmns goosho(){HAeSnYtYmns t=new HAeSnYtYmns();t.mvjvhHcftNANDP=11;t.wLHNXXbvUSit="Goosho";return t;}
    static void catalog(int id) throws Exception {
        QfMjPqGqZaRx.EgoDKTjkxEUMu(item(id,32,7).EgoDKTjkxEUMu);
        packet(33,new byte[]{32,1,7,(byte)(id>>8),(byte)id});
    }
    static void select(SJSCaeScqbiua auto) throws Exception {
        check(TestTransport.last.equals("npc") && TestTransport.a==11,"codes must lead to Goosho before reward use or Tashino");
        kacyVKUNru choices=new kacyVKUNru();choices.addElement(new bmxqULQDW("Cửa hàng",new TestTransport(),105,null));
        AutoPetRuntime.sawMenu(choices);AutoPetLevel.intercept(auto);check(TestTransport.last.equals("menu") && TestTransport.a==105,"Goosho menu uses its native callback");
        openShop(52,"Cửa hàng");catalog(248);AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("buy") && TestTransport.a==32 && TestTransport.b==7 && TestTransport.c==1,"must buy exactly one 248 using actual catalog source/index");
    }
    static void buy(SJSCaeScqbiua auto,int bagSlot) throws Exception {
        select(auto);int requests=TestTransport.buys;AutoPetLevel.intercept(auto);AutoPetLevel.intercept(auto);
        check(TestTransport.buys==requests,"purchase must not repeat while waiting for the bag update");
        bag[bagSlot]=item(248,3,bagSlot);AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("equip") && TestTransport.a==bagSlot,"new 248 must be used from its actual bag slot first");
        packet(10,new byte[]{(byte)bagSlot});AutoPetLevel.intercept(auto);
    }
    static void run() throws Exception {
        SJSCaeScqbiua auto=fresh(25001,10,true,false);progress(25001,3);AutoPetLevel.intercept(auto);select(auto);
        int requests=TestTransport.buys;AutoPetRuntime.sawNotice("Không đủ yên để mua vật phẩm");AutoPetLevel.intercept(auto);
        check(AutoPetAccess.currentAuto()==null && TestTransport.buys==requests && TestTransport.notice.indexOf("Không đủ yên")>=0,"rejected purchase must stop without duplicate buying");
        auto=fresh(25002,10,true,false);progress(25002,3);AutoPetLevel.intercept(auto);select(auto);requests=TestTransport.buys;
        field(AutoPetPurchase.class,null,"deadline",long.class,Long.valueOf(0));AutoPetLevel.intercept(auto);
        check(AutoPetAccess.currentAuto()==null && TestTransport.buys==requests && TestTransport.notice.indexOf("không gửi lại")>=0,"unacknowledged purchase must time out without replay");
        auto=fresh(25003,10,true,false);progress(25003,3);bag[26]=item(248,3,26);requests=TestTransport.buys;AutoPetLevel.intercept(auto);
        check(TestTransport.last.equals("equip") && TestTransport.a==26 && TestTransport.buys==requests,"existing 248 is used without another purchase");
        AutoPetAccess.setAuto(null);AutoPetLevel.watch();
    }
}
