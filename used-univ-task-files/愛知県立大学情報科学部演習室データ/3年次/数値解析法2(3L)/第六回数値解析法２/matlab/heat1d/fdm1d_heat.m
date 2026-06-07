function [tfdm, xfdm, ufdm] = fdm1d_heat(n,itval,nt,lastT,kappa,u0func)
    % 空間方向刻み幅
    dx = (itval(2)-itval(1))/(n+1);
    % 時間方向刻み幅
    dt = lastT/nt;
    % 安定性チェック
    r = kappa*dt/dx^2;
    fprintf('パラメータ r = %e\n',r);
    if r > 0.5
        fprintf('エラー：数値的に安定ではありません．空間・時間分割数を見直してください．\n');
        return;
    end

    % 空間・時間方向分割
    tfdm = linspace(0,lastT,nt+1);
    xfdm = linspace(itval(1),itval(2),n+2);

    % 近似解格納行列の生成
    ufdmtmp = zeros(n,nt+1);

    % 初期値格納
    u0tmp = u0func(xfdm)';
    ufdmtmp(:,1) = u0tmp(2:n+1);
    
    % 差分行列の作成
    a = r*ones(n,1); a(n,1) = 0;
    b = (1-2*r)*ones(n,1);
    c = r*ones(n,1); c(1,1) = 0;
    matA = spdiags([a b c],-1:1,n,n);

    % 陽的Euler法による近似計算
    for j = 1:nt
        ufdmtmp(:,j+1) = matA*ufdmtmp(:,j);
    end

    ufdm = [zeros(1,nt+1); ufdmtmp; zeros(1,nt+1)]';
end