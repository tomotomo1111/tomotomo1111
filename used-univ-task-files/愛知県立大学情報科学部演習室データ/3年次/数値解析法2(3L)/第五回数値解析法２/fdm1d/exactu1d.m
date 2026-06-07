function uval = exactu1d(x,k)
    uval = (x - sinh(k*x)/sinh(k))/k^2;
end