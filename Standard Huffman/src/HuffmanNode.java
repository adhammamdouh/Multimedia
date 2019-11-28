public class HuffmanNode {
    public boolean isParent;
    public double frequency;
    public char symbol;

    public HuffmanNode left;
    public HuffmanNode right;

    HuffmanNode(){

    }
    HuffmanNode(char symbol, double frequency, boolean isParent){
        this.symbol = symbol;
        this.frequency = frequency;
        this.isParent = isParent;
        this.left = null;
        this.right = null;
    }
}
