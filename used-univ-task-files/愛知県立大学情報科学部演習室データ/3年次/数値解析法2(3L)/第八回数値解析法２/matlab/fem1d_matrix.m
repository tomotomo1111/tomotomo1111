function [matM, matA]= fem1d_matrix(xi,pfunc,qfunc)
    [~,n] = size(xi);
    n = n -2;
    a = zeros(n,1);
    bl = zeros(n,1);
    bu = zeros(n,1);
    md = zeros(n,1);
    ml = zeros(n,1);
    mu = zeros(n,1);
    for i = 1:n-1
        hm = xi(i+1)-xi(i);
        hp = xi(i+2) - xi(i+1);
        %
        % 行列Aの三重対角成分 bl(i), a(i), bu(i+1)の計算コードを入力してください
        %
        md(i) = (hm + hp)/3;
        ml(i) = hm/6;
        mu(i+1) = ml(i);
    end
    hm = xi(n+1)-xi(n);
    hp = xi(n+2) - xi(n+1);
    a(n) = integral(@(x) pfunc(x)+qfunc(x).*(x-xi(n)).^2,xi(n),xi(n+1))/hm^2 ...
                + integral(@(x) pfunc(x)+qfunc(x).*(xi(n+2)-x).^2,xi(n+1),xi(n+2))/hp^2;
    md(n) = (hm + hp)/3;
    matA = spdiags([bl a bu],-1:1,n,n);
    matM = spdiags([ml md mu],-1:1,n,n);
end