function fmat = fem1d_rvec(xi,tk,funcf)
    [~,n] = size(xi);
    [~,nt] = size(tk);
    n = n - 2;
    fmat = zeros(n,nt);
    for k = 1:nt
        for i = 1:n
            hm = xi(i+1) - xi(i);
            hp = xi(i+2) - xi(i+1);
            fmat(i,k) = integral(@(x) funcf(x,tk(k)).*(x-xi(i)),xi(i),xi(i+1))/hm ...
                        + integral(@(x) funcf(x,tk(k)).*(xi(i+2)-x),xi(i+1),xi(i+2))/hp;
        end
    end
end