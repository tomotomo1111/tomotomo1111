% program1.m

clear;
npar=16;
rc=8400;
A=ones(rc,rc);

parfor i=1:npar
    tmp=A(:,(i-1)*rc/npar+1:i*rc/npar)*i;
    e(i,:)=reshape(tmp,1,[]);
end