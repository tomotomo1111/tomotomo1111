n=16;
a=-1; b=1;
x=linspace(a,b,n+1); %分点 
f = 1./(1+25*x.*x);  %分点における関数値
xp = linspace(a,b,201); %補間値を計算する点
plot(x,f,'o',xp,csapi(x,f,xp));