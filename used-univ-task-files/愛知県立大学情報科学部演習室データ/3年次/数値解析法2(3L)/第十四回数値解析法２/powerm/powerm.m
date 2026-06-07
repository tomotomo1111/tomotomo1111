function [lam,x,iter,iconv] = powerm(A,x0,m)
    eps = 1e-8;
    [n,~] = size(A);
    itmax = n;
    x = x0/norm(x0,2);
    lam = zeros(m,1);
    phi = zeros(n,m);
    iter = zeros(m,1);
    iconv = zeros(m,1);
    for i = 1:m
        iter(i) = 0;
        icont = true;
        iconv(i)=false;
        y = x;
        if i > 1
            for j = 1:i-1
                y = y - dot(x,phi(:,j)) * phi(:,j);
            end
        end
        ynorm = norm(y,2);
        phi(:,i) = y / ynorm;
        while icont == true
            % [演習] べき乗法プログラムを完成させてください．
            iter(i) = iter(i) + 1;
            if iter(i) == itmax
                fprintf('べき乗法: 収束しませんでした (%d-th 固有値, 最大反復回数 = %d)\n',i,itmax);
                icont = false;
            elseif ynorm^2-abs(lam(i))^2 < eps
                fprintf('べき乗法: 収束しました (%d-th 固有値, 反復回数 = %d)\n',i,iter(i));
                icont = false;
                iconv(i) = true;
            end
        end
    end
end