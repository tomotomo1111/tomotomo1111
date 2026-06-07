function [x,fconv,itr,rvec] = cgsm(A,b,x0,L,U)
    eps = 1.e-12;
    [n,~] = size(b);
    kmax = 2*n;
    bnorm = norm(b,2);
    r = b - A*x0; p = U\(L\r); u = p;
    rvectmp = zeros(kmax,1);
    rvectmp(1) = norm(r,2);
    rdot = p;
    rho = dot(rdot,p);
    x = x0;
    k = 0;
    fconv = true;
    while norm(r,2) >= eps*bnorm
        % [問題3.10] 前処理付き共役勾配二乗法関数を完成させてください
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