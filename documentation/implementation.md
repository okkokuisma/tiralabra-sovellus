# Implementation

## LZSS (Lempel-Ziv-Storer–Szymanski)
### Data structures
For my implementation of LZSS no other data structures are needed except for a dynamic list structure for storing the encoded and decoded data. The algorithm uses a sliding window of 2047 bytes as a dictionary. The window is maintained and read while reading the input file for the first time. This means the dictionary itself doesn't have to be stored at any point.

### Compression
While decoding the input file, at any given point the LZSS scans the sliding window directly preceding the byte being read for matching data. If found, these matches are coded as (offset, length) pairs where offset tells us how many steps back the starting points of the matching data is from the point being read at the moment and the length value entails for how many bytes the match goes on from this starting point. This (offset, length) pair is always preceded by a sign bit of 1 to tell the decoder that a coded sequence is coming up. If a match isn't found for a symbol, that symbol is added to the output uncoded, preceded only by a sign bit of 0. 

The relatively small size of the sliding window ensures that the offset value can be coded with 11 bits. In addition to the sign bit 1, this leaves four bits for coding the length for a nice even chunk of 16 bits of two bytes for every (offset, length) pair. Matches are coded only if the length is over two bytes long so we can ensure the coding actually saves us space. Uncoded bytes are always 9 bits long where the extra bit is the sign bit 0. This is the main advantage of LZSS compared to the original LZ77 algorithm where even uncoded symbols were given a (offset, length) pair.

### Time complexity
(At the moment) my implementation uses a brute force way of scanning the sliding window for every new byte in the input. If a matching point is found, only the neighbouring bytes are read. If no matches are found at all the whole sliding window is scanned for every byte in the input. This gives a time complexity of O(nm) where n is the number of bytes in the input and m is the size of the sliding window. This is why the small dictionary size also helps with the performance of the algorithm. 
