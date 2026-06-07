function [x,fconv,itr,rvec] = pcgm(A,b,x0,L)
    eps = 1.e-8;
    [n,~] = size(b);
    kmax = 2*n;
    bnorm = norm(b,2);
    r = b - A*x0;
    rvectmp = zeros(kmax,1);
    rvectmp(1) = norm(r,2);
    p = L.'\(L\r); x = x0;
    rho = dot(r,p);
    k = 0;
    fconv = true;
    while norm(r,2) >= eps*bnorm
        % [演習] 前処理付き共役勾配法プログラムを完成させてください．
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