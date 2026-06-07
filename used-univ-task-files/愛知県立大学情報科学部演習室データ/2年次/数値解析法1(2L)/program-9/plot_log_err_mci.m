close all; clear;

% 有界区間積分の誤差片対数グラフ
fprintf('***** Calculate the convergent order of MC method\n');

% 変数へ結果を代入
filename = 'exc-mci-err.dat';
delimiterIn = ' ';
edb = importdata(filename,delimiterIn);

% 収束次数格納用ファイル
fileID = fopen('corder_mmi.dat','w');

% polyfit関数による直線フィッティング
p1m = polyfit(log(edb(:,1)),log(edb(:,2)),1);
fprintf('Conv. order = %17.15e\n',abs(p1m(1)));
fprintf('a = %17.15e\nb = %17.15e\n',p1m(1),p1m(2));
fprintf(fileID,'\nConv. order = %17.15e\n',abs(p1m(1)));
fprintf(fileID,'a = %17.15e\nb = %17.15e\n',p1m(1),p1m(2));
fclose(fileID);

% 片対数グラフの描画と画像ファイルへの保存
xp1m = linspace(0.5*min(edb(:,1)),1.25*max(edb(:,1)),1000);
figure;
plot(log(edb(:,1)),log(edb(:,2)),"o");
hold on;
% 傾き-1/2の直線
ym = -0.5*xp1m + p1m(2);
plot(xp1m,ym);
% 線形最小二乗近似による直線
%pp1m = polyval(p1m,xp1m);
%plot(xp1m,pp1m);
hold off
xlim([0.5*min(log(edb(:,1))) 1.05*max(log(edb(:,1)))]);
ylim([1.05*min(log(edb(:,2))) 0.95*max(log(edb(:,2)))]);
xlabel('log(分割数)');
ylabel('log(絶対誤差)');
legend('Monte Carlo');
pbaspect([1 1 1]);
exportgraphics(gcf,'corder_mci.png');
