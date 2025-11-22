%program2.m

clear;
npar=16;
rc=8400;
A=ones(rc,rc);

a=distributed(A)

spmd(npar)
    b=labindex;
    c=a*b;
    d=gather(c,1);
end
e=d{1}