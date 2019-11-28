import java.util.Comparator;

public class NodeComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        if (x.frequency > y.frequency)
            return 1;
        else if (x.frequency < y.frequency)
            return -1;
        else
            return 0;
    }
}
