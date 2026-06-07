clearvars;
close all;
% 共役勾配法実行プログラム

% 正定値行列の読み込み
load('Kuu.mat');
%load('bcsstk15.mat');
A = Problem.A;
[n,~] = size(A);
figure;
spy(A,'r.',0.01);
saveas(gcf,'cgm_ex_mat.png');
nsp = nnz(A);
fprintf('行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));

% 真の解ベクトルと右辺ベクトル生成
xexact = ones(n,1); b = A*xexact;

% 共役勾配法による求解
x0 = zeros(n,1);
[x,rvec] = cgm(A,b,x0);

% 相対誤差の計算
rerr = norm(x-xexact,2)/norm(xexact,2);
fprintf('相対誤差 = %e\n',rerr);

% 残差グラフの描画
figure;
semilogy(0:length(rvec)-1,rvec,'-');
saveas(gcf,'cgm_ex_res.png');
