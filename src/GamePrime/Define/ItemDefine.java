package GamePrime.Define;

public class ItemDefine {
    public static class IntPair{
        public String name;
        public int value;
        public IntPair(String name,int value){
            this.name = name;
            this.value = value;
        }
    }
    public static IntPair[] StoreItem = { new IntPair("BonusLife",15), new IntPair("MoveSpeed",10), new IntPair("ShotSpeed",20)};
    public static String[] ActiveItem = { "Ghost", "Auxiliary", "Bomb","SpeedUp"};
}