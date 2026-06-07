function uval = exactu_heat1d(x,t,kappa)
    uval = exp(-kappa*pi^2*t)' .* sin(pi*x);
end