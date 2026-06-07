function [x,fconv,itr,rvec] = bicgstabm(A,b,x0,L,U)
    eps = 1.e-12;
    [n,~] = size(b);
    kmax = 2*n;
    bnorm = norm(b,2);
    r = b - A*x0; p = U\(L\r);
    rvectmp = zeros(kmax,1);
    rvectmp(1) = norm(r,2);
    rdot = p;
    rho = dot(rdot,p);
    x = x0;
    k = 0;
    fconv = true;
    while norm(r,2) >= eps*bnorm
        % [問題3.12] 前処理付き安定化双共役勾配法関数を完成させてください
        k = k + 1;
        rvectmp(k) = norm(r,2);
        if k > kmax
            fconv = false;
            rvec = rvectmp;
            itr = k-1;
            break;
        end
    end
    if fconv == true
        rvec = rvectmp(1:k);
        itr = k;
    end
end