# Design document
My aim is to create working implementations of some version of LZ77 based algorithms and Huffman coding. My target compression rate is between 40 and 60 percent of the original file size, as mentioned in the course material. The algorithms should work with different file types (compression rate may vary, of course) and produce the same exact file when decoded.

## Data structures
LZ77 based algorithms can be implemented with many different data structures. The performance of the algorithm is mostly determined by how the dictionary is stored and how matching symbols are searched. I'm planning on coding a working brute force implementation and improving the performance from there.

Huffman coding is usually based on a minimum heap structure to determine different codes for byte values that exist in the input data. 

## Sources
* https://zlib.net/feldspar.html
* https://michaeldipperstein.github.io/lzss.html
* https://sites.google.com/site/datacompressionguide/lz77
* http://web.stanford.edu/class/archive/cs/cs106x/cs106x.1192/resources/minibrowser2/huffman-encoding-supplement.pdf
