function [xfdm, ufdm] = fdm1d(n,itval,cofp,cofq,funcr,ubd)
    h = (itval(2)-itval(1))/(n+1);
    a = zeros(n,1);
    b = zeros(n,1);
    c = zeros(n,1);
    rvec = zeros(n,1);
    xfdm = linspace(itval(1),itval(2),n+2);

    % 差分行列と右辺ベクトルの作成
    b(1) = 2+h^2*cofq(xfdm(2)); c(2) = -1+h/2*cofp(xfdm(2));
    rvec(1) = h^2*funcr(xfdm(2)) +(1+h/2*cofp(xfdm(2)))*ubd(1);
    for i = 1:n-2
        a(i) = -1-h/2*cofp(xfdm(i+2));
        b(i+1) = 2+h^2*cofq(xfdm(i+2));
        c(i+2) = -1+h/2*cofp(xfdm(i+2));
        rvec(i+1) = h^2*funcr(xfdm(i+2));
    end
    a(n-1) = -1-h^2/2*cofp(xfdm(n+1));
    b(n) = 2+h^2/2*cofq(xfdm(n+1));
    matA = spdiags([a b c],-1:1,n,n);
    rvec(n) = h^2 * funcr(xfdm(n+1))+(1-h/2*cofp(xfdm(n+1)))*ubd(2);

    % 差分近似解の計算
    ufdmtmp = matA\rvec;

    ufdm = [ubd(1) ufdmtmp' ubd(2)];
end