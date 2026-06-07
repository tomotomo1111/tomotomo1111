fun = @(x,y) x.*y;
ymax = @(x) sqrt(4-x.*x);
exactval = 9.0/8;
q = integral2(fun,1,2,0,ymax);
fprintf("数値積分値 = %17.15e (絶対誤差 = %e)\n", q, abs(q-exactval));
