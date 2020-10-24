# Testing

## Comparisons between the algorithms
There were four different text files used for testing the computing and compression performance of the three algorithms, LZSS, Huffman and DeflateLite. The smallest file was also the simplest in terms of content. The other three were taken from the Canterbury Corpus, a set of files specifically chosen for testing compression algorithms (more info here https://corpus.canterbury.ac.nz/purpose.html).

First compression performance was tested with all four files. File sizes as bytes.

Input size<br />|Compressed<br />file size<br />(LZSS)|Compression<br />rate<br />(LZSS)|Compressed<br />file size<br />(Huffman)|Compression<br />rate<br />(Huffman)|Compressed<br />file size<br />(DeflateLite)|Compression<br />rate<br />(DeflateLite)
:-- | :-- | :-- | :-- | :-- | :-- | :--
12900 | 7091 | 55.0% | 7394 | 57.3% | 6567 | 50.9% |
152100 | 87623 | 57.6% | 87851 | 57.8% | 79124 | 52.0% |
2473400 | 1478960 | 59.8% | 1558759 | 63.0% | 1323187 | 53.5% |
4000000 | 2074150 | 51.2% | 2218614 | 54.8% | 1865829 | 46.1% |

Compression rates seem to vary quite radically between the different files but DeflateLite manages to offer clearly the best compression each time, with LZSS being the second best and Huffman the last.

Next, computing times were timed when encoding and decoding the approximately 2.5 MB file, "world192.txt". The same file was encoded 10 times and then decoded 10 times and an average time was calculated. Both LZSS algorithms were timed for comparison. Times as seconds.

LZSS (brute force)<br />encoding | LZSS (queues)<br/>encoding | LZSS<br />decoding | Huffman<br />encoding | Huffman<br />decoding | DeflateLite<br />encoding | DeflateLite<br/>decoding
:-- | :-- | :-- | :-- | :-- | :-- | :--
4.22 | 1.48 | 0.17 | 0.17 | 0.14 | 2.38 | 0.17 | 

Huffman might offer the worst compression rate of the three algorithms but it's quite clearly the most efficient in terms of computing performance. Although using queues to store possible matching points offers a notable boost to LZSS, it still can't beat Huffman coding with a time complexity of O(n log n). DeflateLite, which uses the queue version of LZSS as a basis for its compression, loses clearly to Huffman as well. Differences in decoding times don't seem to be significant.

With my implementation of these famous algorithms, the choice between the three of them comes down to which is preferred, compression rate or time. Huffman loses with compression but wins comfortably with efficiency while DeflateLite offers the best compression rate but the worst computing performance.

## LZSS
Next set of tests focus on LZSS and the differences in the algorithm's performance when the number of offset and length bits are tinkered with. Tests were performed with a 152,1 kB text file of the book 'Alice's Adventures in Wonderland', specifically chosen for testing compression algorithms for the Canterbury Corpus (more info here https://corpus.canterbury.ac.nz/purpose.html). 

First set of tests were run with the number of length bits set as 4, the default number. The test file was encoded with varying number of offset bits.

### LZSS compression testing

Offset bits<br />(dictionary size) | Compressed <br />file size | Compression<br />rate
:-- | :-- | :--
8 (256 B) | 113,6 kB | 74.7%
9 (512 B) | 101,4 kB | 66.7%
10 (1024 B) | 93,1 kB | 61.2%
11 (2048 B) | 88,7 kB | 58.3%
12 (4096 B) | 87,7 kB | 57.7%
13 (8192 B) | 88,7 kB | 58.3%
14 (16384 B) | 91,1 kB | 59.9%
15 (32768 B) | 94,5 kB | 62.1%
16 (65536 B) | 98,6 kB | 64.8%

Seems that the best compression rate is achieved with the number of offset bits set as 12 which translates to a sliding window size of 4095 bytes. While a bigger sliding window means there's a bigger chance of finding matching bytes, coding those matches with longer (offset, length) codes worsens the compression rate after 12 bits.

Next set of test were run with the number of offset bits set as 11, the default number, while changing the number of length bits.

Length bits | Compressed <br />file size | Compression<br />rate
:-- | :-- | :--
2 | 102,9 kB | 67.7%
3 | 87,7 kB | 57.6%
4 | 88,7 kB | 58.3%
5 | 92,1 kB | 60.6%
6 | 95,9 kB | 63.0%
7 | 99,7 kB | 65.6%
8 | 103,6 kB | 68.1%
9 | 107,4 kB | 70.6%

The changes follow the same trend as when changing the number of offset bits: after the ideal number of bits, the compression rate starts to gradually worsen. The sweet spot seems to be 3 bits when coding the length of matches.

### LZSS performance testing
Next I tested the differences in performance between the two LZSS algorithms, one using a brute force method of scanning the whole sliding window for matches, the other one utilising queues to store the index of every occurrence of each byte value. The same 152,1 kB file was encoded 10 times with each number of offset bits and an average time was calculated.

Offset bits | Queues<br />(seconds) | Brute force<br />(seconds)
:-- | :-- | :--
8 | 0.060 | 0.078
9 | 0.052 | 0.096
10 | 0.062 | 0.130
11 | 0.081 | 0.182
12 | 0.107 | 0.252
13 | 0.137 | 0.325
14 | 0.168 | 0.421
15 | 0.205 | 0.524
16 | 0.251 | 0.629
