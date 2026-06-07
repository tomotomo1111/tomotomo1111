% demo3

clear;
a=ones(16000,16000);
ad=distributed(a);
spmd
    al=getLocalPart(ad);
    ad2=ad*2; % processing
    ad2_com=gather(ad2,1);
end