import autouplv.MapCatalog;

public final class MapRulesTest {
    static int checks;
    static void check(boolean b,String text) {if(!b)throw new AssertionError(text);checks++;}
    public static void main(String[] args) {
        int[][] maps=AutoPetMaps.extend(MapCatalog.MAPS);
        for(int i=0;i<AutoPetMaps.WORLD.length;i++) {
            int[] row=AutoPetMaps.WORLD[i];boolean found=false;
            for(int j=0;j<maps.length;j++)found|=maps[j][0]==row[0] && maps[j][1]==row[1];
            check(found,"outside map level missing");
        }
        for(int level=1;level<60;level++) {
            check(AutoPetMaps.realmMap(level)==-1,"realm locked below 60");
            int selected=AutoPetMaps.selectExcluding(level,139,-1,maps);
            check(!AutoPetMaps.realm(selected),"low level must never select realm");
        }
        check(!AutoPetMaps.canContinue(60,65,24),"level 60 must leave otherwise-valid outside map");
        check(AutoPetMaps.selectExcluding(60,24,24,maps)==139,"level 60 must enter 139");
        for(int i=0;i<AutoPetMaps.REALM.length;i++) {
            int[] r=AutoPetMaps.REALM[i];int map=r[0],min=r[1],max=r[2];
            check(AutoPetMaps.realmMap(min+1)==map,"stay until min+2");
            check(AutoPetMaps.realmMap(min+2)==(i+1<AutoPetMaps.REALM.length?map+1:148),"transition at min+2 except terminal map");
            check(AutoPetMaps.canContinue(min+1,min,map),"continue in correct realm map");
            check(AutoPetMaps.canContinue(min+2,min,map)==(map==148),"only terminal map stays after threshold");
            check(AutoPetMaps.targetMobLevel(max-10,map)==(max-10<60?-1:min),"gap 10 targets nearest only after unlock");
            check(AutoPetMaps.targetMobLevel(max-9,map)==(max-9<60?-1:0),"gap 9 allows all only after unlock");
            check(AutoPetMaps.targetMobLevel(59,map)==-1,"realm mobs locked below 60");
        }
        check(AutoPetMaps.realmMap(155)==148 && AutoPetMaps.realmMap(200)==148,"terminal map remains available above threshold");
        check(AutoPetMaps.targetMobLevel(41,19)==0,"outside targeting unchanged");
        System.out.println("PASS: "+checks+" map/realm rule assertions");
    }
}
