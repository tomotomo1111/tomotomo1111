clearvars;
close all;
% BiCGSTAB法関数 bicgstabm 実行例

% 行列データ読み込み
load west0479;
A = west0479;
pn = '';
fileID = fopen('result_bicgstabm_west.txt','w');
%load('cryg10000.mat');
%A = Problem.A;
%pn = Problem.name;
%fileID = fopen('result_bicgstabm_cryg.txt','w');

[n,~]=size(A);
figure;
spy(A,'r.',0.01);
nsp = nnz(A);

if strcmp(pn,'Bai/cryg10000') == 1
    saveas(gcf,'bicgstabm_ex_mat_cryg.png');
    fprintf('行列データ：cryg10000\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
    fprintf(fileID,'行列データ：cryg10000\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
else
    saveas(gcf,'bicgstabm_ex_mat_west.png');
    fprintf('行列データ：west0479\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
    fprintf(fileID,'行列データ：west0479\n行列次元 = %d  非零要素数 = %d (非零要素率: %f)\n',n,nsp,nsp/(n*n));
end

% 真の解ベクトルと右辺ベクトル生成
xexact = ones(n,1); b = A*xexact;
xexnorm = norm(xexact,2);

% 初期ベクトル
xini = zeros(n,1);

% 前処理なし
[x0,fl0,it0,rv0] = bicgstabm(A,b,xini,speye(n),speye(n));
if fl0 == true
    fprintf('前処理なしBiCGSTAB法: 収束（反復回数 %d）\n',it0);
    fprintf(fileID,'前処理なしBiCGSTAB法: 収束（反復回数 %d）\n',it0);
else
    fprintf('前処理なしBiCGSTAB法: 失敗（最大反復回数 %d で収束せず）\n',2*n);
    fprintf(fileID,'前処理なしBiCGSTAB法: 失敗（最大反復回数 %d で収束せず）\n',2*n);
end

% 前処理あり (ILU(1e-6))
setup = struct('type','ilutp','droptol',1e-6);
[L1,U1] = ilu(A,setup);
[x1,fl1,it1,rv1] = bicgstabm(A,b,xini,L1,U1);
if fl1 == true
    fprintf('前処理付きBiCGSTAB法（ILU(1e-6)）: 収束（反復回数 %d）\n',it1);
    fprintf(fileID,'前処理付きBiCGSTAB法（ILU(1e-6)）: 収束（反復回数 %d）\n',it1);
else
    fprintf('前処理付きBiCGSTAB法（ILU(1e-6)）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
    fprintf(fileID,'前処理付きBiCGSTAB法（ILU(1e-6)）: 失敗（最大反復回数 %d で収束せず）\n',2*n);
end

% 相対誤差の計算と残差グラフ描画
rerr0 = norm(x0-xexact,2)/xexnorm;
rerr1 = norm(x1-xexact,2)/xexnorm;
fprintf('相対誤差:\n 前処理なし = %e\n 前処理あり (ILU(1e-6)) = %e\n',rerr0,rerr1);
fprintf(fileID,'相対誤差:\n 前処理なし = %e\n 前処理あり (ILU(1e-6)) = %e\n',rerr0,rerr1);
figure;
semilogy(0:length(rv0)-1,rv0,'-r',0:length(rv1)-1,rv1,'-b');
xlim([0 length(rv0)+10]);
legend('No preconditioning','ILU(1e-6)','Location','southeast');
if strcmp(pn,'Bai/cryg10000') == 1
    saveas(gcf,'bicgstabm_ex_cryg_res.png');
else
    saveas(gcf,'bicgstabm_ex_west_res.png');
end
figure;
semilogy(0:length(rv0)-1,rv0,'-o',0:length(rv1)-1,rv1,'-^');
xlim([0 length(rv1)+10]);
legend('No preconditioning','ILU(1e-6)','Location','southeast');
if strcmp(pn,'Bai/cryg10000') == 1
    saveas(gcf,'bicgstabm_ex_res_cryg_fc.png');
else
    saveas(gcf,'bicgstabm_ex_res_west_fc.png');
end

fclose(fileID);
