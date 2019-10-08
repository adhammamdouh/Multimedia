import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LZ78 {
    private String data;
    private List<String> compressTable;
    private List<String> tags;
    private int lastIndex;
    private FileWriter outTags;
    private FileWriter outTable;
    private FileReader inTags;
    private FileReader inTable;

    LZ78(String tagsFilePath, String tableFilePath) throws IOException {
        this.outTags = new FileWriter(tagsFilePath);
        this.outTable = new FileWriter(tableFilePath);

        this.data = new String();
        this.compressTable = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.lastIndex = 0;

        this.addToCompressTable("NULL");
    }

    public void compress(String data) throws IOException {
        this.data = data;
        System.out.println(data.length());
        int len = this.data.length();
        String currentSubStr = new String();
        int[] indexInTable = {0} ;

        for(int i = 0 ; i < len ; ++i){
            boolean exists = exists(currentSubStr, data.charAt(i), indexInTable);
            if(!exists){
                generateTag(indexInTable[0], data.charAt(i) + "");
                addToCompressTable(currentSubStr + data.charAt(i));

                indexInTable[0] = 0;
                currentSubStr = "";
            }
            else if(exists && i == (len - 1)){
                generateTag(indexInTable[0], "NUll");
            }
            currentSubStr += ((!exists)? "" : data.charAt(i));
        }
        this.closeFiles();
    }

    private void closeFiles() throws IOException {
        this.outTags.close();
        this.outTable.close();
    }

    private void addToCompressTable(String subString) throws IOException {
        this.compressTable.add(subString);

        this.outTable.write(lastIndex + "," + subString + '\n');
        this.outTable.flush();

        ++this.lastIndex;
    }

    private void generateTag(int indexInTable, String nextChar) throws IOException {
        this.tags.add("<" + indexInTable + "," + nextChar + ">");

        this.outTags.write("<" + indexInTable + "," + nextChar + ">" + '\n');
        this.outTags.flush();
    }

    private boolean exists(String currentSubString, char nextChar,  int[] indexInTable) {
        for(int i = 0 ; i < this.lastIndex ; ++i){
            if(this.compressTable.get(i).compareToIgnoreCase(currentSubString) == 0){
                indexInTable[0] = i;
            }
            if(this.compressTable.get(i).compareToIgnoreCase(currentSubString + nextChar) == 0){
                indexInTable[0] = i;
                return true;
            }
        }
        return false;
    }

    public String decompress(List<String> tags, String tableFilePath) throws IOException {
        List<String> table = convertToTableList(tableFilePath);
        return this.mainCompress(tags, table);
    }

    public String decompress(String tagsFilePath, String tableFilePath) throws IOException {
        List<String> tags = convertToTagsList(tagsFilePath);
        List<String> table = convertToTableList(tableFilePath);

        return this.mainCompress(tags, table);
    }

    private String mainCompress(List<String> tags, List<String> table){
        int tagsLen = tags.size();
        String result = new String();

        for(int i = 0 ; i < tagsLen ; ++i){
            int[] index = {0};
            char[] nextChar = new char[1];
            getParameters(tags.get(i), index, nextChar);
            result += table.get(index[0]) + nextChar[0];
        }
        return result;
    }

    private void getParameters(String tag, int[] index, char[] nextChar) {
        int len = tag.length();
        String temp = new String();
        int i = 0;

        for( i = 1 ; i < len ; ++i){
            if (tag.charAt(i) == ',') break;
            index[0] *= 10;
            index[0] += (int)tag.charAt(i) - (int)'0';
        }
        for(i = i+1; i < len-1 ; ++i){
            temp += tag.charAt(i);
        }

        if(temp.compareToIgnoreCase("NULL") == 0)nextChar[0] = Character.MIN_VALUE;
        else nextChar[0] = temp.charAt(0);
    }

    private List<String> convertToTagsList(String tagsFilePath) throws IOException {
        this.inTags = new FileReader(tagsFilePath);
        List<String> tags = new ArrayList<>();
        BufferedReader read = new BufferedReader(this.inTags);
        String str = new String();

        while ((str = read.readLine()) != null){
            tags.add(str);
        }

        this.inTags.close();
        read.close();

        return tags;
    }

    private List<String> convertToTableList(String tableFilePath) throws IOException {
        this.inTable = new FileReader(tableFilePath);
        List<String> table = new ArrayList<>();
        BufferedReader read = new BufferedReader(this.inTable);
        String str;

        while ((str = read.readLine()) != null){
            boolean space = false;
            String subString = new String();
            int i = 0;
            for(; i < str.length() ; ++i)
                if (str.charAt(i) == ',')
                    break;

            for(i = i+1 ; i < str.length() ; ++i)
                subString += str.charAt(i);

            table.add(((subString.compareToIgnoreCase("NULL")) == 0)? "" : subString);
        }
        this.inTable.close();
        read.close();
        return table;
    }

    public static void main(String[] args) throws IOException {
        LZ78 LZ = new LZ78("Tags.txt", "Table.txt");
        LZ.compress("AAAAA AA AAAA A A A A A A A AAAA AAA");
        System.out.println(LZ.decompress("Tags.txt", "Table.txt"));
    }
}
