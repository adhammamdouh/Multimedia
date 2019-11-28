/**
 * Created by Adham mamdouh
 * Date : 11/28/2019
 * JDk Version : 11.0.2
 */


import java.util.*;

public class StandardHuffman {
    public HuffmanNode root;

    StandardHuffman() {
        root = null;
    }

    private String getSymbol(HuffmanNode root, String searchCode, String symbolCode) {
        if (root == null) return "";
        else if (!root.isParent && searchCode.equals(symbolCode)) return root.symbol + "";
        return getSymbol(root.left, searchCode, symbolCode + "0")
                + getSymbol(root.right, searchCode, symbolCode + "1");
    }

    private String getSymbolCode(HuffmanNode root, char symbol, String symbolCode) {
        if (root == null) return "";
        else if (!root.isParent && symbol == root.symbol) return symbolCode;
        return getSymbolCode(root.left, symbol, symbolCode + "0")
                + getSymbolCode(root.right, symbol, symbolCode + "1");
    }

    private void buildTree(PriorityQueue<HuffmanNode> q) {
        while (q.size() > 1) {
            HuffmanNode left = q.peek();
            q.poll();
            HuffmanNode right = q.peek();
            q.poll();
            HuffmanNode parent = new HuffmanNode('-', left.frequency + right.frequency, true);

            parent.left = left;
            parent.right = right;
            root = parent;

            q.add(parent);
        }
    }

    private void printTree(HuffmanNode root, String symbolCode) {
        if (root == null) return;
        else if (!root.isParent) System.out.println(root.symbol + " :" + symbolCode + " : " + root.frequency);
        printTree(root.left, symbolCode + "0");
        printTree(root.right, symbolCode + "1");
    }

    public String compress(String data) {
        String result = new String();
        Map<Character, Integer> frequencyMap = new HashMap<>();

        int dataLen = data.length();
        for (int i = 0; i < dataLen; ++i) {
            if (frequencyMap.containsKey(data.charAt(i)))
                frequencyMap.put(data.charAt(i), frequencyMap.get(data.charAt(i)) + 1);
            else
                frequencyMap.put(data.charAt(i), 1);
        }

        PriorityQueue<HuffmanNode> nodePriorityQueue = new PriorityQueue<>(frequencyMap.size(), new NodeComparator());

        for (Map.Entry<Character, Integer> i : frequencyMap.entrySet()) {
            HuffmanNode node = new HuffmanNode(i.getKey(), (i.getValue() / (double) dataLen), false);
            nodePriorityQueue.add(node);
        }

        buildTree(nodePriorityQueue);

        for (int i = 0; i < dataLen; ++i) {
            result += getSymbolCode(this.root, data.charAt(i), "");
        }

        return result;
    }

    public String decompress(String data, HuffmanNode root) {
        int dataLen = data.length();
        String result = new String();
        String curStream = new String();

        for (int i = 0; i < dataLen; ++i) {
            curStream += data.charAt(i);
            String symbol = this.getSymbol(root, curStream, "");

            if (symbol.equals("")) continue;

            result += symbol;
            curStream = "";
        }
        return result;
    }

    public void printTree(HuffmanNode root) {
        this.printTree(root, "");
    }

    public static void main(String[] args) {
        StandardHuffman SH = new StandardHuffman();
        String data = SH.compress("adham mamdouh mohamed mohamed ahmed");
        System.out.println(data);
        data = SH.decompress(data, SH.root);
        System.out.println(data);
        SH.printTree(SH.root);
    }
}
