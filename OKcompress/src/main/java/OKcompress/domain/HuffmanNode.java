
package OKcompress.domain;

/**
 * Nodes used in HuffmanHeaps and Huffman trees.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    public byte byteValue;
    public int weight;
    public HuffmanNode leftChild;
    public HuffmanNode rightChild;

    public HuffmanNode() {
    }


    public HuffmanNode(byte byteValue, int weight) {
        this.byteValue = byteValue;
        this.weight = weight;
    }
    
    @Override
    public int compareTo(HuffmanNode compared) {
        return this.weight - compared.weight;
    } 
}
