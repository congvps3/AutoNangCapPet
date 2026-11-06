/** Match runtime labels, not guessed numeric option IDs. */
public final class AutoPetRules {
    public static final int PET=1305, MIKA=1282, TONE=22;
    public static String plain(String text) {
        if(text==null) return "";
        String from="àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ";
        String to=  "aaaaaaaaaaaaaaaaaeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyd";
        text=text.toLowerCase();StringBuffer b=new StringBuffer();boolean space=false;
        for(int i=0;i<text.length();i++) {
            char c=text.charAt(i);int p=from.indexOf(c);
            if(p>=0) c=to.charAt(p);
            if(c=='\n' || c=='\r' || c=='\t' || c==' ') {if(!space && b.length()>0) b.append(' ');space=true;}
            else {b.append(c);space=false;}
        }
        return b.toString().trim();
    }
    public static int option(String label) {
        String s=plain(label);
        if(s.indexOf("tan cong")==0) return s.indexOf('%')>=0?7:0;
        if(s.indexOf("bang cong")==0) return 1;
        if(s.indexOf("hoa cong")==0) return 2;
        if(s.indexOf("phong loi")==0) return 3;
        if(s.indexOf("chi mang")==0) return 4;
        if(s.indexOf("hp toi da")==0) return 5;
        if(s.indexOf("mp toi da")==0) return 6;
        if(s.indexOf("mien giam sat thuong")==0) return 8;
        if(s.indexOf("hoi phuc")>=0 && s.indexOf("nua giay")>=0 && s.indexOf("hp")>=0 && s.indexOf("mp")>=0) return 9;
        if(s.indexOf("kinh nghiem")>=0 && s.indexOf("quai")>=0) return 10;
        return -1;
    }
    public static int menu(String label) {
        String s=plain(label);
        if(s.equals("nhan vat pham")) return AutoPetEngine.RECEIVE;
        if(s.equals("luyen trang bi")) return AutoPetEngine.TRAIN;
        if(s.equals("nang cap vat pham")) return AutoPetEngine.UPGRADE;
        return 0;
    }
    public static boolean shop(int action,String title) {
        String s=plain(title);
        if(action==AutoPetEngine.RECEIVE) return s.equals("nhan vat pham");
        if(action==AutoPetEngine.TRAIN) return s.equals("luyen") || s.equals("luyen trang bi");
        if(action==AutoPetEngine.UPGRADE) return s.equals("nang cap vat pham");
        return false;
    }
    public static boolean confirmation(int action,String text) {
        String s=plain(text);
        if(action==AutoPetEngine.TRAIN) return s.equals("xac nhan luyen vat pham da chon?");
        if(action==AutoPetEngine.UPGRADE) return s.indexOf("nang cap:")>=0 && s.indexOf("xac nhan nang cap?")>=0;
        return false;
    }
    public static boolean upgradeFailure(String text) {
        String s=plain(text);
        if(s.indexOf("khong du")>=0 || s.indexOf("chua du")>=0 || s.indexOf("khong the")>=0) return false;
        return s.indexOf("that bai")>=0 && (s.indexOf("nang cap")>=0 || s.equals("that bai") || s.equals("that bai!"));
    }
}
