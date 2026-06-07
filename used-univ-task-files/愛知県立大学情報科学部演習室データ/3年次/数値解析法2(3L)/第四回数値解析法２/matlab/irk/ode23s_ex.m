clearvars; clf; close all;
% 陰的解法（例題）

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

% ode23sによる例題計算（ヤコビ行列なし）
opts=odeset('RelTol',1e-5,'AbsTol',1e-7,'Stats','on');
fprintf('\n***** ode23s without Jacobian\n');
tStart = tic;
[t23s,u23s] = ode23s(@odestf,tspan,u0,opts);
tEnd = toc(tStart);
fprintf('Calculation time = %e [s]\n',tEnd);
fprintf('Num. of division = %d\n',length(t23s));
% 真の解との絶対最大誤差計算
exvec23s = exstf(t23s);
erru = norm(u23s(:,1)-exvec23s(:,1),Inf);
errv = norm(u23s(:,2)-exvec23s(:,2),Inf);
fprintf('Max error (disp) = %17.15e\nMax error (velo) = %17.15e\n',erru,errv);

% ode23sによる例題計算（ヤコビ行列あり）
opts=odeset('RelTol',1e-5,'AbsTol',1e-7,'Stats','on','Jacobian',@jacstf);
fprintf('\n***** ode23s with Jacobian\n');
tStart = tic;
[t23sj,u23sj] = ode23s(@odestf,tspan,u0,opts);
tEnd = toc(tStart);
fprintf('Calculation time = %e [s]\n',tEnd);
fprintf('Num. of division = %d\n',length(t23sj));
% 真の解との絶対最大誤差計算
exvec23sj = exstf(t23sj);
erru = norm(u23sj(:,1)-exvec23sj(:,1),Inf);
errv = norm(u23sj(:,2)-exvec23sj(:,2),Inf);
fprintf('Max error (disp) = %17.15e\nMax error (velo) = %17.15e\n',erru,errv);

% 実行履歴出力終了
diary off;

% 真の解と計算結果のプロット
tsol = 0:tlast/1000:tlast;
exuplot = exstf(tsol);

% ode23s without Jacobian
% 変位のプロット
subplot(2,1,1);
plot(t23s,u23s(:,1),'o',tsol,exuplot(:,1),'-');
xlim(tspan);
ylim([-0.25 1.1]);
xlabel('t');
ylabel('displacement');
legend('ode23s without Jacobian','exact disp');
% 速度のプロット
subplot(2,1,2);
plot(t23s,u23s(:,2),'*',tsol,exuplot(:,2),'-');
xlim(tspan);
ylim([-0.25 2.1]);
xlabel('t');
ylabel('velocity');
legend('ode23s without Jacobian','exact velo');
% 画像の出力 (他の形式も可能)
saveas(gcf,'stf_ode23s_result.png');

% ode23s with Jacobian
% 変位のプロット
subplot(2,1,1);
plot(t23sj,u23sj(:,1),'o',tsol,exuplot(:,1),'-');
xlim(tspan);
ylim([-0.25 1.1]);
xlabel('t');
ylabel('displacement');
legend('ode23s with Jacobian','exact disp');
% 速度のプロット
subplot(2,1,2);
plot(t23sj,u23sj(:,2),'*',tsol,exuplot(:,2),'-');
xlim(tspan);
ylim([-0.25 2.1]);
xlabel('t');
ylabel('velocity');
legend('ode23s with Jacobian','exact velo');
% 画像の出力 (他の形式も可能)
saveas(gcf,'stf_ode23sj_result.png');