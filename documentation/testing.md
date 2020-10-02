# LZSS performance testing
Tests were performed with 152,1 kB text file of the book 'Alice's adventures in Wonderland', specifically chosen for testing compression algorithms for the Canterbury Corpus (more info here (https://corpus.canterbury.ac.nz/purpose.html)). 

First set of test were run with the number of length bits set as 4, the default number. The test file was encoded with different number of offset bits.

Results:

Original file size: 152089

Offset bits | Compressed file size | compression rate (compressed / original)
8 | 113635 | 74.7
9 | 101441 | 66.7
10 | 93129 | 61.2
11 | 88685 | 58.3
12 | 87688 | 57.7
13 | 88635 | 58.3
14 | 91062 | 59.9
15 | 94488 | 62.1
16 | 98555 | 64.8

Seems that the best compression rate is achieved with the number of offset bits set as 12 which translates as a sliding window size of 4095 bytes. While a bigger sliding window means there's a bigger chance of finding matching bytes, coding those matches with longer (offset length) codes worsens the compression rate after 12 bits.

Next set of test were run with the number of offset bits set as 11, the default number while changing the number of length bits.


Results:

Original file size: 152089

Length bits | Compressed file size | compression rate (compressed / original)
2 | 102885 | 67.64789037997488
3 | 87664 | 57.63993451202914
4 | 88685 | 58.31125196431037
5 | 92105 | 60.55993530104084
6 | 95872 | 63.03677452018226
7 | 99689 | 65.54648922670279
8 | 103531 | 68.07264167691287
9 | 107373 | 70.59879412712293

The changes follow a same trend as when changing the number of offset bits: after the ideal number of bits, the compression rate starts to gradually worsen. The sweet spot seems to be 3 bits when coding the length of matches.
