# Week 5

## What have I done this week
This week I finally got a fully working version of Huffman coding done, modified the LZSS algorithm so that the number of bits in (offset, length) pairs are not hard coded and started with the implementation and testing documentation. Also added JavaDoc comments.

## Progress so far
I now have implementations of both LZSS and Huffman coding done and they seem to be working well. I have the data structures I'm using at the moment coded as well. However, I may still look into using more efficient data structures in LZSS. Started the week by plunging into documentation of Deflate algorithm but decided that upgrading my measly algorithms into Deflate would be too much work. So that project lasted only a week after all my enthusiasm last week, heh.

## What have I learned this week
More on Huffman coding, especially how it's used in Deflate. Clever stuff. Even if I won't continue on that road, I decided to borrow the way Deflate stores information about Huffman trees on file headers and how Huffman codes are generated from code lengths.

## Problems so far
Right now stuff seems to be working. 

## What will I do next
I'll look into making my algorithms, especially LZSS, more efficient. I'll continue with documentation and add to the testing.

## Hours spent this week
About 16.
