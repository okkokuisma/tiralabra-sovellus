# Week 2

## What have I done this week
I joined this course a week late so this week I had some catching up to do. Decided to got with one of the recommended topics because I didn't really have the time to look something else up. So far haven't been dissappointed: data compression has been quite fun! 

I hadn't heard of Huffman coding or LZ77 before so the first days went by figuring out what I was getting into. By wednesday I had the core of my LZSS algorithm done, on thursday I sort of figured out the decoding part of the algorithm and today (friday) I seem to have a somewhat working algorithm up and running.

## Progress so far
At the moment I only have the first version of my LZSS algorithm and an utility class for bit manipulation done.

## What have I learned this week
Quite a bit, I'd say. Haven't really had to manipulate individual bits before so that's a new. Had only seen bitwise operations in theory before. The algorithms themselves were new for me as was the general logic behind compression algorithms. So basically almost everything has been new to me and figuring things out has taken time and effort but that's the point of this course, I guess.

## Problems so far
I'm a bit unsure of the way I'm manipulating bytes at the moment. I'm quite content with the way I'm writing bytes but the reading seems a bit clumsy. Basically I'm parsing bytes to binary Strings in order to read and manipulate the individual bits. It works but I feel like there is a better way out there. 

The biggest question mark for me is what would be the best way to read uncoded symbols from the encoded data. I have the means to encode uncoded symbols into just 9 bits, and doing so, get the best compression rate out of the algorithm. I just didn't come up with a smart way of reading the encoded data in a situation where I couldn't manipulate the bits as nice even chunks of 8. So I ended up using a zero byte as an indicator instead of a single bit which is a huge waste of course. I'll keep thinking of my options.

## What will I do next
I think I'll start with implementing Huffman coding. Then I'll decide which data structures I want to use for my LSZZ. It's using a brute force method at the moment so there's plenty to improve. I'll start implementing the required structures if there's time.

## Hours spent this week
Roughly 20.   
