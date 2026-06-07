clearvars; clf; close all;
% ode113使用例1（硬くない問題）

% 例題のパラメータ設定
tspan = [0 10*pi];
u0 = [1.0 0.0];
omega0 = 1.0;
gamma = 0.3 * omega0;

% 実行履歴のファイル出力
if isfile("run1_result.txt") ==1
    % 古い履歴は別ファイルへ上書き保存
    movefile("run1_result.txt","run1_old.txt");
end
diary("run1_result.txt");

% ode113による例題計算
fprintf('***** ode113\n')
opts = odeset('RelTol',1e-5,'AbsTol',1e-7);
tic; [t113,u113] = ode113(@(t,u) odefcn(t,u,gamma,omega0),tspan,u0,opts); toc

% 真の解との絶対最大誤差計算
exvec113 = ex113u(t113,gamma,omega0,u0);
erru113 = norm(u113(:,1)-exvec113(:,1),Inf);
errv113 = norm(u113(:,2)-exvec113(:,2),Inf);
fprintf('Num. of division = %d\n',length(t113));
fprintf('Max error (disp) = %17.15e\nMax error (velo) = %17.15e\n',erru113,errv113);

% 実行履歴出力終了
diary off;

% 真の解と計算結果のプロット
tsol = 0:pi/100:10*pi;
exuplot = ex113u(tsol,gamma,omega0,u0);
% 変位のプロット
subplot(2,1,1);
plot(t113,u113(:,1),'o',tsol,exuplot(:,1),'-');
xlim(tspan);
ylim([-1 1]);
xlabel('t');
ylabel('displacement');
legend('ode113','exact disp');
% 速度のプロット
subplot(2,1,2);
plot(t113,u113(:,2),'*',tsol,exuplot(:,2),'-');
xlim(tspan);
ylim([-1 1]);
xlabel('t');
ylabel('velocity');
legend('ode113','exact velo');
% 画像の出力 (他の形式も可能)
saveas(gcf,'nonstf_ode113_result.png');
