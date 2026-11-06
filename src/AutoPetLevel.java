import javax.microedition.rms.RecordStore;

/** Runs on the existing AutoLV worker; the canvas only watches cancellation. */
public final class AutoPetLevel {
    private static volatile SJSCaeScqbiua owner;
    private static volatile int stage; // 0=leveling, 1=travel, 2=Pet, 3=completed
    private static String character,key;
    private static long deadline;

    private static boolean bothEquipped() {
        boolean pet=false,mika=false;
        int[] sources={5,41,51,52,53};
        for(int s=0;s<sources.length;s++) {
            wpiwfWAXuWze[] all=AutoPetAccess.items(sources[s]);
            if(all==null) continue;
            for(int i=0;i<all.length;i++) {
                wpiwfWAXuWze it=all[i];
                if(it==null || it.EgoDKTjkxEUMu==null || it.tTqVmuHPEhAbg!=sources[s]) continue;
                int id=it.EgoDKTjkxEUMu.QpKzrQoqbjfLc;
                pet|=id==AutoPetRules.PET;mika|=id==AutoPetRules.MIKA;
            }
        }
        return pet && mika;
    }

    private static String character() {
        oFpffzUyucjDJX p=oFpffzUyucjDJX.rdmjdEJDQ();
        return p==null?"":p.BfndoYuX+":"+p.dvKEjoqBk;
    }
    private static boolean saved(boolean write) throws Exception {
        RecordStore rs=null;
        try {
            rs=RecordStore.openRecordStore(key,true);
            if(rs.getNumRecords()>0) {
                String stored=new java.io.DataInputStream(new java.io.ByteArrayInputStream(rs.getRecord(1))).readUTF();
                if(!stored.equals(character)) throw new RuntimeException("Không xác định được dấu hoàn tất của nhân vật");
                return true;
            }
            if(write) {
                java.io.ByteArrayOutputStream bytes=new java.io.ByteArrayOutputStream();
                java.io.DataOutputStream out=new java.io.DataOutputStream(bytes);out.writeUTF(character);out.flush();
                byte[] data=bytes.toByteArray();rs.addRecord(data,0,data.length);
                return true;
            }
            return false;
        } finally {if(rs!=null) rs.closeRecordStore();}
    }
    private static gsxwWgJt tashino() {
        kacyVKUNru all=AutoPetAccess.npcs();
        if(all!=null) for(int i=0;i<all.size();i++) {
            gsxwWgJt npc=(gsxwWgJt)all.elementAt(i);
            HAeSnYtYmns t=AutoPetAccess.template(npc);
            if(t!=null && AutoPetRules.plain(t.wLHNXXbvUSit).equals("tashino")) return npc;
        }
        return null;
    }
    private static void stop(String message) {
        if(AutoPetRuntime.running()) AutoPetRuntime.stop();
        if(AutoPetAccess.currentAuto()==owner) AutoPetAccess.setAuto(null);
        owner=null;stage=0;
        if(message!=null) AutoPetBridge.notice(message);
    }
    public static synchronized boolean intercept(SJSCaeScqbiua auto) {
        try {
            if(AutoPetAccess.currentAuto()!=auto) return true;
            String current=character();
            if(stage!=0 && (AutoPetAccess.currentAuto()!=owner || !current.equals(character))) {
                stop(null);return true;
            }
            if(stage==3) return false;
            if(stage==0) {
                if(AutoPetRuntime.running()) return true;
                if(oFpffzUyucjDJX.rdmjdEJDQ().vMyUkiiUKxerr<21) return false;
                owner=auto;character=current;key="APLC21_"+current.hashCode();
                // Existing equipment is enough to continue leveling even if
                // RMS/configuration was lost when re-importing the client.
                // Do not save a completion marker: this is not an upgrade result.
                if(bothEquipped()) {stage=3;return false;}
                if(saved(false)) {stage=3;return false;}
                AutoPetConfig[] c=AutoPetUI.settings();boolean enabled=false;
                for(int i=0;i<2;i++) {c[i].validate();enabled|=c[i].enabled;}
                if(!enabled) {stop("Đã đạt cấp 21. Hãy cấu hình và Enable AutoPet/LC rồi bật lại Tàn sát");return true;}
                stage=1;deadline=System.currentTimeMillis()+180000;
                AutoPetAccess.clearCombat();
            }
            if(stage==1) {
                if(System.currentTimeMillis()>=deadline) {stop("Dừng chuỗi cấp 21: chưa đến được Tashino ở Làng Tone");return true;}
                if(AutoPetAccess.map()!=22) {auto.peMayxrZEBA(22,-1,-1,-1);return true;}
                gsxwWgJt npc=tashino();if(npc==null) return true;
                if(!AutoPetAccess.nearNpc(npc)) {BMWCZFcnBgzHkh.peMayxrZEBA(npc);return true;}
                if(AutoPetAccess.currentAuto()!=auto) {stop(null);return true;}
                stage=2;
                AutoPetRuntime.toggle();
                if(!AutoPetRuntime.running() && !AutoPetRuntime.completed()) stop(null);
                return true;
            }
            if(stage==2) {
                if(AutoPetRuntime.running()) return true;
                if(!AutoPetRuntime.completed()) {stop(null);return true;}
                saved(true);stage=3;AutoPetAccess.resetLevel(auto);
                return false; // same native update immediately resumes AutoLV
            }
        } catch(Exception e) {stop("Dừng chuỗi cấp 21: "+e.getMessage());}
        return true;
    }
    public static void watch() {
        SJSCaeScqbiua active=owner;
        if(active!=null && (AutoPetAccess.currentAuto()!=active || !character().equals(character))) {
            // Do not acquire this class's monitor: native travel can wait for
            // network updates, which must remain free to paint and parse.
            if(stage==2) AutoPetRuntime.stop();
            owner=null;stage=0;
        }
    }
}
