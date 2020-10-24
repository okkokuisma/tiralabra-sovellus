
package OKcompress.domain;

/**
 * Nodes used in IntegerHashMaps.
 */
public class IntegerHashMapNode {
    int key;
    int value;
    IntegerHashMapNode nextNode;
    int hashValue;
    
    public IntegerHashMapNode(int key, int value, IntegerHashMapNode nextNode) {
        this.key = key;
        this.value = value;
        this.nextNode = nextNode;
    }
}
