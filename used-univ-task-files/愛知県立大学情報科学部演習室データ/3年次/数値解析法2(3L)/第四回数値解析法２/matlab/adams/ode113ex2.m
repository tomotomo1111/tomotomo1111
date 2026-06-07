% ode113使用例2（硬い問題）

% 例題のパラメータ設定
delta = 1.0e-2;
tlast = -log(delta)/ abs(-4.0e-4);
tspan = [0 tlast];
u0 = [0.0 2.0];

% 実行履歴のファイル出力
if isfile("run2_result.txt") ==1
    % 古い履歴は別ファイルへ上書き保存
    movefile("run2_result.txt","run2_old.txt");
end
diary("run2_result.txt");

% ode113による例題計算
opts = odeset('RelTol',1e-5,'AbsTol',1e-7,'Stats','on');
tic; [t113,u113] = ode113(@odestf,tspan,u0,opts); toc

% 真の解との絶対最大誤差計算
exvec113 = exstf(t113);
erru113 = norm(u113(:,1)-exvec113(:,1),Inf);
errv113 = norm(u113(:,2)-exvec113(:,2),Inf);
fprintf('Num. of division = %d\n',length(t113));
fprintf('Max error (disp) = %17.15e\nMax error (velo) = %17.15e\n',erru113,errv113);

% 実行履歴出力終了
diary off;

% 真の解と計算結果のプロット
tsol = 0:tlast/100:tlast;
exuplot = exstf(tsol);
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
ylim([-2.1 2.1]);
xlabel('t');
ylabel('velocity');
legend('ode113','exact velo');
% 画像の出力 (他の形式も可能)
saveas(gcf,'stf_ode113_result.png');
