tic;
nhist=1e7;
count=0;
time1=toc;
tic;
parfor i=1:nhist
   length=norm(rand(1,2));

   if (length<=1)
       count=count+1;
   end
end

time2=toc
tic;
res=count/nhist*4
res-pi
time3=toc;

N=4;
amu=(time1 + time3) / (time1+time2+time3);
amu
Sn=1/(amu+(1-amu)/N);
sn=5.7835/time2;
En=sn/Sn;
en=sn/N;
sn
Sn
en
En