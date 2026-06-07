clearvars; clf; close all;
% 差分法（1次元）例題

% 例題設定
itval = [0 1];
ubd = [0 0];
k = 6;
cofp = @(x) 0;
cofq = @(x) k^2;
funcr = @(x) x;

% 初期分割数
n = 10;

% 分割回数
mmax = 4;

% 収束オーダー確認
hdiv = zeros(mmax,1);
abserr = zeros(mmax,1);
for m = 1:mmax
    % 差分法による近似解
    [xfdm, ufdm] = fdm1d(n,itval,cofp,cofq,funcr,ubd);
    % 真の解との絶対値最大誤差計算
    hdiv(m) = 1.0/(n+1);
    exvec = exactu1d(xfdm,k);
    abserr(m) = norm(ufdm-exvec,"inf");
    fprintf('h = %e:  Max error = %17.15e\n',hdiv(m),abserr(m));
    n = 10 * n;
end

% 真の解と近似解のプロット
figure;
xsol = itval(1):(itval(2)-itval(1))/10000:itval(2);
exactplt = exactu1d(xsol,k);
plot(xfdm,ufdm,'o',xsol,exactplt,'-');
xlim(itval);
ylim([0 1.1*max(ufdm)]);
saveas(gcf,'sol_fdm1d.png');

% 両対数グラフと収束オーダーの確認
p1e = polyfit(log10(1.0./hdiv),log10(abserr),1);
fprintf('\nConv. order = %e\n',abs(p1e(1)));
xp1e = linspace(0.5*min(log10(1.0./hdiv)),1.25*max(log10(1.0./hdiv)),1000);
pp1e = polyval(p1e,xp1e);
figure;
plot(log10(1.0./hdiv),log10(abserr),"o");
hold on;
plot(xp1e,pp1e);
hold off
xlim([0.5*min(log10(1.0./hdiv)) 1.25*max(log10(1.0./hdiv))]);
ylim([1.2*min(log10(abserr)) 0.75*max(log10(abserr))]);
saveas(gcf,'corder_fdm1d.png');
