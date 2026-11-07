/** Find live items by template while the native parser is not changing inventory. */
public final class AutoPetInventory {
    private static final int[] WORN={5,41,51,52,53};
    public static wpiwfWAXuWze bag(int id) {
        synchronized(AutoPetRuntime.packetLock()) {
            wpiwfWAXuWze[] all=AutoPetAccess.items(3);
            if(all!=null)for(int i=0;i<all.length;i++)
                if(matches(all[i],id,3) && all[i].AZmTbjgvi==i)return all[i];
            return null;
        }
    }
    public static wpiwfWAXuWze worn(int id) {
        synchronized(AutoPetRuntime.packetLock()) {
            for(int s=0;s<WORN.length;s++) {
                wpiwfWAXuWze[] all=AutoPetAccess.items(WORN[s]);
                if(all!=null)for(int i=0;i<all.length;i++)if(matches(all[i],id,WORN[s]))return all[i];
            }
            return null;
        }
    }
    public static wpiwfWAXuWze find(int id) {
        synchronized(AutoPetRuntime.packetLock()) {
            wpiwfWAXuWze it=worn(id);return it==null?bag(id):it;
        }
    }
    private static boolean matches(wpiwfWAXuWze it,int id,int source) {
        return it!=null && it.EgoDKTjkxEUMu!=null && it.EgoDKTjkxEUMu.QpKzrQoqbjfLc==id && it.tTqVmuHPEhAbg==source;
    }
    public static long count(int id) {
        synchronized(AutoPetRuntime.packetLock()) {
            long count=0;wpiwfWAXuWze[] all=AutoPetAccess.items(3);
            if(all!=null)for(int i=0;i<all.length;i++)if(matches(all[i],id,3))count+=Math.max(1,all[i].WKutVuVwuGT);
            return count;
        }
    }
    public static boolean room() {
        synchronized(AutoPetRuntime.packetLock()) {
            wpiwfWAXuWze[] all=AutoPetAccess.items(3);
            if(all!=null)for(int i=0;i<all.length;i++)if(all[i]==null)return true;
            return false;
        }
    }
}
