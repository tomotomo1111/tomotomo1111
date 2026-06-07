clearvars;
close all;
% 逆反復関数 invpm 実行例

% 行列データ読み込み
n = 1000;
A = gallery('minij',n) ;

% 真の固有値
r = [500 1];
teig = 0.25*sec(r*pi/(2*n+1)).^2;

fileID = fopen('result_invpm.txt','w');

% 例1
applam = 0.5;

% 逆反復法による固有ベクトル計算および固有値補正
[lam1,x1,iter1,iconv1] = invpm(A,applam);

% 誤差計算
if iconv1 == true
    err = abs(lam1 - teig(1))/abs(teig(1));
    fprintf('補正固有値 = %e (相対誤差 = %e)\n',lam1,err);
    fprintf(fileID,'補正固有値 = %e (相対誤差 = %e, 反復回数 = %d)\n',lam1,err,iter1);
else
    fprintf(fileID,'例1: 収束しませんでした\n');
end

% 例2
applam = 0.0;

% 逆反復法による固有ベクトル計算および固有値補正
[lam2,x2,iter2,iconv2] = invpm(A,applam);

% 誤差計算
if iconv2 == true
    err = abs(lam2 - teig(2))/abs(teig(2));
    fprintf('絶対値最小固有値 = %e (相対誤差 = %e)\n',lam2,err);
    fprintf(fileID,'絶対値最小固有値 = %e (相対誤差 = %e, 反復回数 = %d)\n',lam2,err,iter2);
else
    fprintf(fileID,'絶対値最小固有値: 収束しませんでした\n');
end

fclose(fileID);