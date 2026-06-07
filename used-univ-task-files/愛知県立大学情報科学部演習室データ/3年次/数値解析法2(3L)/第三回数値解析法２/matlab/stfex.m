clearvars; clf; close all;
% 硬い問題（例題）

% 例題のパラメータ設定
delta = 1.0e-2;
tlast = -log(delta)/ abs(-4.0e-4);
tspan = [0 tlast];
u0 = [0.0 2.0];

% 実行履歴のファイル出力
if isfile("run_result.txt") ==1
    % 古い履歴は別ファイルへ上書き保存
    movefile("run_result.txt","run_old.txt");
end
diary("run_result.txt");

% オプション設定
opts = odeset('RelTol',1e-5,'AbsTol',1e-7);

% ode45による例題計算
fprintf('\n***** ode45\n');
tic; [t45,u45] = ode45(@odestf,tspan,u0,opts); toc
% 真の解との絶対最大誤差計算
exvec = exstf(t45);
erru = norm(u45(:,1)-exvec(:,1),Inf);
errv = norm(u45(:,2)-exvec(:,2),Inf);
fprintf('Num. of division = %d\n',length(t45));
fprintf('Max error (disp) = %17.15e\nMax error (velo) = %17.15e\n',erru,errv);

% 実行履歴出力終了
diary off;

% 真の解と計算結果のプロット
tsol = 0:tlast/100:tlast;
exuplot = exstf(tsol);

% 変位のプロット
subplot(2,1,1);
plot(t45,u45(:,1),'o',tsol,exuplot(:,1),'-');
xlim(tspan);
ylim([-0.25 1.1]);
xlabel('t');
ylabel('displacement');
legend('ode45','exact disp');
% 速度のプロット
subplot(2,1,2);
plot(t45,u45(:,2),'*',tsol,exuplot(:,2),'-');
xlim(tspan);
ylim([-0.25 2.1]);
xlabel('t');
ylabel('velocity');
legend('ode45','exact velo');
% 画像の出力 (他の形式も可能)
saveas(gcf,'ex_result.png');