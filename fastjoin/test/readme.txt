Version: 0.1
Date: 2011-02-10
Author: Jiannan Wang
URL: http://dbgroup.cs.tsinghua.edu.cn/wangjn/projects/fastjoin/



== win32 ==

1. Test self-join

-------------------------------------------------------------------------------------------
..\win32\FastJoin.exe FJACCARD 0.85 0.8 self-join\querylog.format.sample
..\win32\FastJoin.exe FJACCARD 0.85 0.8 self-join\author.format.sample
..\win32\FastJoin.exe FCOSINE 0.85 0.8 self-join\querylog.format.sample
..\win32\FastJoin.exe FCOSINE 0.85 0.8 self-join\author.format.sample
..\win32\FastJoin.exe FDICE 0.85 0.8 self-join\querylog.format.sample
..\win32\FastJoin.exe FDICE 0.85 0.8 self-join\author.format.sample
-------------------------------------------------------------------------------------------



2. Test join

-------------------------------------------------------------------------------------------
..\win32\FastJoin.exe FJACCARD 0.85 0.8 join\author.format.1000 join\author.format.9000
..\win32\FastJoin.exe FJACCARD 0.85 0.8 join\author.format.9000 join\author.format.1000
..\win32\FastJoin.exe FJACCARD 0.85 0.8 join\querylog.format.1000 join\querylog.format.9000
..\win32\FastJoin.exe FJACCARD 0.85 0.8 join\querylog.format.9000 join\querylog.format.1000
-------------------------------------------------------------------------------------------


3. Test exsiting similarity functions

Jaccard (Compute all similar string pairs JACCARD(s1,s2) >= 0.8)
-------------------------------------------------------------------------------------------
..\win32\FastJoin.exe FJACCARD 1 0.8 self-join\querylog.format.sample
..\win32\FastJoin.exe FJACCARD 1 0.8 self-join\author.format.sample
-------------------------------------------------------------------------------------------

Cosine (Compute all similar string pairs COSINE(s1,s2) >= 0.8)
-------------------------------------------------------------------------------------------
..\win32\FastJoin.exe FCOSINE 1 0.8 self-join\querylog.format.sample
..\win32\FastJoin.exe FCOSINE 1 0.8 self-join\author.format.sample
-------------------------------------------------------------------------------------------

Dice (Compute all similar string pairs DICE(s1,s2) >= 0.8)
-------------------------------------------------------------------------------------------
..\win32\FastJoin.exe FDICE 1 0.8 self-join\querylog.format.sample
..\win32\FastJoin.exe FDICE 1 0.8 self-join\author.format.sample
-------------------------------------------------------------------------------------------

Edit Similarity (Compute all similar string pairs NED(s1,s2) >= 0.85. The similarity is ned(s1,s2) = 2*x/(x+1) where x is FJACCARD(s1, s2))
-------------------------------------------------------------------------------------------
..\win32\FastJoin.exe FJACCARD 0.85 0.1 self-join\querylog.format.sample.onetoken
..\win32\FastJoin.exe FJACCARD 0.85 0.1 self-join\author.format.sample.onetoken
-------------------------------------------------------------------------------------------


== Linux ==

0. Make FastJoin executable

chmod 777 ../linux/FastJoin

1. Test self-join
-------------------------------------------------------------------------------------------
../linux/FastJoin FJACCARD 0.85 0.8 self-join/querylog.format.sample
../linux/FastJoin FJACCARD 0.85 0.8 self-join/author.format.sample
../linux/FastJoin FCOSINE 0.85 0.8 self-join/querylog.format.sample
../linux/FastJoin FCOSINE 0.85 0.8 self-join/author.format.sample
../linux/FastJoin FDICE 0.85 0.8 self-join/querylog.format.sample
../linux/FastJoin FDICE 0.85 0.8 self-join/author.format.sample
-------------------------------------------------------------------------------------------

2. Test join

-------------------------------------------------------------------------------------------
../linux/FastJoin FJACCARD 0.85 0.8 join/author.format.1000 join/author.format.9000
../linux/FastJoin FJACCARD 0.85 0.8 join/author.format.9000 join/author.format.1000
../linux/FastJoin FJACCARD 0.85 0.8 join/querylog.format.1000 join/querylog.format.9000
../linux/FastJoin FJACCARD 0.85 0.8 join/querylog.format.9000 join/querylog.format.1000
-------------------------------------------------------------------------------------------



3. Test exsiting similarity functions

Jaccard (Compute all similar string pairs JACCARD(s1,s2) >= 0.8)
-------------------------------------------------------------------------------------------
../linux/FastJoin FJACCARD 1 0.8 self-join/querylog.format.sample
../linux/FastJoin FJACCARD 1 0.8 self-join/author.format.sample
-------------------------------------------------------------------------------------------

Cosine (Compute all similar string pairs COSINE(s1,s2) >= 0.8)
-------------------------------------------------------------------------------------------
../linux/FastJoin FCOSINE 1 0.8 self-join/querylog.format.sample
../linux/FastJoin FCOSINE 1 0.8 self-join/author.format.sample
-------------------------------------------------------------------------------------------

Dice (Compute all similar string pairs DICE(s1,s2) >= 0.8)
-------------------------------------------------------------------------------------------
../linux/FastJoin FDICE 1 0.8 self-join/querylog.format.sample
../linux/FastJoin FDICE 1 0.8 self-join/author.format.sample
-------------------------------------------------------------------------------------------


Edit Similarity (Compute all similar string pairs ned(s1,s2) >= 0.85. The similarity is ned(s1,s2) = 2*x/(x+1) where x is FJACCARD(s1, s2))
-------------------------------------------------------------------------------------------
../linux/FastJoin FJACCARD 0.85 0.1 self-join/querylog.format.sample.onetoken
../linux/FastJoin FJACCARD 0.85 0.1 self-join/author.format.sample.onetoken
-------------------------------------------------------------------------------------------