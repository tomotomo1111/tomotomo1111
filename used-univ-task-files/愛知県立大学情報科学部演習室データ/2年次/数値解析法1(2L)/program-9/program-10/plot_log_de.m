close all; clear;

% 有界区間積分の誤差片対数グラフ
fprintf('***** Calculate the convergent order of DE formula (finite interval case)\n');

% ヘッダー部分を除いて変数へ結果を代入
edb = readmatrix('exc-de-bound.dat','NumHeaderLines',1);

% 収束次数格納用ファイル
fileID = fopen('corder_de_bound.dat','w');

% polyfit関数による直線フィッティング
p1bt = polyfit(edb(:,1),log(edb(:,3)),1);
fprintf('Conv. order = %17.15e\n',abs(p1bt(1)));
fprintf('a = %17.15e\nb = %17.15e\n',p1bt(1),p1bt(2));
fprintf(fileID,'\nConv. order = %17.15e\n',abs(p1bt(1)));
fprintf(fileID,'a = %17.15e\nb = %17.15e\n',p1bt(1),p1bt(2));
fclose(fileID);

% 片対数グラフの描画と画像ファイルへの保存
xp1bt = linspace(0.5*min(edb(:,1)),1.25*max(edb(:,1)),1000);
pp1bt = polyval(p1bt,xp1bt);
figure;
plot(edb(:,1),log(edb(:,3)),"o");
hold on;
plot(xp1bt,pp1bt);
hold off
xlim([0.5*min(edb(:,1)) 1.25*max(edb(:,1))]);
ylim([1.2*min(log(edb(:,3))) 1.0]);
pbaspect([1 1 1]);
exportgraphics(gcf,'corder_de_bound.png');

% 無限区間積分の誤差片対数グラフ
fprintf('\n***** Calculate the convergent order of DE formula (infinite interval case)\n');

% ヘッダー部分を除いて変数へ結果を代入
edi = readmatrix('exc-de-inf.dat','NumHeaderLines',1);

% 収束次数格納用ファイル
fileID = fopen('corder_de_inf.dat','w');

% polyfit関数による直線フィッティング
p1it = polyfit(edi(:,1),log(edi(:,3)),1);
fprintf('Conv. order = %17.15e\n',abs(p1it(1)));
fprintf('a = %17.15e\nb = %17.15e\n',p1it(1),p1it(2));
fprintf(fileID,'\nConv. order = %17.15e\n',abs(p1it(1)));
fprintf(fileID,'a = %17.15e\nb = %17.15e\n',p1it(1),p1it(2));
fclose(fileID);

% 片対数グラフの描画と画像ファイルへの保存
xp1it = linspace(0.5*min(edi(:,1)),1.25*max(edi(:,1)),1000);
pp1it = polyval(p1it,xp1it);
figure;
plot(edi(:,1),log(edi(:,3)),"o");
hold on;
plot(xp1it,pp1it);
hold off
xlim([0.5*min(edi(:,1)) 1.25*max(edi(:,1))]);
ylim([1.2*min(log(edi(:,3))) 1.0]);
pbaspect([1 1 1]);
exportgraphics(gcf,'corder_de_inf.png');
