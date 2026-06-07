function [x, sol] = fem1d(n,itval,bdval,cofp,cofq,dcofp,srcf)
    x = linspace(itval(1),itval(2),n);
    wd = itval(2) - itval(1);
    v = @(x) bdval(1)*(itval(2)-x)/wd + bdval(2)*(x - itval(1))/wd;
    f = @(x) srcf(x)-cofq(x)*v(x) + (bdval(2)-bdval(1))/wd*dcofp(x);
    matA = fem1d_matrix(x,cofp,cofq);
    fvec = fem1d_rvec(x,f);
    utmp = matA\fvec;
    sol = [0; utmp; 0] + v(x)';
end