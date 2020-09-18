
package OKcompress.domain;

/**
 *
 * @author ogkuisma
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    public byte byteValue;
    public int weigth;
    public HuffmanNode leftChild;
    public HuffmanNode rightChild;

    public HuffmanNode() {
    }


    public HuffmanNode(byte byteValue, int weigth) {
        this.byteValue = byteValue;
        this.weigth = weigth;
    }

    @Override
    public int compareTo(HuffmanNode compared) {
        return this.weigth - compared.weigth;
    }
    
}
