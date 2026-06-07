% ode45使用例
%
% 例題のパラメータ設定
tspan = [0 10*pi];
u0 = [1.0 0.0];
omega0 = 1.0;
gamma = 0.3 * omega0;
% ode45による例題計算
[t,u] = ode45(@(t,u) odefcn(t,u,gamma,omega0),tspan,u0);
% 真の解との絶対最大誤差計算
exvec = ex45u(t,gamma,omega0,u0);
erru = norm(u(:,1)-exvec(:,1),Inf);
errv = norm(u(:,2)-exvec(:,2),Inf);
fprintf('Max error (disp) = %17.15e\nMax error (velo) = %17.15e\n',erru,errv);
% 真の解と計算結果のプロット
tsol = 0:pi/10:10*pi;
exuplot = ex45u(tsol,gamma,omega0,u0);
% 変位のプロット
subplot(2,1,1);
plot(t,u(:,1),'o',tsol,exuplot(:,1),'-');
xlim(tspan);
ylim([-1 1]);
xlabel('t');
ylabel('displacement');
legend('ode45','exact disp');
% 速度のプロット
subplot(2,1,2);
plot(t,u(:,2),'*',tsol,exuplot(:,2),'-');
xlim(tspan);
ylim([-1 1]);
xlabel('t');
ylabel('velocity');
legend('ode45','exact velo');
% 画像の出力 (他の形式も可能)
saveas(gcf,'ode45result.png');