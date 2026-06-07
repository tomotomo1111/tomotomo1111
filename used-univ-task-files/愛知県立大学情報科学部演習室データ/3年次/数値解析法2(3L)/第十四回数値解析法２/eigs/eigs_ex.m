clearvars;
close all;
% 関数 eigs 実行例

% 行列データ読み込み
n = 1000;
A = gallery('minij',n) ;

% 真の固有値
r1 = [n n-1 n-2 n-3 n-4];
teig1 = 0.25*sec(r1*pi/(2*n+1)).^2;
r2 = [1 2 3 4 5];
teig2 = 0.25*sec(r2*pi/(2*n+1)).^2;

% 求める固有値の数
m = 5;

% 絶対値の大きい順に固有値計算
[V1,D1,flag] = eigs(A,m);

fileID = fopen('result_eigs.txt','w');
fprintf('絶対値の大きい順:\n');
fprintf(fileID,'絶対値の大きい順:\n');
if flag == 0
    for i = 1:m
        err = abs(D1(i,i) - teig1(i))/abs(teig1(i));
        fprintf('  %d-th 近似固有値 = %e (相対誤差 = %e)\n',i,D1(i,i),err);
        fprintf(fileID,'  %d-th 近似固有値 = %e (相対誤差 = %e)\n',i,D1(i,i),err);
    end
else
    fprintf(fileID,'  収束しませんでした\n');
end

% 絶対値の小さい順に固有値計算
opts = struct('tol',1e-8,'maxit',n);
[V2,D2,flag] = eigs(A,m,'smallestabs',opts);

fprintf('絶対値の小さい順:\n');
fprintf(fileID,'絶対値の小さい順:\n');
if flag == 0
    for i = 1:m
        err = abs(D2(i,i) - teig2(i))/abs(teig2(i));
        fprintf('  %d-th 近似固有値 = %e (相対誤差 = %e)\n',i,D2(i,i),err);
        fprintf(fileID,'  %d-th 近似固有値 = %e (相対誤差 = %e)\n',i,D2(i,i),err);
    end
else
    fprintf('  収束しませんでした\n');
    fprintf(fileID,'  収束しませんでした\n');
end

fclose(fileID);
