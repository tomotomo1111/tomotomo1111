function [x,rvec] = cgm(A,b,x0)
    % 収束判定定数
    eps = 1.e-8;
    % 行列・ベクトル次元取得
    [n,~] = size(b);
    % 最大反復回数
    kmax = 2*n;
    % 収束状況（true->収束，false->収束していない）
    fconv = true;
    % 残差一時格納用配列
    rvectmp = zeros(kmax,1);
    rvectmp(1) = norm(r,2);
    
    bnorm = norm(b,2);
    r = b - A*x0;
    p = r; x = x0;
    k = 0;
    while norm(r,2) >= eps*bnorm
        % [演習] 共役勾配法関数を完成させてください

        k = k + 1;
        rvectmp(k) = norm(r,2);
        if k > kmax
            fprintf('最大反復回数（%d）に到達しました．終了します\n',kmax);
            fconv = false;
            rvec = rvectmp;
            break;
        end
    end
    if fconv == true
        fprintf('収束しました：反復回数 = %d\n',k);
        rvec = rvectmp(1:k);
    end
end