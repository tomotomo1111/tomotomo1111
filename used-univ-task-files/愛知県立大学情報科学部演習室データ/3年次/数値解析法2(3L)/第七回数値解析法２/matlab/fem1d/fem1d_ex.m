clearvars;
close all;
% 1次元FEM例題
itval = [0 1];
cofp = @(x) 1;
cofq = @(x) pi^2;
dcofp = @(x) 0;
srcf = @(x)  17*pi^2*sin(4*pi*x);
bdval = [1 exp(-pi)];
% 分割回数
mmax = 4;
hdiv = zeros(mmax,1);
l2err = zeros(mmax,1);
n = 10;
for m = 1:mmax
    [x, sol] = fem1d(n+2,itval,bdval,cofp,cofq,dcofp,srcf);
    exu = exactu(x)';
    errvec = sol - exu;
    hdiv(m) = (itval(2)-itval(1))/(n+1);
    l2err(m) = 0.0;
    for i = 1:n+1
        l2err(m) = l2err(m) + errvec(i)^2+errvec(i)*errvec(i+1)+errvec(i+1)^2;
    end
    l2err(m) = hdiv(m)/3*l2err(m);
    l2err(m) = sqrt(l2err(m));
    fprintf('h = %e:  L2 error = %17.15e\n',hdiv(m),l2err(m));
    n = 10* n;
end
% 真の解と近似解プロット
figure;
plot(x,sol,'o',x,exu,'-');
xlim(itval);
%ylim([-1.1*max(abs(sol)) 1.1*max(abs(sol))]);
saveas(gcf,'sol_fem1d.png');

% 両対数グラフと収束オーダーの確認
p1e = polyfit(log10(1.0./hdiv),log10(l2err),1);
fprintf('\nConv. order = %e\n',abs(p1e(1)));
xp1e = linspace(0.5*min(log10(1.0./hdiv)),1.25*max(log10(1.0./hdiv)),1000);
pp1e = polyval(p1e,xp1e);
figure;
plot(log10(1.0./hdiv),log10(l2err),"o");
hold on;
plot(xp1e,pp1e);
hold off
xlim([0.5*min(log10(1.0./hdiv)) 1.25*max(log10(1.0./hdiv))]);
ylim([1.2*min(log10(l2err)) 0.75*max(log10(l2err))]);
saveas(gcf,'corder_fem1d.png');