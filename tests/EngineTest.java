import java.util.*;

public final class EngineTest {
    static int checks;
    static void check(boolean value,String label) {
        if(!value) throw new AssertionError(label);
        ++checks;
    }
    static class Port implements AutoPetEngine.Port {
        int run,request,item,action,sends;
        List<String> notices=new ArrayList<>();
        public void send(int r,int q,int i,int a) {run=r;request=q;item=i;action=a;++sends;}
        public void notice(String s) {notices.add(s);}
    }
    static AutoPetConfig[] config() {
        AutoPetConfig c=new AutoPetConfig();
        c.enabled=true;c.receive=true;c.train=true;c.upgrade=true;c.selected=3;c.attempts=2;c.target=2;
        return new AutoPetConfig[]{c,new AutoPetConfig()};
    }
    static AutoPetEngine.Item item(boolean p,int options,int level) {return new AutoPetEngine.Item(p,options,level);}
    public static void main(String[] args) throws Exception {
        String[] labels={"Tấn công: +#","Băng công: +#","Hoả công: +#","Phong lôi: +#","Chí mạng: +#","HP tối đa: #","MP tối đa: +#","Tấn công: +#%","Miễn giảm sát thương: #%","Mỗi nửa giây hồi phục # HP và MP","Tăng #% kinh nghiệm khi đánh quái"};
        for(int i=0;i<labels.length;i++) check(AutoPetRules.option(labels[i])==i,"runtime option label "+i);
        check(AutoPetRules.menu("Nhận Vật\nPhẩm")==AutoPetEngine.RECEIVE,"receive screenshot menu");
        check(AutoPetRules.menu("Luyện Trang Bị")==AutoPetEngine.TRAIN,"training screenshot menu");
        check(AutoPetRules.menu("Nâng cấp vật phẩm")==AutoPetEngine.UPGRADE,"upgrade screenshot menu");
        check(AutoPetRules.menu("Thăng ngưỡng khai hóa")==0,"unrelated action never selected");
        check(AutoPetRules.confirmation(2,"Xác nhận luyện vật phẩm đã chọn?"),"training screenshot confirmation");
        check(AutoPetRules.confirmation(3,"Nâng cấp: Pet Long\nCần: 1.000 lượng.\nTỉ lệ thành công: 100%.\nXác nhận nâng cấp?"),"upgrade screenshot confirmation");
        check(!AutoPetRules.confirmation(2,"Xác nhận nâng cấp?"),"wrong action rejected");
        AutoPetConfig[] c=config();c[1].selected=1024;c[1].attempts=14;
        AutoPetConfig[] restored=AutoPetConfig.decode(AutoPetConfig.encode(c));
        check(restored[0].selected==3 && restored[1].selected==1024 && !restored[0].receive,"independent settings and legacy receive disabled");
        AutoPetConfig clone=c[0].copy();clone.selected=0;check(c[0].selected==3,"editing isolation");
        try {AutoPetConfig.decode(new byte[4]);throw new AssertionError("corrupt store accepted");} catch(java.io.IOException expected) {++checks;}
        c[0].selected=0;try {c[0].validate();throw new AssertionError("empty selection accepted");} catch(IllegalArgumentException expected) {++checks;}
        Port p=new Port();AutoPetEngine e=new AutoPetEngine(p);
        e.start(config(),new AutoPetEngine.Item[]{item(false,0,0),item(false,0,0)},0);
        check(!e.running() && p.sends==0,"manual receiving required even with legacy receive=true");
        p=new Port();e=new AutoPetEngine(p);
        e.start(config(),new AutoPetEngine.Item[]{bag(1,0),item(false,0,0)},0);
        check(p.action==AutoPetEngine.TRAIN && p.sends==1,"train before use");
        e.reply(p.run,p.request,true,bag(1,0),1);
        check(p.action==AutoPetEngine.TRAIN && p.sends==2,"one option is insufficient");
        e.reply(p.run,p.request,true,bag(3,0),2);
        check(p.action==AutoPetEngine.USE && p.sends==3,"use only after all selected options");
        e.reply(p.run,p.request,true,item(true,3,0),3);
        check(p.action==AutoPetEngine.UPGRADE && p.sends==4,"upgrade only after use acknowledgement");
        e.reply(p.run,p.request,true,item(true,3,1),4);
        check(p.sends==5 && e.running(),"level response controls next upgrade");
        e.reply(p.run,p.request,true,item(true,3,2),5);
        check(!e.running() && p.sends==5 && p.notices.get(0).contains("đủ cấp 2"),"stop at target and popup");
        p=new Port();e=new AutoPetEngine(p);c=config();c[0].attempts=1;
        e.start(c,new AutoPetEngine.Item[]{bag(1,0),item(false,0,0)},0);
        e.reply(p.run,p.request,true,bag(1,0),1);
        check(!e.running() && p.sends==1 && p.action==AutoPetEngine.TRAIN,"exhaustion neither uses nor upgrades");
        check(p.notices.get(0).contains("Không nâng cấp"),"exhaustion message");
        p=new Port();e=new AutoPetEngine(p);e.start(config(),new AutoPetEngine.Item[]{bag(3,0),item(false,0,0)},0);
        check(p.action==AutoPetEngine.USE,"already matching bag item skips training");
        int oldRun=p.run,oldRequest=p.request;e.stop();e.reply(oldRun,oldRequest,true,item(true,3,0),1);
        check(!e.running() && p.sends==1 && p.notices.get(0).equals("đã dừng auto"),"stop ignores pending use response");
        e.start(config(),new AutoPetEngine.Item[]{bag(1,0),item(false,0,0)},2);
        e.reply(oldRun,oldRequest,true,item(true,3,0),3);
        check(p.sends==2 && p.action==AutoPetEngine.TRAIN,"old generation cannot equip new run");
        e.tick(15002);check(!e.running() && p.sends==2,"timeout never retries spending");
        p=new Port();e=new AutoPetEngine(p);e.start(config(),new AutoPetEngine.Item[]{bag(3,0),item(false,0,0)},0);
        e.reply(p.run,p.request,true,bag(3,0),1);
        check(!e.running() && p.sends==1,"use response must report equipped");
        p=new Port();e=new AutoPetEngine(p);c=config();c[0].upgrade=false;
        e.start(c,new AutoPetEngine.Item[]{bag(3,0),item(false,0,0)},0);
        e.reply(p.run,p.request,true,item(true,3,0),1);
        check(!e.running() && p.sends==1 && p.action==AutoPetEngine.USE,"train-only uses matched item without upgrade");
        p=new Port();e=new AutoPetEngine(p);e.start(config(),new AutoPetEngine.Item[]{item(true,1,0),item(false,0,0)},0);
        check(!e.running() && p.sends==0,"never train an equipped item");
        p=new Port();e=new AutoPetEngine(p);e.start(config(),new AutoPetEngine.Item[]{item(true,3,0),item(false,0,0)},0);
        e.reply(p.run,p.request,true,item(true,3,0),1);
        check(e.running() && p.sends==2 && p.action==AutoPetEngine.UPGRADE,"failed upgrade continues after fresh result");
        e.reply(p.run,p.request,true,item(true,3,1),2);
        e.reply(p.run,p.request,true,item(true,3,0),3);
        check(e.running() && p.sends==4,"level loss continues toward configured target");
        e.reply(p.run,p.request,true,item(true,3,2),4);
        check(!e.running() && p.sends==4,"upgrade retries stop at target");
        check(AutoPetRules.upgradeFailure("Nâng cấp thất bại"),"ordinary upgrade failure is recoverable");
        check(!AutoPetRules.upgradeFailure("Nâng cấp thất bại, không đủ lượng"),"insufficient resources still stop");
        p=new Port();e=new AutoPetEngine(p);c=config();c[1]=c[0].copy();
        e.start(c,new AutoPetEngine.Item[]{item(true,3,2),bag(3,1)},0);
        check(p.item==1 && p.action==AutoPetEngine.USE,"two independent item states");
        e.reply(p.run,p.request,true,item(true,3,1),1);e.reply(p.run,p.request,true,item(true,3,2),2);
        check(!e.running(),"both items complete");
        p=new Port();e=new AutoPetEngine(p);c=config();c[0].attempts=0;
        e.start(c,new AutoPetEngine.Item[]{bag(1,0),item(false,0,0)},0);
        check(!e.running() && p.sends==0,"zero budget does not use unmatched item");
        System.out.println("PASS: "+checks+" engine/config assertions (simulated server, not live game)");
    }
    static AutoPetEngine.Item bag(int options,int level) {return new AutoPetEngine.Item(true,options,level,false);}
}
