%
% [入力]
%  A: 係数行列（正方行列）
%  b: 右辺ベクトル
%  x0: 初期ベクトル
%  L,U: 前処理行列
%
% [出力]
%  x: 解ベクトル
%  fconv: 収束状況 (収束 -> true, 収束せず -> false)
%  itr: 反復回数
%  rvec: 残差履歴格納配列
%
function [x,fconv,itr,rvec] = bicgm(A,b,x0,L,U)
    eps = 1.e-12; % 収束判定定数
    [n,~] = size(b);
    kmax = 2*n; % 最大反復回数
    bnorm = norm(b,2);
    rvectmp = zeros(kmax,1);
    r = b - A*x0; p = U\(L\r);
    rvectmp(1) = norm(r,2);

    % [演習] BiCG法関数を完成させてください
    
    if fconv == true
        %fprintf('収束しました：反復回数 = %d\n',k);
        rvec = rvectmp(1:k);
        itr = k;
    end
end