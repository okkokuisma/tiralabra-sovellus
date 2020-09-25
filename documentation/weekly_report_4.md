# Week 4

## What have I done this week
I have made my algorithms leaner and more efficient by improving the way the program manipulates bytes and reads individual bits. I coded an utility class for reading byte arrays as a bit stream which greatly helps with decoding the encoded files. Added some tests but more work need to be done on that front.

## Progress so far
On paper it doesn't seem like I made huge progress this week as I didn't really add that much new functionality to the program but I feel like I made some important discoveries and improved what was already there significantly. The most obvious improvement was the change from a zero byte to a zero bit as a sign for uncoded byte in LZSS.

The Huffman algorithm is still not done but that's partly because I did some research on how Huffman coding is used in Deflate algorithm and decided that I'd like to take a punt on implementing it that way (and that requires a bit more work). Ran out of time with it this week.

## What have I learned this week
More and more about bitwise operations. Many binary strings were doodled on my notebook this week.

## Problems so far
Not really at the moment. I'll have to do some more research on Deflate and some problems may still arise with that. I always have the possibility of implementing the LZSS and Huffman coding as separate algorithms if Deflate takes too much time and effort.

## What will I do next
I want to finish the Huffman algorithm. Preferably in a way that enables me to use it as a part of Deflate. Then I'll have to start thinking about performance testing and all the other juicy documentation still waiting to be done.  
