clearvars;
close all;
% べき乗法関数 powerm 実行例

% 行列データ読み込み
n = 1000;
A = gallery('minij',n) ;

% 真の固有値
r = [n n-1 n-2 n-3 n-4];
teig = 0.25*sec(r*pi/(2*n+1)).^2;

% 求める固有値の数
m = 5;

% 初期ベクトル
x0 = ones(n,1);

% べき乗法による絶対値最大固有値計算
[lam,x,iter,iconv] = powerm(A,x0,m);

fileID = fopen('result_powerm.txt','w');
for i = 1:m
    if iconv(i) == true
        err = abs(lam(i) - teig(i))/abs(teig(i));
        fprintf('%d-th 近似固有値 = %e (相対誤差 = %e)\n',i,lam(i),err);
        fprintf(fileID,'%d-th 近似固有値 = %e (相対誤差 = %e, 反復回数 = %d)\n',i,lam(i),err,iter(i));
    else
        fprintf(fileID,'収束しませんでした (%d-th 固有値)\n',i);
    end
end
fclose(fileID);
