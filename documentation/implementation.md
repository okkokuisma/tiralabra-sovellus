# Implementation

## LZSS (Lempel-Ziv-Storer–Szymanski)
### Data structures
For my implementation of LZSS no other data structures are needed except for a dynamic list structure for storing the encoded and decoded data. The algorithm uses a sliding window of 2047 bytes as a dictionary. The window is maintained and read while reading the input file for the first time. This means the dictionary itself doesn't have to be stored at any point.

### Compression
While decoding the input file, at any given point the LZSS scans the sliding window directly preceding the byte being read for matching data. If found, these matches are coded as (offset, length) pairs where offset tells the decoder the starting point of the match (how many steps back from the point being read) and the length value entails the number of matching bytes. This (offset, length) pair is always preceded by a sign bit of 1 to tell the decoder that a coded sequence is coming up. If a match isn't found for a symbol, that symbol is added to the output uncoded preceded only by a sign bit of 0. 

The relatively small size of the sliding window ensures that the offset value can be coded with 11 bits. In addition to the sign bit 1, this leaves four bits for coding the length for a nice even chunk of 16 bits, or two bytes, for every (offset, length) pair. Matches are coded only if the length is over two bytes long which ensures that the coding actually saves space. Uncoded bytes always take up 9 bits in the decoded output where the extra bit is the sign bit 0. This is the main advantage of LZSS compared to the original LZ77 algorithm where every byte is coded with a (offset, length) pair, even if no match was found.

### Time complexity
(At the moment) my implementation uses a brute force way of scanning the sliding window for every new byte in the input. If a matching point is found, only the neighbouring bytes are read. If no matches are found at all the whole sliding window is scanned for every byte in the input. This gives a time complexity of O(nm) where n is the number of bytes in the input and m is the size of the sliding window. This is why the small dictionary size also helps with the performance of the algorithm. 

## Huffman coding
### Compression
Huffman coding is based on unique codes given for each byte value that occurs in the input data. These codes are generated based on the number of occurrences of each byte value: byte values that occur less are given longer codes while more common byte values are given shorter codes. When compressing a suitable file, such as a text file, this enables the file to be rewritten with less bits used as the most common byte values are coded using less than 8 bits. Decoding a Huffman coded file is made simple because of the prefix attribute of Huffman codes. There can't exist a code that is also a prefix of another code among a set of valid Huffman codes. This means that while reading the decoded data a bit at a time, when a byte value with a matching code is found, one can be sure that the code represents that particular byte value.  

While Huffman coding is quite efficient by itself at compressing data, it falls a bit behind of LZSS based on my testing. When compressing a smaller file in particular Huffman isn't the best choice as the lengths of the codes created in decoding have to be stored in the compressed file for decoding. In my implementation this takes up 255 * 8 = 1275 bits (approximately 159 bytes) at the start of the file. 

### Data structures
A dynamic list structure is again needed to store encoded and decoded data. Code lengths are determined using a minimum heap stucture. HuffmanNodes, objects created for every byte value in the input data, are polled from the heap to create a tree stucture. The depth of each node in the created tree determines the code length for the byte value it represents. While decoding, this tree structure is rebuilt using a normal array structure.

### Time complexity
The most intensive part of the algorithm is determining the used code lengths. (At the moment) the heap used to build a tree structure of the HuffmanNodes is created by adding each node individually with a time complexity of O(log n) where n is the number of different byte values found in the input file. That means the time complexity of building the heap amounts to O(n log n). Next, at each step two nodes with the smallest weights are removed to create a new subtree. The root of this subtree is added back to the heap so that the size of the heap decreases by one at each step. This is repeated until there is only one node remaining in the heap. As the time complexity of both adding and removing from a heap is O(log n), the time complexity of each step is O(log n). As there are n-1 steps, the time complexity of creating the tree structure is O(n log n). 
