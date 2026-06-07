function [lam,x,iter,iconv] = invpm(A,applam)
    eps = 1e-8;
    [n,~] = size(A);
    itmax = 20*n;
    x0 = randn(n,1);
    x = x0/norm(x0,2);
    iconv = false;
    icont = true;
    iter = 0;
    Alam = A - applam*eye(n);
    [L,U,P] = lu(Alam);
    while icont == true
        % [演習] 逆反復法プログラムを完成させてください
        iter = iter + 1;
        if iter == itmax
            fprintf('逆反復法: 収束しませんでした (最大反復回数 = %d)\n',itmax);
            icont = false;
        elseif ynorm^2-abs(mu)^2 < eps
            fprintf('逆反復法: 収束しました (反復回数 = %d)\n',iter);
            icont = false;
            iconv = true;
        end
    end
    lam = applam + 1/mu;
end