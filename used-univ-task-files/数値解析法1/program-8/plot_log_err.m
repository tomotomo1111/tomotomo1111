close all;
% 両対数グラフと収束オーダーの確認
% 出力ファイルの読み込み
load exc-dif-err.dat;

% 最小二乗近似直線
p1f=polyfit(log(exc_dif_err(:,1)),log(exc_dif_err(:,2)),1);
p1b=polyfit(log(exc_dif_err(:,1)),log(exc_dif_err(:,3)),1);
p1c=polyfit(log(exc_dif_err(:,1)),log(exc_dif_err(:,4)),1);

% 収束オーダーの確認
fprintf('収束オーダー（前進差分）= %e\n',abs(p1f(1)));
fprintf('収束オーダー（後退差分）= %e\n',abs(p1b(1)));
fprintf('収束オーダー（中心差分）= %e\n',abs(p1c(1)));

% 両対数グラフ
xp1 = linspace(0.9*min(log(exc_dif_err(:,1))),1.05*max(log(exc_dif_err(:,1))),1000);
pp1f = polyval(p1f,xp1);
pp1b = polyval(p1b,xp1);
pp1c = polyval(p1c,xp1);
figure;
plot(log(exc_dif_err(:,1)),log(exc_dif_err(:,2)),'o');
hold on;
plot(log(exc_dif_err(:,1)),log(exc_dif_err(:,3)),'+');
plot(log(exc_dif_err(:,1)),log(exc_dif_err(:,4)),'*');
plot(xp1,pp1f);
plot(xp1,pp1b);
plot(xp1,pp1c);
hold off;
xlim([0.9*min(log(exc_dif_err(:,1))) 1.05*max(log(exc_dif_err(:,1)))]);
ylim([1.2*min(log(exc_dif_err(:,4))) 1.0]);
xlabel('log(分割数)');
ylabel('log(絶対誤差)');
legend('前進差分','後退差分','中心差分')
pbaspect([1 1 1]);
exportgraphics(gcf,'fit_nd_err.png');