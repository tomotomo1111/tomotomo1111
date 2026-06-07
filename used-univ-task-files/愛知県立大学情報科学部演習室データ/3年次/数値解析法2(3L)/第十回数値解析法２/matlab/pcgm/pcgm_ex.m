clearvars;
close all;
% 共役勾配法関数 pcgm 実行例

% 行列データ読み込み
load('Kuu.mat');
%load('bcsstk15.mat');

fileID = fopen('result_pcgm.txt','a');

% 行列設定と行列構造表示
A = Problem.A;
pn = Problem.name;
[n,~]=size(A);
figure;
spy(A,'r.',0.01);
nsp = nnz(A);
if strcmp(pn,'MathWorks/Kuu') == 1
    saveas(gcf,'pcgm_ex_mat_kuu.png');
    fprintf('行列データ：Kuu.mat\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
    fprintf(fileID,'行列データ：Kuu.mat\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
else
    saveas(gcf,'pcgm_ex_mat_bcs15.png');
    fprintf('行列データ：bcsstk15.mat\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
    fprintf(fileID,'行列データ：bcsstk15.mat\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
end

% 真の解ベクトルと右辺ベクトル生成
xexact = ones(n,1); b = A*xexact;
xexnorm = norm(xexact,2);

% 共役勾配法による求解
tol = 1e-8; % 終了判定定数
maxit = 2*n; %最大反復回数

% 初期ベクトル
xini = zeros(n,1);

% 前処理なし
[x0,fl0,it0,rv0] = pcgm(A,b,xini,speye(n));
if fl0 == true
    fprintf('前処理なしCG法: 収束（反復回数 %d）\n',it0);
    fprintf(fileID,'前処理なしCG法: 収束（反復回数 %d）\n',it0);
else
    fprintf('前処理なしCG法: 失敗（最大反復回数 %d で収束せず）\n',2*n);
    fprintf(fileID,'前処理なしCG法: 失敗（最大反復回数 %d で収束せず）\n',2*n);
end

% 前処理あり (ichol(0))
if strcmp(pn,'MathWorks/Kuu') == 1
    L1 = ichol(A);
    [x1,fl1,it1,rv1] = pcgm(A,b,xini,L1);
    if fl1 == true
        fprintf('前処理付きCG法（ichol(0)）: 収束（反復回数 %d）\n',it1);
        fprintf(fileID,'前処理付きCG法（ichol(0)）: 収束（反復回数 %d）\n',it1);
    else
        fprintf('前前処理付きCG法（ichol(0)）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
        fprintf(fileID,'前処理付きCG法（ichol(0)）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
    end
end

% 前処理あり (ichol(1e-3))
L2 = ichol(A,struct('type','ict','droptol',1e-3));
[x2,fl2,it2,rv2] = pcgm(A,b,xini,L2);
if fl2 == true
    fprintf('前処理付きCG法（ichol(1e-3)）: 収束（反復回数 %d）\n',it2);
    fprintf(fileID,'前処理付きCG法（ichol(1e-3)）: 収束（反復回数 %d）\n',it2);
else
    fprintf('前前処理付きCG法（ichol(1e-3)）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
    fprintf(fileID,'前処理付きCG法（ichol(1e-3)）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
end

% 前処理あり (ichol(1e-3)+diag)
alpha = max(sum(abs(A),2)./diag(A))-2;
L3 = ichol(A, struct('type','ict','droptol',1e-3,'diagcomp',alpha));
[x3,fl3,it3,rv3] = pcgm(A,b,xini,L3);
if fl3 == true
    fprintf('前処理付きCG法（ichol(1e-3)+diag）: 収束（反復回数 %d）\n',it3);
    fprintf(fileID,'前処理付きCG法（ichl(1e-3)+diag）: 収束（反復回数 %d）\n',it3);
else
    fprintf('前前処理付きCG法（ichol(1e-3)+diag）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
    fprintf(fileID,'前処理付きCG法（ichol(1e-3)+diag）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
end

% 相対誤差の計算と残差グラフ描画
if strcmp(pn,'MathWorks/Kuu') == 1
    rerr0 = norm(x0-xexact,2)/xexnorm;
    rerr1 = norm(x1-xexact,2)/xexnorm;
    rerr2 = norm(x2-xexact,2)/xexnorm;
    rerr3 = norm(x3-xexact,2)/xexnorm;
    fprintf('相対誤差:\n 前処理なし = %e\n 前処理あり (ichol(0)) = %e\n 前処理あり (ichol(1e-3)) = %e\n 前処理あり (ichol(1e-3)+diag) = %e\n',rerr0,rerr1,rerr2,rerr3);
    fprintf(fileID,'相対誤差:\n 前処理なし = %e\n 前処理あり (ichol(0)) = %e\n 前処理あり (ichol(1e-3)) = %e\n 前処理あり (ichol(1e-3)+diag) = %e\n',rerr0,rerr1,rerr2,rerr3);
    figure;
    semilogy(0:length(rv0)-1,rv0,'-r',0:length(rv1)-1,rv1,'-b',0:length(rv2)-1,rv2,'-g');
    xlim([0 length(rv0)+10]);
    legend('No preconditioning','ichol(0)','ichol(1e-3)','Location','northeast');
    saveas(gcf,'pcgm_ex_res_kuu.png');
    figure;
    semilogy(0:length(rv0)-1,rv0,'-r',0:length(rv1)-1,rv1,'-b',0:length(rv2)-1,rv2,'-g');
    xlim([0 length(rv1)+100]);
    legend('No preconditioning','ichol(0)','ichol(1e-3)','Location','northeast');
    saveas(gcf,'pcgm_ex_res_kuu_fc.png');
else
    rerr0 = norm(x0-xexact,2)/xexnorm;
    rerr2 = norm(x2-xexact,2)/xexnorm;
    rerr3 = norm(x3-xexact,2)/xexnorm;
    fprintf('相対誤差:\n 前処理なし = %e\n 前処理あり (ichol(1e-3)) = %e\n 前処理あり (ichol(1e-3)+diag) = %e\n',rerr0,rerr2,rerr3);
    fprintf(fileID,'相対誤差:\n 前処理なし = %e\n 前処理あり (ichol(1e-3)) = %e\n 前処理あり (ichol(1e-3)+diag) = %e\n',rerr0,rerr2,rerr3);
    figure;
    semilogy(0:length(rv0)-1,rv0,'-r',0:length(rv2)-1,rv2,'-g');
    xlim([0 length(rv0)+10]);
    legend('No preconditioning','ichol(1e-3)','Location','northeast');
    saveas(gcf,'pcgm_ex_res_bcs15.png');
    figure;
    semilogy(0:length(rv0)-1,rv0,'-r',0:length(rv2)-1,rv2,'-g');
    xlim([0 length(rv2)+100]);
    legend('No preconditioning','ichol(1e-3)','Location','northeast');
    saveas(gcf,'pcgm_ex_res_bcs15_fc.png');
end

fclose(fileID);
