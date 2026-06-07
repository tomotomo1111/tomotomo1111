% demo2.m

clear;
spmd
	a=1;
    b=rand(10,10);
	c=labindex;
end