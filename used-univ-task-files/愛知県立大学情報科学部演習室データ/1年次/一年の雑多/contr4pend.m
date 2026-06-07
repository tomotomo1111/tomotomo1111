close all         % すべてのFigureを閉じる
clear variables   % すべての変数をクリアする

%% パラメータ設定
% 物理パラメータ
global M l J c ga
M  = 1;      % 質量 [kg]
l  = 0.5;    % 回転軸から重心までの距離 [m]
J  = 1;      % 慣性モーメント [kg m^2]
c  = 0.XX;   % 粘性摩擦係数 [Nm/(rad/s)]　※XX：学籍番号の下2桁
ga = 9.81;   % 重力加速度 [m/s^2]

% 時間パラメータ
t0 = 0;  dt = 0.01;  tf = 10;

% 初期状態（初期時刻での状態）
x0 = [ pi/2; 0 ];   % x(0) = [ theta(0), thetadot(0) ]

% 制御ゲイン（パラメータ）
global kp kd
kp = 1;   % 比例（Proportional; P）ゲイン
kd = 1;   % 微分（Differential; D）ゲイン


%% 常微分方程式を数値的に解く：詳細モデル（非線形システム）の場合
% 状態の解軌道を数値計算（nls: non-linear systemの略）
[t4nls, x4nls] = ode45( @(t,x) pend_nlnsys(t,x), t0:dt:tf, x0 );
% 制御入力の時系列データ算出
u4nls = arrayfun(@(t,x1,x2) ux(t,[x1;x2]), t4nls,x4nls(:,1),x4nls(:,2));

%% 常微分方程式を数値的に解く：設計用モデル（x = xe まわりで近似した線形システム）の場合
% システム表現の一部（システム行列）
global A b
A = [  ];   % 適切に定義する
b = [  ];   % 適切に定義する
disp('Aの固有値は')
eig(A)
% 状態の解軌道を数値計算（als: approximately linearized systemの略）
[t4als, x4als] = ode45( @(t,x) pend_alnsys(t,x), t0:dt:tf, x0 );
% 制御入力の時系列データ算出
u4als = arrayfun(@(t,x1,x2) ux(t,[x1;x2]), t4als,x4als(:,1),x4als(:,2));

%% 結果表示（データの可視化）
figure(1)
subplot(3,1,1)
plot( t4nls,x4nls(:,1), 'LineWidth',2), hold on
plot( t4als,x4als(:,1), '--','LineWidth',2)
legend('original','linearized', 'Location','best')
ylabel('\theta [rad]'), grid on
subplot(3,1,2)
plot( t4nls,x4nls(:,2), 'LineWidth',2 ), hold on
plot( t4als,x4als(:,2), '--','LineWidth',2)
legend('original','linearized', 'Location','best')
ylabel('d\theta/dt [rad/s]'), grid on
subplot(3,1,3)
plot( t4nls,u4nls, 'LineWidth',2), hold on
plot( t4als,u4als, '--','LineWidth',2)
legend('for original','for linearized', 'Location','best')
xlabel('t [s]'), ylabel('\tau [Nm]'), grid on
print -dpdf -fillpage 'result_t-x+u.pdf'
%print -depsc 'result_t-x+u.eps'

show_anim(2,l,t4nls,x4nls)


%% ローカル関数

% 各種制御則（いずれか一行のみ有効化し，必要に応じて編集する）
function u = ux(t, x)
    global kp kd
    % 1) 無制御（駆動なし）
%    u = 0*t;
    % 2) PD制御（ただし，kd=0 => P制御，kp=0 => D制御）
    u = -[kp kd]*x;   % (= -kp*x(1) - kd*x(2))
end

% 詳細モデル（非線形システム）
function dxdt = pend_nlnsys(t, x)
    global M l J c ga
    % システム表現の一部（ベクトル場）
    f = [  ];   % 適切に定義する
    g = [   0;
          1/J ];
    % ODE
    dxdt = f + g*ux(t,x);
end

% 設計用モデル（x = xe まわりで近似した線形システム）
function dxdt = pend_alnsys(t, x)
    global A b
    % ODE
    dxdt = A*x + b*ux(t,x);
end

function show_anim(fid,l,tc,xc)
    figure(fid), clf
    ax = gca;  ax.XAxisLocation = 'origin';  ax.YAxisLocation = 'origin';
    axis equal, xlim([-2.5*l 2.5*l]), ylim([-2.5*l 2.5*l])
    xlabel('X (m)'), ylabel('Y (m)'), grid on, hold off, hold on
    p1 = @(t) zeros(numel(t), 2);
    pE = @(theta) [ 2*l*sin(theta), -2*l*cos(theta) ];
    alpha = 0.5;  p1t = p1(tc);  pEt = pE(xc(:,1));
    for i=1:(numel(tc)-1)/50:numel(tc)
        if i==1
            plot([p1t(i,1) pEt(i,1)],...
                 [p1t(i,2) pEt(i,2)],'b-', 'Linewidth',5)
            plot(p1t(i,1),p1t(i,2),'bo', 'MarkerSize',10,...
                 'MarkerFaceColor',[0,0,0]+alpha,'MarkerEdgeColor','b')
            plot(pEt(i,1),pEt(i,2),'bo', 'MarkerSize',10,...
                 'MarkerFaceColor','m', 'MarkerEdgeColor','b')
        elseif i==numel(tc)
            plot([p1t(i,1) pEt(i,1)],...
                 [p1t(i,2) pEt(i,2)],'k-', 'Linewidth',5)
            plot(p1t(i,1),p1t(i,2),'ko', 'MarkerSize',10,...
                 'MarkerFaceColor',[0,0,0]+alpha,'MarkerEdgeColor','k')
            plot(pEt(i,1),pEt(i,2),'ko', 'MarkerSize',10,...
                 'MarkerFaceColor','m', 'MarkerEdgeColor','k')
        else
            plot([p1t(i,1) pEt(i,1)],...
                 [p1t(i,2) pEt(i,2)],'c-', 'Linewidth',3)
        end
        pause(.25)
    end
    plot(pEt(:,1),pEt(:,2),'r--', 'Linewidth',3), hold off
    print -dpdf -fillpage 'result_link-motion.pdf'
    %print -depsc 'result_link-motion.eps'
end