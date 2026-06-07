n=16;
a=-1; b=1;
x=linspace(a,b,n+1); %分点 
f = 1./(1+25*x.*x);  %分点における関数値
xp = linspace(a,b,201); %補間値を計算する点
fval = spline(x, [25/338 f -25/338], xp); %端点の導関数値も追加
plot(x,f,'o',xp,fval);