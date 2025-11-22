x=[1.0;2.0;3.0;4.0;5.0];
f=[0.0;0.6;1.77;1.92;3.31];
p=polyfit(x,f,3)
xp=linspace(0.5,5.5);
pp=polyval(p,xp);
figure;
plot(x,f,'o');
hold on;
plot(xp,pp)
hold off;
