function fvec = fem1d_rvec(xi,funcf)
    [~,n] = size(xi);
    n = n - 2;
    fvec = zeros(n,1);
    for i = 1:n
        hm = xi(i+1) - xi(i);
        hp = xi(i+2) - xi(i+1);
        fvec(i) = integral(@(x) funcf(x).*(x-xi(i)),xi(i),xi(i+1))/hm ...
                        + integral(@(x) funcf(x).*(xi(i+2)-x),xi(i+1),xi(i+2))/hp;
    end
end